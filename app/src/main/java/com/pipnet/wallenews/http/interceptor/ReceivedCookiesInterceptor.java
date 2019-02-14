package com.pipnet.wallenews.http.interceptor;

import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.http.Router;
import com.pipnet.wallenews.util.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by LeeBoo on 2019/1/15.
 */

public class ReceivedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (chain.request().url().toString().contains(Router.BASE_URL + "myspace/me") && !originalResponse.headers("Cookie").isEmpty()) {
            LoginInfo info = SPUtils.getObject(LoginInfo.class);
            originalResponse.headers("Cookie");
            SPUtils.setObject(info);
        }
        return originalResponse;
    }
}