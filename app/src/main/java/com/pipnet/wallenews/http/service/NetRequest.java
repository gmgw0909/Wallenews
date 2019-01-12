package com.pipnet.wallenews.http.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.RetrofitManager;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by LeeBoo on 2017/8/18.
 */

public class NetRequest {

    /**
     * 获取验证码
     */
    public static void sendMobileCode(String phone, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().sendMobileCode(phone), subscriber);
    }

    /**
     * 验证码登陆
     * username:VerificationCode:<手机号码>
     * password:<短信验证码>
     * rememberMe:true
     * loginBackUrl:/myspace/me
     */
    public static void login(String phone, String verCode, Subscriber<LoginInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().login(phone, verCode, true, "/myspace/me"), subscriber);
    }

    //======================================================上面是所有后台接口=========================================================

    /**
     * 抽象调度者
     */
    private static <T> void toSubscriber(Flowable<T> flowable, Subscriber<T> subscriber) {
        flowable.compose(NetRequest.<T>rxSchedulerTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 统一线程处理
     */
    private static <T> FlowableTransformer<T, T> rxSchedulerTransformer() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io());
            }
        };
    }

    /**
     * 处理请求数据为json格式
     */
    private static RequestBody getRequestBody(Map<String, Object> map) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(map));
        return body;
    }
}
