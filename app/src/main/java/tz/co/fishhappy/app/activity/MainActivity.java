package tz.co.fishhappy.app.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.adapter.MainPagerAdapter;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.fragment.ListFavoriteFragment;
import tz.co.fishhappy.app.fragment.ListFishFragment;
import tz.co.fishhappy.app.fragment.ListOrderFragment;

public class MainActivity extends BaseAppCompatActivity implements
        ListFishFragment.FishFragmentListener, ListFavoriteFragment.FavoriteFragmentListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.vp_main) ViewPager mViewPager;
    @BindView(R.id.tabs_main) TabLayout mTabLayout;
    @BindView(R.id.bnViewCart) Button mBnViewCart;
    @BindView(R.id.cvCartItems) CardView mCvItems;
    @BindView(R.id.tvCartItemCount) TextView mTvItemCount;

    @BindString(R.string.tab_fish) String mTabFish;
    @BindString(R.string.tab_order) String mTabOrder;
    @BindString(R.string.tab_favorite) String mTabFavorite;

    private MainPagerAdapter mPagerAdapter;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle("");
        setSupportActionBar(mToolbar);

        //Set logout broadcast listener
        setLogoutBroadcastListener();

        //initialize and get instance of realm
        mRealm = Realm.getDefaultInstance();

        //mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        //mViewPager.setAdapter(mPagerAdapter);
        //mViewPager.setOffscreenPageLimit(3);
        setupViewPager(mViewPager);

        mTabLayout.setupWithViewPager(mViewPager);
        setTabs();

        //Check items in cart
        checkItemInCart();
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ListFishFragment(), "Fish");
        viewPagerAdapter.addFragment(new ListOrderFragment(), "Order");
        viewPagerAdapter.addFragment(new ListFavoriteFragment(), "Favorite");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(0);
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @OnClick(R.id.bnViewCart) void onViewCart(){
        startActivity(new Intent(this, CartActivity.class));
    }

    public void setTabs(){
        mTabLayout.getTabAt(0).setText(mTabFish);
        mTabLayout.getTabAt(1).setText(mTabOrder);
        mTabLayout.getTabAt(2).setText(mTabFavorite);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return true;
            case R.id.menu_action_share:
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                        .setType("text/plain")
                        .setText("This app can help you get that fish you " +
                                "like and deliver it to your doors. " +
                                "Download Fish Happy on Playstore " +
                                "https://play.google.com/store/apps/details?id=tz.co.fishhappy.app")
                        .getIntent(), "Share App With"));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkItemInCart() {

       RealmResults<CartRealmObject> result = mRealm.where(CartRealmObject.class).findAllAsync();

       Log.d("CART_REALM",result.asJSON());

        result.addChangeListener(new RealmChangeListener<RealmResults<CartRealmObject>>() {
            @Override
            public void onChange(RealmResults<CartRealmObject> element) {
                if(element.size() != 0) {
                    mTvItemCount.setText(String.valueOf(element.size()));
                    mCvItems.setVisibility(View.VISIBLE);
                } else {
                    mTvItemCount.setText("0");
                    mCvItems.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onFishOrderClicked() {
        checkItemInCart();
    }

    @Override
    public void onFavoriteOrderClicked() {
        checkItemInCart();
    }

    private void setLogoutBroadcastListener(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        }, intentFilter);
    }
}
