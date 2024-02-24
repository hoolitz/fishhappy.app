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
import tz.co.fishhappy.app.adapter.FishAdapter;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.FavoriteAddService;
import tz.co.fishhappy.app.endpoint.FavoriteRemoveService;
import tz.co.fishhappy.app.endpoint.FishListService;
import tz.co.fishhappy.app.model.FavoriteAddModel;
import tz.co.fishhappy.app.model.FavoriteRemoveModel;
import tz.co.fishhappy.app.model.FishModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Simon on 28-Apr-17.
 */

public class ListFishFragment extends Fragment implements FishAdapter.FishListener{

    @BindView(R.id.rvListFish) RecyclerView mRvFish;
    @BindView(R.id.srlFish) SwipeRefreshLayout mSrlFish;

    private FishAdapter mFishAdapter;
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

    public interface FishFragmentListener {

        void onFishOrderClicked();

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
        View rootView = inflater.inflate(R.layout.fragment_list_fish, container, false);
        ButterKnife.bind(this, rootView);

        mLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRvFish.setLayoutManager(mLayoutManager);

        fetchFishList();

        mSrlFish.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFishList();
            }
        });

        return rootView;
    }

    private void fetchFishList(){
        if(!mSrlFish.isRefreshing())
            mSrlFish.setRefreshing(true);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FishListService service = retrofit.create(FishListService.class);
        Call<List<FishModel>> call = service.getFishList("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""));
        call.enqueue(new retrofit2.Callback<List<FishModel>>(){

            @Override
            public void onResponse(Call<List<FishModel>> call, Response<List<FishModel>> response) {
                mSrlFish.setRefreshing(false);
                List<FishModel> list = response.body();
                mFishAdapter = new FishAdapter(getActivity(), list, ListFishFragment.this,
                        mPref.getString(getString(R.string.pref_login_token),""));
                mRvFish.swapAdapter(mFishAdapter, false);
            }

            @Override
            public void onFailure(Call<List<FishModel>> call, Throwable t) {
                t.printStackTrace();
                mSrlFish.setRefreshing(false);
            }
        });
    }

    @Override
    public void onAddFavorite(int position, int itemId) {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FavoriteAddService service = retrofit.create(FavoriteAddService.class);
        Call<FavoriteAddModel> call = service.addFavorite("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""), itemId);
        call.enqueue(new retrofit2.Callback<FavoriteAddModel>(){

            @Override
            public void onResponse(Call<FavoriteAddModel> call,
                                   Response<FavoriteAddModel> response) {
                FavoriteAddModel model = response.body();
            }

            @Override
            public void onFailure(Call<FavoriteAddModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRemoveFavorite(int position, int itemId) {
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
                FavoriteRemoveModel model = response.body();
            }

            @Override
            public void onFailure(Call<FavoriteRemoveModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onAddOrder(final FishModel item) {
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

                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    // Transaction failed and was automatically canceled.
                    error.printStackTrace();
                }
            });
            try{
                ((FishFragmentListener) activity).onFishOrderClicked();
            }catch (ClassCastException cce){

            }
            Toast.makeText(getActivity(), "Item added in cart", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
