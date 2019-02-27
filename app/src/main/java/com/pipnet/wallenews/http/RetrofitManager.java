package com.pipnet.wallenews.http;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.pipnet.wallenews.App;
import com.pipnet.wallenews.http.interceptor.HeaderInterceptor;
import com.pipnet.wallenews.http.interceptor.HttpLoggingInterceptor;
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

    private static final int DEFAULT_TIMEOUT = 20;//超时时间10s
    private Retrofit retrofit;
    private OkHttpClient.Builder builder;

    private RetrofitManager() {
        //OkHttpClient.Builder
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(HeaderInterceptor.getInstance());//设置请求头
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);//打印日志(放最后)
        builder.cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getInstance())));
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
