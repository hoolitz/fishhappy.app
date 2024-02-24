package tz.co.fishhappy.app.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.adapter.FavoriteAdapter;
import tz.co.fishhappy.app.adapter.FishAdapter;
import tz.co.fishhappy.app.adapter.OrderAdapter;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.FavoriteAddService;
import tz.co.fishhappy.app.endpoint.FavoriteRemoveService;
import tz.co.fishhappy.app.endpoint.FishListService;
import tz.co.fishhappy.app.endpoint.OrderCancelService;
import tz.co.fishhappy.app.endpoint.OrderListService;
import tz.co.fishhappy.app.model.FavoriteAddModel;
import tz.co.fishhappy.app.model.FavoriteRemoveModel;
import tz.co.fishhappy.app.model.FishModel;
import tz.co.fishhappy.app.model.OrderCancelResponse;
import tz.co.fishhappy.app.model.OrderModel;
import tz.co.fishhappy.app.model.Orders;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Simon on 28-Apr-17.
 */

public class ListOrderFragment extends Fragment implements OrderAdapter.OrderListener{

    @BindView(R.id.rvListOrder) RecyclerView mRvOrder;
    @BindView(R.id.srlOrder) SwipeRefreshLayout mSrlOrder;

    private OrderAdapter mOrderAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Realm mRealm;
    private Activity activity;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize and get instance of realm
        mRealm = Realm.getDefaultInstance();

        mPref = getActivity().getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_order, container, false);
        ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRvOrder.setLayoutManager(mLayoutManager);

        fetchOrderList();

        mSrlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchOrderList();
            }
        });

        return rootView;
    }

    private void fetchOrderList(){
        if(!mSrlOrder.isRefreshing())
            mSrlOrder.setRefreshing(true);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OrderListService service = retrofit.create(OrderListService.class);
        Call<List<Orders>> call = service.getOrderList("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""));
        call.enqueue(new retrofit2.Callback<List<Orders>>(){

            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                mSrlOrder.setRefreshing(false);
                List<Orders> list = response.body();
                mOrderAdapter = new OrderAdapter(getActivity(), list, ListOrderFragment.this);
                mRvOrder.swapAdapter(mOrderAdapter, false);
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("ORDER=faile");
                mSrlOrder.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCancelOrder(int position, int itemId) {
        mOrderAdapter.removeItem(position);
        cancelOrder(itemId);
    }

    private void cancelOrder(int itemId){
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        OrderCancelService service = retrofit.create(OrderCancelService.class);
        Call<OrderCancelResponse> call = service.cancelOrder("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""), String.valueOf(itemId));
        call.enqueue(new retrofit2.Callback<OrderCancelResponse>(){

            @Override
            public void onResponse(Call<OrderCancelResponse> call,
                                   Response<OrderCancelResponse> response) {
                fetchOrderList();
            }

            @Override
            public void onFailure(Call<OrderCancelResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
