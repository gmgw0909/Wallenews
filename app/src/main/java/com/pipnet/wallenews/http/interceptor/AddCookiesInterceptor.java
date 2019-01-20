package com.pipnet.wallenews.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.util.SPUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LeeBoo on 2019/1/15.
 */

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> header = new HashSet<>();
        HashSet<String> preferences = (HashSet<String>) SPUtils.getStringSet("cookie",null);
        if (preferences != null) {
            header.addAll(preferences);
        }
        LoginInfo info = SPUtils.getObject(LoginInfo.class);
        if (info != null) {
            if (!TextUtils.isEmpty(info.uid)) {
                header.add("uid=" + info.uid);
            }
            if (!TextUtils.isEmpty(info.rememberMe)) {
                header.add("rememberMe" + info.rememberMe);
            }
        }
        if (header != null) {
            for (String cookie : header) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}