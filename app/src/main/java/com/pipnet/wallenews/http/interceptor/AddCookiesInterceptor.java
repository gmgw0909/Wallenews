package com.pipnet.wallenews.http.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.pipnet.wallenews.App;
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
        HashSet<String> preferences = (HashSet) App.getInstance().getSharedPreferences("cookie_config",
                App.getInstance().MODE_PRIVATE).getStringSet("cookie", null);
        LoginInfo info = SPUtils.getObject(LoginInfo.class);
        if (info != null) {
            if (!TextUtils.isEmpty(info.uid)) {
                preferences.add("uid=" + info.uid);
            }
            if (!TextUtils.isEmpty(info.rememberMe)) {
                preferences.add("rememberMe" + info.rememberMe);
            }
        }
        if (preferences != null) {
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie);
            }
        }
        return chain.proceed(builder.build());
    }
}