package com.pipnet.wallenews.module;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web)
    WebView webView;
    @BindView(R.id.title)
    TextView tv_title;

    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";

    @Override
    public int setContentView() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initViewData() {
        initWebSettings();
        initWebClient();
        String title = getIntent().getStringExtra(KEY_TITLE);
        tv_title.setText(title);
        String url = getIntent().getStringExtra(KEY_URL);
        webView.loadUrl(url);
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    protected void initWebClient() {
        webView.setWebViewClient(getWebViewClient());
    }

    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        };
    }

    @OnClick(R.id.btn_left)
    public void onViewClicked() {
        finish();
    }
}
