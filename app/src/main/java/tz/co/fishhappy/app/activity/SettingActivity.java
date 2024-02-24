package tz.co.fishhappy.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.adapter.SettingAdapter;
import tz.co.fishhappy.app.model.SettingsModel;

/**
 * Created by Simon on 30-Apr-17.
 */

public class SettingActivity extends BaseAppCompatActivity implements SettingAdapter.SettingListener {

    @BindView(R.id.rvListSettings) RecyclerView mRvSettings;

    private SettingAdapter mSettingAdapter;
    private List<SettingsModel> mList;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Settings");
        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvSettings.setLayoutManager(mLayoutManager);

        populateSettingList();

        mSettingAdapter = new SettingAdapter(this, mList, this);
        mRvSettings.setAdapter(mSettingAdapter);

    }

    private void populateSettingList(){
        SettingsModel model;
        mList = new ArrayList<>();

        model = new SettingsModel("My Account", R.drawable.ic_action_user);
        mList.add(model);

        model = new SettingsModel("Change Password", R.drawable.ic_action_password);
        mList.add(model);

        model = new SettingsModel("Notification", R.drawable.ic_action_notification);
        mList.add(model);

        model = new SettingsModel("Privacy Policy", R.drawable.ic_action_privacy);
        mList.add(model);

        model = new SettingsModel("Terms of Service", R.drawable.ic_action_terms);
        mList.add(model);

        model = new SettingsModel("Frequently Asked Questions(FAQ)", R.drawable.ic_action_faq);
        mList.add(model);

        model = new SettingsModel("About", R.drawable.ic_action_about);
        mList.add(model);

        model = new SettingsModel("Logout", R.drawable.ic_action_logout);
        mList.add(model);
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

    @Override
    public void onLogout() {
        SharedPreferences pref = getSharedPreferences(
                getString(R.string.pref_main), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.package.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);

        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
