package tz.co.fishhappy.app.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.FavoriteListService;
import tz.co.fishhappy.app.endpoint.FavoriteRemoveService;
import tz.co.fishhappy.app.model.FavoriteModel;
import tz.co.fishhappy.app.model.FavoriteRemoveModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Simon on 28-Apr-17.
 */

public class ListFavoriteFragment extends Fragment implements FavoriteAdapter.FavoriteListener{

    @BindView(R.id.rvListFavorite) RecyclerView mRvFavorite;
    @BindView(R.id.srlFavorite) SwipeRefreshLayout mSrlFavorite;

    private FavoriteAdapter mFavoriteAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Realm mRealm;
    private Activity activity;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    public interface FavoriteFragmentListener {

        void onFavoriteOrderClicked();

    }


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
        View rootView = inflater.inflate(R.layout.fragment_list_favorite, container, false);
        ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRvFavorite.setLayoutManager(mLayoutManager);

        fetchFavoriteList();

        mSrlFavorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFavoriteList();
            }
        });

        return rootView;
    }

    private void fetchFavoriteList(){
        if(!mSrlFavorite.isRefreshing())
            mSrlFavorite.setRefreshing(true);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FavoriteListService service = retrofit.create(FavoriteListService.class);
        Call<List<FavoriteModel>> call = service.getFavoriteList("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""));
        call.enqueue(new retrofit2.Callback<List<FavoriteModel>>(){

            @Override
            public void onResponse(Call<List<FavoriteModel>> call,
                                   Response<List<FavoriteModel>> response) {
                mSrlFavorite.setRefreshing(false);
                List<FavoriteModel> list = response.body();

                mFavoriteAdapter = new FavoriteAdapter(getActivity(), list,
                        ListFavoriteFragment.this);
                mRvFavorite.swapAdapter(mFavoriteAdapter, false);

            }

            @Override
            public void onFailure(Call<List<FavoriteModel>> call, Throwable t) {
                t.printStackTrace();
                mSrlFavorite.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRemoveFavorite(int position, int itemId) {
        removeFavorite(itemId, position);
    }

    @Override
    public void onAddOrder(final FavoriteModel item) {
        try {
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    // increment index
                    CartRealmObject cart = bgRealm.where(CartRealmObject.class)
                            .equalTo("id", item.getId())
                            .findFirst();
                    CartRealmObject order = new CartRealmObject();
                    if (cart != null) {
                        order.setQuantity(cart.getQuantity() + 1);
                        order.setId(cart.getId());
                        order.setCreatedAt(cart.getCreatedAt());
                        order.setPrice(cart.getPrice());
                        order.setName(cart.getName());
                        order.setUpdatedAt(System.currentTimeMillis());
                    } else {
                        order.setId(item.getId());
                        order.setQuantity(1);
                        order.setPrice(item.getPrice());
                        order.setName(item.getName());
                        order.setCreatedAt(System.currentTimeMillis());
                        order.setUpdatedAt(System.currentTimeMillis());
                    }
                    bgRealm.insertOrUpdate(order);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    // Transaction was a success.
                    System.out.println("Add item in cart");

                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    error.printStackTrace();
                }
            });
            try{
                ((FavoriteFragmentListener) activity).onFavoriteOrderClicked();
            }catch (ClassCastException cce){

            }
            Toast.makeText(getActivity(), "Item added in cart", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeFavorite(int itemId, final int position){
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FavoriteRemoveService service = retrofit.create(FavoriteRemoveService.class);
        Call<FavoriteRemoveModel> call = service.removeFavorite(itemId);
        call.enqueue(new retrofit2.Callback<FavoriteRemoveModel>(){

            @Override
            public void onResponse(Call<FavoriteRemoveModel> call,
                                   Response<FavoriteRemoveModel> response) {
                mFavoriteAdapter.removeItem(position);
            }

            @Override
            public void onFailure(Call<FavoriteRemoveModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
