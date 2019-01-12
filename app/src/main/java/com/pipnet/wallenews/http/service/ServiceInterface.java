package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by LeeBoo on 2019/1/11.
 * 网络访问的接口
 */

public interface ServiceInterface {

    /**
     * 获取验证码
     */
    @GET("register/message/sendMobileCode")
    Flowable<Response> sendMobileCode(@Query("mobile") String mobile);

    /**
     * ⼿机快速登录
     */
    @POST("login")
    @FormUrlEncoded
    Flowable<LoginInfo> login(@Field("username") String username, @Field("password") String password,
                              @Field("rememberMe") boolean rememberMe, @Field("loginBackUrl") String loginBackUrl);

}
