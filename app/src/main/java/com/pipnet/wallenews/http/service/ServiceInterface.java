package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.UserLogin;
import com.pipnet.wallenews.bean.WeatherInfo;
import com.pipnet.wallenews.bean.response.ResponseObject;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mzb on 2017/8/20.
 * 网络访问的接口
 */

public interface ServiceInterface {

    /**
     * ⼿机快速登录
     */
    @POST("user/login")
    Flowable<ResponseObject<UserLogin>> login(@Header("mobile") String mobileHeader,
                                              @Header("token") String tokeHeader,
                                              @Body RequestBody route);
    @GET("adat/sk/{cityId}.html")
    Flowable<WeatherInfo> getWeatherInfo(@Path("cityId") String cityId);
}
