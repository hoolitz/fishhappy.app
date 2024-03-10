package tz.co.fishhappy.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tz.co.fishhappy.app.R;
import tz.co.fishhappy.app.endpoint.EndPointsUrls;
import tz.co.fishhappy.app.endpoint.GetPayService;
import tz.co.fishhappy.app.endpoint.PaymentService;
import tz.co.fishhappy.app.utility.Utils;

/**
 * Created by Simon Mawole
 * Email: simonmawole2011@gmail.com
 * Date: 05-Sep-17.
 */

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private final OkHttpClient mClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    final Retrofit mRetrofit = new Retrofit.Builder()
            .client(mClient)
            .baseUrl("http://fishhappy.co.tz/")
            //.addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Payment");

        mPref = getSharedPreferences(getString(R.string.pref_main), MODE_PRIVATE);
        mEditor = mPref.edit();

        //Init progress dialog
        mProgressDialog = new ProgressDialog(this);
        //Init toast

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new WebInterface(this), "Android");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        getPaymentUrl();

        /*Integer orderId = getIntent().getIntExtra("orderId", 0);
        if(orderId != 0) {
            getPayment(orderId);
        } else {
            Toast.makeText(this, "Fail to get payment form", Toast.LENGTH_SHORT).show();
            finish();
        }*/
    }

    public void getPayment(Integer orderId){
        if(orderId == 0){
            return;
        }
        Utils.showProgressDialog(mProgressDialog, getString(R.string.login),
                getString(R.string.loading));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle("Payment");
        mProgressDialog.setMessage("Loading payment form");
        mProgressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EndPointsUrls.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        PaymentService service = retrofit.create(PaymentService.class);

        Call call = service.getPayment("Bearer " +
                        mPref.getString(getString(R.string.pref_login_token),""), orderId);

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                if(response.isSuccessful()){
                    String replaceStart = response.body().replace("<iframe src=\"", "");
                    String replaceEnd = replaceStart.replace("\" width=\"100%\" height=\"400px\" scrolling=\"auto\" frameBorder=\"0\"> <p>Unable to load the payment page</p> </iframe>","");
                    mWebView.loadData(response.body(), "text/html", "utf-8");
                } else {
                    Toast.makeText(PaymentActivity.this, "Fail to get payment form",
                            Toast.LENGTH_LONG).show();
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mProgressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                mProgressDialog.dismiss();
                Toast.makeText(PaymentActivity.this, "Ooops! something went wrong. " +
                        "Please try again later", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPaymentUrl(){
        if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
        mProgressDialog.setTitle("Payment");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();

        GetPayService service = mRetrofit.create(GetPayService.class);
        Call<String> call = service.getPaymentUrl("tokenHere", "1000");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call,
                                   Response<String> response) {
                if(response.isSuccessful()){
                    String url = response.body();
                    mWebView.loadUrl(url);
                } else {
                    mProgressDialog.dismiss();
                        Toast.makeText(PaymentActivity.this,
                                "Unknown response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(PaymentActivity.this,
                        "Something went wrong", Toast.LENGTH_SHORT).show();
                //t.printStackTrace();
            }
        });
    }

    class WebInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void paymentIsComplete(){
            setTitle("Complete");
        }
    }
}
