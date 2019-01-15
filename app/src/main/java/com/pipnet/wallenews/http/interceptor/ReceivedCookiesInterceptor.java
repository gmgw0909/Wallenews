package com.pipnet.wallenews.http.interceptor;

import android.content.SharedPreferences;

import com.pipnet.wallenews.App;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by LeeBoo on 2019/1/15.
 */

public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            SharedPreferences.Editor config = App.getInstance().getSharedPreferences("cookie_config", App.getInstance().MODE_PRIVATE)
                    .edit();
            config.putStringSet("cookie", cookies);
            config.commit();
        }
        return originalResponse;
    }
}