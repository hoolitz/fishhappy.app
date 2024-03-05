package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.LoginService;
import tz.co.fishhappy.app.model.LoginModel;
import tz.co.fishhappy.app.model.LoginResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 27-Apr-17.
 * Updated by Kelvin on 26-Feb-23.
 */

public class LoginActivity extends BaseAppCompatActivity {

    @BindView(R.id.etLoginPassword) EditText mEtPassword;
    @BindView(R.id.etLoginUsername) EditText mEtUsername;
    @BindView(R.id.bnLogin) Button mBnLogin;
    @BindView(R.id.tvLoginRegister) TextView mTvRegister;
    @BindView(R.id.tvLoginForgotPassword) TextView mTvForgotPassword;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        //Init progress dialog
        mProgressDialog = new ProgressDialog(this);
        //Init toast


    }

    @OnClick(R.id.bnLogin) void onLogin(){
        enableControls(false);
        if(verifyUsername()){
            enableControls(true);
            Utils.showToast(this, "Empty username");
            return;
        }
        if(verifyPassword()){
            enableControls(true);
            Utils.showToast(this, "Empty password");
            return;
        }
        Utils.showProgressDialog(mProgressDialog, getString(R.string.login),
                getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle(getString(R.string.login));
        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndPointsUrls.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        LoginService service = retrofit.create(LoginService.class);

        Call call = service.login(new LoginModel(mEtUsername.getText().toString(), mEtPassword.getText().toString()));

        call.enqueue(new retrofit2.Callback<   LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call,
                                       Response<LoginResponseModel> response) {
                    enableControls(true);
                    //Utils.hideProgressDialog(mProgressDialog);
                    mProgressDialog.dismiss();

                    if(response.isSuccessful()){
                        LoginResponseModel model = response.body();
                        mEditor.putString(getString(R.string.pref_login_token), model.getToken());
                        mEditor.commit();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Wrong username or password",
                                Toast.LENGTH_LONG).show();
                        try {
                            System.out.println(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    t.printStackTrace();
                    enableControls(true);
                    //Utils.hideProgressDialog(mProgressDialog);
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Ooops! something went wrong. " +
                            "Please try again later", Toast.LENGTH_LONG).show();
                }
            });
        mProgressDialog.hide();
    }

    @OnClick(R.id.tvLoginRegister) void onRegister(){
        enableControls(false);
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    @OnClick(R.id.tvLoginForgotPassword) void onForgotPassword(){
        enableControls(false);
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mProgressDialog != null){
            if(mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }
    }

    /**
     * Enable and Disable view
     *
     * @param value - true for enable, false to disable
     * */
    private void enableControls(boolean value){
        mBnLogin.setEnabled(value);
        mTvForgotPassword.setEnabled(value);
        mTvRegister.setEnabled(value);
        mEtUsername.setEnabled(value);
        mEtPassword.setEnabled(value);
    }

    /**
     * Clear password field if login attempt fail, like wrong username or password
     **/
    private void clearPasswordField(){
        mEtPassword.setText("");
    }

    /**
     * Verify username
     * */
    private boolean verifyUsername(){
        String user = mEtUsername.getText().toString().trim();
        return user.length() == 0 ? true : false;
    }

    /**
     * Verify password
     * */
    private boolean verifyPassword(){
        String pass = mEtPassword.getText().toString().trim();
        return pass.length() == 0 ? true : false;
    }

}
