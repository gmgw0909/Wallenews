package com.pipnet.wallenews.http.interceptor;

import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.http.Router;
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
        if (originalResponse.request().url().toString().contains(Router.BASE_URL + "register/message/notLogin")) {
            if (originalResponse.request().url().toString().split("JSESSIONID=").length == 2) {
                LoginInfo info = SPUtils.getObject(LoginInfo.class);
                info.uid = originalResponse.request().url().toString().split("JSESSIONID=")[1];
                SPUtils.setObject(info);
            }
        }
        return originalResponse;
    }
}