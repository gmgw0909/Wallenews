package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.WeatherInfo;
import com.pipnet.wallenews.http.RetrofitManager;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LeeBoo on 2017/8/18.
 */

public class NetRequest {

    /**
     * test(api)
     */
    public static void test(String cityId, Subscriber<WeatherInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().getWeatherInfo(cityId), subscriber);
    }

    /**
     * 统一线程处理
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerTransformer() {
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
     * 抽象调度者
     */
    public static <T> void toSubscriber(Flowable<T> flowable, Subscriber<T> subscriber) {
        flowable.compose(NetRequest.<T>rxSchedulerTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
