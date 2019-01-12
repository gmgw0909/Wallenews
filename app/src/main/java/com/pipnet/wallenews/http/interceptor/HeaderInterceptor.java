package com.pipnet.wallenews.http.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LeeBoo on 2018/12/2.
 * 设置请求头拦截器
 */

public class HeaderInterceptor implements Interceptor {

    private static HeaderInterceptor instance;

    public static HeaderInterceptor getInstance() {
        if (instance == null) {
            instance = new HeaderInterceptor();
        }
        return instance;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}
