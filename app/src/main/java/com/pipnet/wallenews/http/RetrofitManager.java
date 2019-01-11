package com.pipnet.wallenews.http;

import com.pipnet.wallenews.http.interceptor.LoggingInterceptor;
import com.pipnet.wallenews.http.interceptor.RewriteCacheControlInterceptor;
import com.pipnet.wallenews.http.service.ServiceInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by LeeBoo on 17/8/19.
 */
public final class RetrofitManager {

    private static final int DEFAULT_TIMEOUT = 10;//超时时间10s
    private Retrofit retrofit;
    private OkHttpClient.Builder builder;

    private RetrofitManager() {
        //拦截器
        LoggingInterceptor mLoggingInterceptor = LoggingInterceptor.getInstance();
        RewriteCacheControlInterceptor mRewriteCacheControlInterceptor = RewriteCacheControlInterceptor.getInstance();
        //OkHttpClient.Builder
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addNetworkInterceptor(mRewriteCacheControlInterceptor);
        builder.addInterceptor(mRewriteCacheControlInterceptor).addInterceptor(mLoggingInterceptor);
        //Retrofit
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Router.BASE_URL)
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    //获取单例
    public static RetrofitManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ServiceInterface getServiceInterface() {
        return retrofit.create(ServiceInterface.class);
    }

}
