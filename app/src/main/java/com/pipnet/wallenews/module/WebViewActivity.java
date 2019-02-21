package com.pipnet.wallenews.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.pipnet.wallenews.App;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.http.Router;
import com.pipnet.wallenews.util.SPUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import retrofit2.http.Url;

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
        synCookies(this, url);
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

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
//      cookieManager.removeSessionCookie();// 移除
        cookieManager.removeAllCookie();
        List<Cookie> cookies = new SharedPrefsCookiePersistor(App.getInstance()).loadAll();
        cookieManager.setCookie(url,cookies.toString().replace("[","").replace("]",""));
//        cookieManager.setCookie(url,"uid="+SPUtils.getObject(LoginInfo.class).uid);
//        StringBuffer sb = new StringBuffer();
//        for (Cookie cookie : cookies) {
//
//            String cookieName = cookie.name();
//            String cookieValue = cookie.value();
//            if (!TextUtils.isEmpty(cookieName)
//                    && !TextUtils.isEmpty(cookieValue)) {
//                sb.append(cookieName + "=");
//                sb.append(cookieValue + ";");
//            }
//        }
//        String[] cookie = sb.toString().split(";");
//        for (int i = 0; i < cookie.length; i++) {
//            Log.d("Ok_cookie[i]", cookie[i]);
//            cookieManager.setCookie(url, cookie[i]);// cookies是在HttpClient中获得的cookie
//        }
        CookieSyncManager.getInstance().sync();
    }
}
