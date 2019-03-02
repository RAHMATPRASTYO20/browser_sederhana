package com.example.browser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {
    //deklarasi
    private WebView webView;
    private EditText etUrl;
    private Button btnCari;
    ProgressBar pg;
    //iklan
    private AdView madView;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inisialisasi
        webView = (WebView) findViewById(R.id.webV);
        etUrl = (EditText) findViewById(R.id.etUrl);
        btnCari = (Button) findViewById(R.id.btnCari);

        //inisialisasi iklan
        madView = (AdView) findViewById(R.id.adView);
        madView.loadAd(new AdRequest.Builder().build());

        //menyiapkan iklan untuk interstitial ad
        interstitial = new InterstitialAd(MainActivity.this);

        //masukan id iklan
        interstitial.setAdUnitId(getString(R.string.admob_interensial_id));
        AdRequest adRequest = new AdRequest.Builder().build();

        //muat iklan interensial
        interstitial.loadAd(adRequest);
        //persiapkan iklan interensial
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                //memanggil display
                displayInterstitial();
            }
        });

        //url default
        String url = "https://www.google.co.id/";
        //inisialisasi kebutuhan browser
        webView.getSettings().setJavaScriptEnabled(true);//untuk mendukung javascrip
        webView.getSettings().setDisplayZoomControls(true);//mengontrol zoom pada browser
        webView.getSettings().setLoadWithOverviewMode(true);//otomatis load zoom
        webView.getSettings().setUseWideViewPort(true);//memberi tau browser untuk mengaktivkan wide view port
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//otomatis menampilkan javascrip window
        //untuk support file url di browser
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//untuk scrollbar
        //inisialisai progresbar
        pg = (ProgressBar) findViewById(R.id.progressBar);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pg.setVisibility(View.VISIBLE);
                pg.setProgress(newProgress);
                if (newProgress == 100) {
                    pg.setVisibility(View.GONE);
                }
            }
        });

        //untuk ngeload url yang pertama kita inisialisasikan
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebLaunch());

        btnCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etUrl.getText().toString();
                //aktivkan javascrip
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setDisplayZoomControls(true);
                pg = (ProgressBar) findViewById(R.id.progressBar);
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        pg.setVisibility(View.VISIBLE);
                        pg.setProgress(newProgress);
                        if (newProgress == 100) {
                            pg.setVisibility(View.GONE);
                        }
                    }
                });

                webView.loadUrl(url);
                webView.setWebViewClient(new MyWebLaunch());
            }
        });
    }

    private class MyWebLaunch extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
