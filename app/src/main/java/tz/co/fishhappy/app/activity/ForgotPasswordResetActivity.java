package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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
import tz.co.fishhappy.app.endpoint.ForgotPasswordResetService;
import tz.co.fishhappy.app.endpoint.ForgotPasswordService;
import tz.co.fishhappy.app.model.ForgotPasswordModel;
import tz.co.fishhappy.app.model.ForgotPasswordResetModel;
import tz.co.fishhappy.app.model.ForgotPasswordResetResponseModel;
import tz.co.fishhappy.app.model.ForgotPasswordResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class ForgotPasswordResetActivity extends BaseAppCompatActivity {

    @BindView(R.id.etForgotPasswordResetCode) EditText mEtCode;
    @BindView(R.id.etForgotPasswordResetNew) EditText mEtPassword;
    @BindView(R.id.etForgotPasswordResetRetype) EditText mEtPasswordRetype;
    @BindView(R.id.bnForgotPasswordChange) Button mBnForgotPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_reset);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Forgot Password");
        ButterKnife.bind(this);

        //Init progress dialog
        mProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.bnForgotPasswordChange) void onPasswordChange(){
        enableControls(false);
        if(verifyInputs()){
            enableControls(true);
            Utils.showToast(this, "Fill empty fields");
            return;
        }
        if(verifyPassword()){
            enableControls(true);
            Utils.showToast(this, "Password didn't match");
            return;
        }

        Utils.showProgressDialog(mProgressDialog, getString(R.string.forgot_password),
                getString(R.string.loading));


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ForgotPasswordResetService service = retrofit.create(ForgotPasswordResetService.class);
        Call call = service.forgotResetPassword(
                new ForgotPasswordResetModel(mEtCode.getText().toString(),
                        mEtPassword.getText().toString()));

        call.enqueue(new retrofit2.Callback<ForgotPasswordResetResponseModel>(){

            @Override
            public void onResponse(Call<ForgotPasswordResetResponseModel> call,
                                   Response<ForgotPasswordResetResponseModel> response) {
                enableControls(true);
                Utils.hideProgressDialog(mProgressDialog);

                //TODO save token or something else after register
            }

            @Override
            public void onFailure(Call<ForgotPasswordResetResponseModel> call, Throwable t) {
                enableControls(true);
                Utils.hideProgressDialog(mProgressDialog);

                //TODO show message of what went wrong
            }
        });

        startActivity(new Intent(ForgotPasswordResetActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableControls(boolean value){
        mBnForgotPassword.setEnabled(value);
    }

    private boolean verifyInputs(){
        if(mEtCode.getText().toString().trim().length() == 0 ||
                mEtPassword.getText().toString().trim().length() == 0 ||
                mEtPasswordRetype.getText().toString().trim().length() == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verify password match
     * */
    private boolean verifyPassword(){
        if(!mEtPassword.getText().toString().trim().equalsIgnoreCase(
                mEtPasswordRetype.getText().toString().trim()
        )){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPasswordResetActivity.this, ForgotPasswordActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ForgotPasswordResetActivity.this, ForgotPasswordActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
