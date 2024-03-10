package tz.co.fishhappy.app.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import tz.co.fishhappy.app.R;

/**
 * Created by Simon on 30-Apr-17.
 */

public class WebViewActivity extends BaseAppCompatActivity {

    @BindView(R.id.webView) WebView mWebView;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUrl = getIntent().getStringExtra("url");
        setTitle(getIntent().getStringExtra("title"));

        if(mUrl != null && mUrl.length() != 0) {
            mWebView.loadUrl("http://google.com");//mUrl);

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(true);
//            webSettings.setAppCacheEnabled(true);

            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

            mWebView.setWebViewClient(new MyWebViewClient());
        } else {
            Toast.makeText(this, "Unknown or Empty url", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("http")){
                view.loadUrl(url);
            } /*else if(!url.startsWith("file:///android_asset/www/")){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }*/
            return true;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WebViewActivity.this, SettingActivity.class));
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
