package tz.co.fishhappy.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tz.co.fishhappy.app.R;

/**
 * Created by Simon on 27-Apr-17.
 * Updated by Kelvin on 26-Feb-23.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        if(mPref.contains(getString(R.string.pref_login_token))) {
            if(mPref.getString(getString(R.string.pref_login_token),"").equalsIgnoreCase("")){
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }

    }
}
