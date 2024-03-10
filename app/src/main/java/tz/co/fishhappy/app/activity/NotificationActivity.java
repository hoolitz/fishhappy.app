package tz.co.fishhappy.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import tz.co.fishhappy.app.R;

/**
 * Created by Simon on 03-May-17.
 */

public class NotificationActivity extends BaseAppCompatActivity {

    @BindView(R.id.swNotification) Switch mSwNotification;
    @BindView(R.id.swNotificationVibration) Switch mSwVibration;
    @BindView(R.id.tvNotificationSound) TextView mTvSound;
    @BindView(R.id.llNotificationSound) LinearLayout mLlNotificationSound;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private final int RINGTONE_REQUEST_CODE = 333;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setTitle(getString(R.string.notification));

        //Init shared preference and editor
        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        //Get current notification sound
        Uri currentSoundUri = RingtoneManager.getActualDefaultRingtoneUri(
                getApplicationContext(), RingtoneManager.TYPE_NOTIFICATION);
        //Get the title of the ringtone;
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), currentSoundUri);

        if(mPref.contains(getString(R.string.pref_notification))){
            mSwNotification.setChecked(mPref.getBoolean(
                    getString(R.string.pref_notification), true));
            mSwVibration.setChecked(mPref.getBoolean(
                    getString(R.string.pref_notification_vibration), true));
            mTvSound.setText(mPref.getString(
                    getString(R.string.pref_notification_sound_title), ringtone.getTitle(this)));
        } else {
            //Set defaults
            mSwNotification.setChecked(true);
            mSwVibration.setChecked(true);
            mTvSound.setText(ringtone.getTitle(this));

            mEditor.putBoolean(getString(R.string.pref_notification), true);
            mEditor.putBoolean(getString(R.string.pref_notification_vibration), true);
            mEditor.putString(getString(R.string.pref_notification_sound_path),
                    currentSoundUri.getPath());
            mEditor.putString(getString(R.string.pref_notification_sound_title),
                    ringtone.getTitle(this));
            mEditor.commit();
        }
    }

    @OnClick(R.id.llNotificationSound) void onNotificationSound(){
        Intent audioIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        startActivityForResult(audioIntent, RINGTONE_REQUEST_CODE);
    }

    @OnCheckedChanged(R.id.swNotificationVibration) void onVibration(){
        if(mSwVibration.isChecked()){
            mEditor.putBoolean(getString(R.string.pref_notification_vibration), true);
        } else {
            mEditor.putBoolean(getString(R.string.pref_notification_vibration), false);
        }
        mEditor.commit();
    }

    @OnCheckedChanged(R.id.swNotification) void onNotification(){
        if(mSwNotification.isChecked()){
            mEditor.putBoolean(getString(R.string.pref_notification), true);
        } else {
            mEditor.putBoolean(getString(R.string.pref_notification), false);
        }
        mEditor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RINGTONE_REQUEST_CODE && resultCode == RESULT_OK){
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone ringTone = RingtoneManager.getRingtone(getApplicationContext(), uri);

            //Set sound name
            mTvSound.setText(ringTone.getTitle(this));

            //Save the sound changes
            mEditor.putString(getString(R.string.pref_notification_sound_path), uri.getPath());
            mEditor.putString(getString(R.string.pref_notification_sound_title),
                    ringTone.getTitle(this));
            mEditor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotificationActivity.this, SettingActivity.class));
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
