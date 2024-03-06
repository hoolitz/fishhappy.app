package tz.co.fishhappy.app.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.data.CartRealmObject;
import tz.co.fishhappy.app.endpoint.CartService;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.model.CartProductRequestModel;
import tz.co.fishhappy.app.model.CartRequestModel;
import tz.co.fishhappy.app.model.CartResponseModel;
import tz.co.fishhappy.app.utility.Utils;

/**
 * @author Simon Mawole
 * @version 0.1.0
 * @email simonmawole2011@gmail.com
 * @since 0.1.0
 */
public class ShippingDetailActivity extends BaseAppCompatActivity {

    @BindView(R.id.etLocationName)
    EditText etLocationName;
    @BindView(R.id.bnCheckout)
    Button bnCheckout;
    @BindView(R.id.etFirstname) EditText etFirstName;
    @BindView(R.id.etLastname) EditText etLastName;
    @BindView(R.id.etPhoneOne) EditText etPhoneOne;
    @BindView(R.id.etPhoneTwo) EditText etPhoneTwo;
    private LocationCallback mLocationCallback;

    private ProgressDialog mProgressDialog;
    private RealmResults<CartRealmObject> mList;
    private Realm mRealm;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Shipping");
        ButterKnife.bind(this);

        mPref = getSharedPreferences(getString(R.string.pref_main),MODE_PRIVATE);
        mEditor = mPref.edit();

        mProgressDialog = new ProgressDialog(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        /*mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            etLocationName.setText(location.getLatitude()  +  ", " + location.getLongitude());
                        }
                    }
                });
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        etLocationName.setText(location.getLatitude()  +  ", " + location.getLongitude());
                    }
                }
            };
        };*/

        //initialize and get instance of realm
        mRealm = Realm.getDefaultInstance();

    }

    @OnClick(R.id.bnCheckout) void onCheckout(View v){
        enableCotrols(false);

        Utils.showProgressDialog(mProgressDialog, "Cart",
                "Sending order...");


        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CartService service = retrofit.create(CartService.class);
        Call call = service.sendOrderItems("Bearer " +
                        mPref.getString(getString(R.string.pref_login_token),""),
                new CartRequestModel(getOrderItems()));

        call.enqueue(new retrofit2.Callback<CartResponseModel>(){

            @Override
            public void onResponse(Call<CartResponseModel> call, Response<CartResponseModel> response) {
                enableCotrols(true);
                mProgressDialog.dismiss();

                if(response.isSuccessful()){
                    CartResponseModel model = response.body();

                    clearCart();
                    Toast.makeText(ShippingDetailActivity.this, "Your order is sent",
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ShippingDetailActivity.this, PaymentActivity.class)
                            .putExtra("orderId", model.getId()));
                    finish();
                } else {
                    Toast.makeText(ShippingDetailActivity.this, "Fail to send order",
                            Toast.LENGTH_LONG).show();
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CartResponseModel> call, Throwable t) {
                enableCotrols(true);
                mProgressDialog.dismiss();
                Toast.makeText(ShippingDetailActivity.this, "Ooops! something went wrong. " +
                        "Please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            etLocationName.setText(mLastLocation.getLatitude()  +  ", " + mLastLocation.getLongitude());
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            Toast.makeText(ShippingDetailActivity.this,
                                    "getLastLocation:exception", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(ShippingDetailActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) shouldProvideRationale = true;
        else shouldProvideRationale = false;

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            Toast.makeText(ShippingDetailActivity.this,
                    "Displaying permission rationale to provide additional context.",
                    Toast.LENGTH_SHORT).show();
            startLocationPermissionRequest();

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                Toast.makeText(ShippingDetailActivity.this, "Permission denied",
                        Toast.LENGTH_SHORT).show();
                /*showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });*/
            }
        }
    }

    private void clearCart(){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.delete(CartRealmObject.class);
            }
        });
    }

    private List<CartProductRequestModel> getOrderItems(){
        RealmResults<CartRealmObject> result = mRealm.where(CartRealmObject.class)
                .findAll();

        List<CartProductRequestModel> list = new ArrayList<>();
        CartProductRequestModel model;

        for (int i = 0; i < result.size(); i++){
            model = new CartProductRequestModel();
            model.setId(result.get(i).getId());
            model.setQuantity(result.get(i).getQuantity());
            list.add(model);
        }

        return list;
    }

    /**
     * Enable or disable controls
     *
     * @param value - true to enable, false to disable
     * */
    private void enableCotrols(boolean value){
        bnCheckout.setEnabled(false);
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
}
