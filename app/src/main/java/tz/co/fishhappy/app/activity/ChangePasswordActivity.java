package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.widget.Button;
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
import tz.co.fishhappy.app.endpoint.ChangePasswordService;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.model.ChangePasswordModel;
import tz.co.fishhappy.app.model.ChangePasswordResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon on 30-Apr-17.
 */

public class ChangePasswordActivity extends BaseAppCompatActivity {

    @BindView(R.id.etAccountOldPassword) EditText mEtOldPassword;
    @BindView(R.id.etAccountNewPassword) EditText mEtNewPassword;
    @BindView(R.id.bnAccountChangePassword) Button mBnChangePassword;

    private ProgressDialog mProgressDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Change Password");
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        mProgressDialog = new ProgressDialog(this);
    }

    @OnClick(R.id.bnAccountChangePassword) void onPasswordChange(){
        enableControls(false);
        if(verifyInputs()){
            enableControls(true);
            Utils.showToast(this, "Fill empty fields");
            return;
        }

        Utils.showProgressDialog(mProgressDialog, "",
                getString(R.string.loading));


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ChangePasswordService service = retrofit.create(ChangePasswordService.class);
        Call call = service.changePassword("Bearer " +
                        mPref.getString(getString(R.string.pref_login_token),""),
                new ChangePasswordModel(mEtOldPassword.getText().toString(),
                        mEtNewPassword.getText().toString()));

        call.enqueue(new retrofit2.Callback<ChangePasswordResponseModel>(){

            @Override
            public void onResponse(Call<ChangePasswordResponseModel> call,
                                   Response<ChangePasswordResponseModel> response) {
                enableControls(true);
                Utils.hideProgressDialog(mProgressDialog);
                if(response.isSuccessful()){
                    Toast.makeText(ChangePasswordActivity.this, "Password changed successful",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Fail to change password",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponseModel> call, Throwable t) {
                enableControls(true);
                Utils.hideProgressDialog(mProgressDialog);
                Toast.makeText(ChangePasswordActivity.this,
                        "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableControls(boolean value){
        mBnChangePassword.setEnabled(value);
    }

    private boolean verifyInputs(){
        if(mEtOldPassword.getText().toString().trim().length() == 0 ||
                mEtNewPassword.getText().toString().trim().length() == 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChangePasswordActivity.this, SettingActivity.class));
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
}
