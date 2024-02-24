package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.activity.BaseAppCompatActivity;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.RegisterService;
import tz.co.fishhappy.app.model.LoginResponseModel;
import tz.co.fishhappy.app.model.RegisterModel;
import tz.co.fishhappy.app.model.RegisterResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class RegisterActivity extends BaseAppCompatActivity {

    @BindView(R.id.etRegisterEmail) EditText mEtEmail;
    @BindView(R.id.etRegisterFullname) EditText mEtFullname;
    @BindView(R.id.etRegisterPassword) EditText mEtPassword;
    @BindView(R.id.etRegisterPhone) EditText mEtPhone;
    @BindView(R.id.bnRegister) Button mBnRegister;
    @BindView(R.id.cbAgreeToTerms) CheckBox mCbTerms;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register");
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        //Init progress dialog
        mProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.bnRegister) void onRegister(){
        enableCotrols(false);
        if(verifyInputs()){
            enableCotrols(true);
            Utils.showToast(this, "Please complete the form");
            return;
        }
        if(verifyAgreeToTerms()){
            enableCotrols(true);
            Utils.showToast(this, "You have to agree to our terms of service to continue");
            return;
        }
        Utils.showProgressDialog(mProgressDialog, getString(R.string.register),
                getString(R.string.loading));


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RegisterService service = retrofit.create(RegisterService.class);
        Call call = service.register(new RegisterModel(mEtFullname.getText().toString(), mEtEmail.getText().toString(),
                mEtPhone.getText().toString(), mEtPassword.getText().toString()));

        call.enqueue(new retrofit2.Callback<RegisterResponseModel>(){

            @Override
            public void onResponse(Call<RegisterResponseModel> call, Response<RegisterResponseModel> response) {
                enableCotrols(true);
                mProgressDialog.dismiss();

                if(response.isSuccessful()){
                    RegisterResponseModel model = response.body();
                    mEditor.putString(getString(R.string.pref_login_token), model.getToken());
                    mEditor.commit();

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Email or Phone number already in use",
                            Toast.LENGTH_LONG).show();
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponseModel> call, Throwable t) {
                enableCotrols(true);
                mProgressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Ooops! something went wrong. " +
                        "Please try again later", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                return true;
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
        mEtPassword.setEnabled(value);
        mBnRegister.setEnabled(value);
        mCbTerms.setEnabled(value);
    }

    private boolean verifyAgreeToTerms(){
        return mCbTerms.isChecked() ? false: true;
    }

    private boolean verifyInputs(){
        if(mEtEmail.getText().toString().trim().length() == 0 ||
                mEtFullname.getText().toString().trim().length() == 0 ||
                mEtPhone.getText().toString().trim().length() == 0 ||
                mEtPassword.getText().toString().trim().length() == 0){
            return true;
        } else {
            return false;
        }
    }
}
