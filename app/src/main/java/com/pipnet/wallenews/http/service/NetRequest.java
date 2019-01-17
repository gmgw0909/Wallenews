package com.pipnet.wallenews.http.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.UploadResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.RetrofitManager;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;

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

    /**
     * 上传图片
     */
    public static void uploadImg(List<MultipartBody.Part> parts, Subscriber<UploadResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().uploadImg(parts), subscriber);
    }

    /**
     * 修改用户信息 头像 昵称 个人简介
     */
    public static void modify(String uid, Map<String, String> map, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().modify(uid, map), subscriber);
    }

    /**
     * 个人中心
     */
    public static void mySpace(Subscriber<LoginInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().mySpace(), subscriber);
    }

    /**
     * 绑定手机号
     */
    public static void bindMobile(String mobilePhoneNumber, String verificationCode, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().bindMobile(mobilePhoneNumber, verificationCode), subscriber);
    }

    /**
     * 我关注的人
     */
    public static void followList(String cursor, Subscriber<FollowResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().followList(cursor), subscriber);
    }

    /**
     * 关注我的人
     */
    public static void followerList(String cursor, Subscriber<FollowResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().followerList(cursor), subscriber);
    }

    /**
     * 关注和取消关注
     */
    public static void follow(String friendId, boolean follow, Subscriber<Response> subscriber) {
        String isConfirmed;//confirmed(关注)/tobe(取消关注)
        if (follow) {
            isConfirmed = "confirmed";
        } else {
            isConfirmed = "tobe";
        }
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().follow(friendId, isConfirmed), subscriber);
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
