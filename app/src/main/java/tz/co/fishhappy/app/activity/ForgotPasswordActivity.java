package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import tz.co.fishhappy.app.endpoint.ForgotPasswordService;
import tz.co.fishhappy.app.model.ForgotPasswordModel;
import tz.co.fishhappy.app.model.ForgotPasswordResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class ForgotPasswordActivity extends BaseAppCompatActivity {

    @BindView(R.id.etForgotPasswordUsername) EditText mEtUsername;
    @BindView(R.id.bnForgotPassword) Button mBnForgotPassword;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Forgot Password");
        ButterKnife.bind(this);

        //Init Progress dialog
        mProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.bnForgotPassword) void onResetPassword(){
        enableCotrols(false);
        if(verifyInputs()){
            enableCotrols(true);
            Utils.showToast(this, "Empty username");
            return;
        }
        Utils.showProgressDialog(mProgressDialog, getString(R.string.forgot_password),
                getString(R.string.loading));


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ForgotPasswordService service = retrofit.create(ForgotPasswordService.class);
        Call call = service.forgotPassword(new ForgotPasswordModel(mEtUsername.getText().toString()));

        call.enqueue(new retrofit2.Callback<ForgotPasswordResponseModel>(){

            @Override
            public void onResponse(Call<ForgotPasswordResponseModel> call,
                                   Response<ForgotPasswordResponseModel> response) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);

                //TODO save token or something else after register
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponseModel> call, Throwable t) {
                enableCotrols(true);
                Utils.hideProgressDialog(mProgressDialog);

                //TODO show message of what went wrong
            }
        });

        startActivity(new Intent(ForgotPasswordActivity.this, ForgotPasswordResetActivity.class));
        finish();
    }

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableCotrols(boolean value){
        mEtUsername.setEnabled(value);
        mBnForgotPassword.setEnabled(value);
    }

    private boolean verifyInputs(){
        if(mEtUsername.getText().toString().trim().length() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
