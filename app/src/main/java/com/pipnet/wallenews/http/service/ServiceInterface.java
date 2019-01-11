package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.response.Response;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
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
    Flowable<Response> login(@Body RequestBody body);

}
