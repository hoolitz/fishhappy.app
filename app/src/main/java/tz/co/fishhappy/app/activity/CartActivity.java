package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.adapter.CartAdapter;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.endpoint.CartService;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.RegisterService;
import tz.co.fishhappy.app.model.CartModel;
import tz.co.fishhappy.app.model.CartProductRequestModel;
import tz.co.fishhappy.app.model.CartRequestModel;
import tz.co.fishhappy.app.model.CartResponseModel;
import tz.co.fishhappy.app.model.RegisterModel;
import tz.co.fishhappy.app.model.RegisterResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 04-May-17.
 */

public class CartActivity extends BaseAppCompatActivity implements CartAdapter.CartListener{

    @BindView(R.id.rvListCart) RecyclerView mRvCart;
    @BindView(R.id.tvCartTotal) TextView mTvTotal;
    @BindView(R.id.bnCartSendOrder) Button mBnSendOrder;

    private ProgressDialog mProgressDialog;
    private CartAdapter mCartAdapter;
    private RealmResults<CartRealmObject> mList;
    private RecyclerView.LayoutManager mLayoutManager;
    private Realm mRealm;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Cart");
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main),MODE_PRIVATE);
        mEditor = mPref.edit();

        //initialize and get instance of realm
        mRealm = Realm.getDefaultInstance();

        mProgressDialog = new ProgressDialog(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvCart.setLayoutManager(mLayoutManager);

        getCartList();

    }

    @OnClick(R.id.bnCartSendOrder) void onSendOrder(){
        startActivity(new Intent(this, ShippingDetailActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCart(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.delete(CartRealmObject.class);
            }
        });
    }

    private void getCartList(){
        RealmResults<CartRealmObject> result = mRealm.where(CartRealmObject.class)
                .findAllAsync();

        result.addChangeListener(new RealmChangeListener<RealmResults<CartRealmObject>>() {
            @Override
            public void onChange(RealmResults<CartRealmObject> element) {
                mCartAdapter = new CartAdapter(CartActivity.this, element, CartActivity.this);
                mRvCart.swapAdapter(mCartAdapter, false);
                quantityChange();
            }
        });
    }

    private List<CartProductRequestModel> getOrderItems(){
        RealmResults<CartRealmObject> result = mRealm.where(CartRealmObject.class)
                .findAll();

        List<CartProductRequestModel> list = new ArrayList<>();
        CartProductRequestModel model;

        for (int i = 0; i < result.size(); i++){
            model = new CartProductRequestModel();
            model.setId(result.get(i).getId());
            model.setQuantity(result.get(i).getQuantity());
            list.add(model);
        }

        return list;
    }

    @Override
    public void quantityChange() {
        RealmResults<CartRealmObject> result = mRealm.where(CartRealmObject.class)
                .findAllAsync();

        result.addChangeListener(new RealmChangeListener<RealmResults<CartRealmObject>>() {
            @Override
            public void onChange(RealmResults<CartRealmObject> element) {
                int total = 0;
                for(int i = 0; i < element.size(); i++){
                    CartRealmObject model = element.get(i);
                    total += model.getPrice() * model.getQuantity();
                }
                mTvTotal.setText(Utils.formatToMoney(String.valueOf(total)) + " Tshs");
            }
        });
    }

    @Override
    public void removeItem(int position, int itemId) {
        final CartRealmObject result = mRealm.where(CartRealmObject.class)
                .equalTo("id", itemId)
                .findFirst();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.deleteFromRealm();
            }
        });
        mCartAdapter.removeItem(position);
    }

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableCotrols(boolean value){
        mBnSendOrder.setEnabled(false);
    }
}
