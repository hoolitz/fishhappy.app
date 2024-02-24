package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.RegisterService;
import tz.co.fishhappy.app.endpoint.UserAccountService;
import tz.co.fishhappy.app.model.RegisterModel;
import tz.co.fishhappy.app.model.RegisterResponseModel;
import tz.co.fishhappy.app.model.UserAccountModel;
import tz.co.fishhappy.app.model.UserAccountResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class UserAccountActivity extends BaseAppCompatActivity {

    @BindView(R.id.etAccountEmail) EditText mEtEmail;
    @BindView(R.id.etAccountFullname) EditText mEtFullname;
    @BindView(R.id.etAccountPhone) EditText mEtPhone;
    @BindView(R.id.bnAccountSave) Button mBnSave;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.my_account));
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        if(mPref.contains("USER-PHONE"))
            mEtPhone.setText(mPref.getString("USER-PHONE", ""));
        if(mPref.contains("USER-EMAIL"))
            mEtEmail.setText(mPref.getString("USER-EMAIL", ""));
        if(mPref.contains("USER-NAME"))
            mEtFullname.setText(mPref.getString("USER-NAME", ""));

        fetchUserProfile();

        //Init progress dialog
        mProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.bnAccountSave) void onSave(){
        enableCotrols(false);
        if(verifyInputs()){
            enableCotrols(true);
            Utils.showToast(this, getString(R.string.complete_form));
            return;
        }
        Utils.showProgressDialog(mProgressDialog, getString(R.string.my_account),
                getString(R.string.updating));


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserAccountService service = retrofit.create(UserAccountService.class);
        UserAccountModel accountModel = new UserAccountModel();
        accountModel.setName(mEtFullname.getText().toString());
        accountModel.setEmail(mEtEmail.getText().toString());
        accountModel.setPhone(mEtPhone.getText().toString());
        Call call = service.updateUserAccount("Bearer " +
                mPref.getString(getString(R.string.pref_login_token),""),
                accountModel);

        call.enqueue(new retrofit2.Callback<UserAccountResponseModel>(){

            @Override
            public void onResponse(Call<UserAccountResponseModel> call,
                                   Response<UserAccountResponseModel> response) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);
                if(response.isSuccessful()){
                    Toast.makeText(UserAccountActivity.this, "Account updated successful",
                            Toast.LENGTH_SHORT).show();
                    mEditor.putString("USER-EMAIL", mEtEmail.getText().toString());
                    mEditor.putString("USER-PHONE", mEtPhone.getText().toString());
                    mEditor.putString("USER-NAME", mEtFullname.getText().toString());
                    mEditor.commit();
                } else {
                    Toast.makeText(UserAccountActivity.this,
                            "Oops something not right, Please make sure fields are not empty",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserAccountResponseModel> call, Throwable t) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);
                Toast.makeText(UserAccountActivity.this,
                        "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserProfile(){
        Utils.showProgressDialog(mProgressDialog, getString(R.string.my_account),
                "Loading account...");


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserAccountService service = retrofit.create(UserAccountService.class);

        Call call = service.getUserAccount("Bearer " +
                        mPref.getString(getString(R.string.pref_login_token),""));

        call.enqueue(new retrofit2.Callback<UserAccountModel>(){

            @Override
            public void onResponse(Call<UserAccountModel> call,
                                   Response<UserAccountModel> response) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);
                if(response.isSuccessful()){
                    mEditor.putString("USER-EMAIL", response.body().getEmail());
                    mEditor.putString("USER-PHONE", response.body().getPhone());
                    mEditor.putString("USER-NAME", response.body().getName());
                    mEditor.commit();

                    mEtFullname.setText(response.body().getName());
                    mEtEmail.setText(response.body().getEmail());
                    mEtPhone.setText(response.body().getPhone());
                } else {
                    Toast.makeText(UserAccountActivity.this,
                            "Oops something not right, We could not get user profile",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserAccountModel> call, Throwable t) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);
                Toast.makeText(UserAccountActivity.this,
                        "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserAccountActivity.this, SettingActivity.class));
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

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableCotrols(boolean value){
        mEtEmail.setEnabled(value);
        mEtFullname.setEnabled(value);
        mEtPhone.setEnabled(value);
        mBnSave.setEnabled(value);
    }

    private boolean verifyInputs(){
        if(mEtEmail.getText().toString().trim().length() == 0 ||
                mEtFullname.getText().toString().trim().length() == 0 ||
                mEtPhone.getText().toString().trim().length() == 0){
            return true;
        } else {
            return false;
        }
    }
}
