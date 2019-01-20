package com.pipnet.wallenews.http.interceptor;

import com.pipnet.wallenews.util.SPUtils;

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
            SPUtils.setStringSet("cookie", cookies);
        }
        return originalResponse;
    }
}