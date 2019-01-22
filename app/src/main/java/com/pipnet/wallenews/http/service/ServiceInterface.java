package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.bean.UploadResponse;
import com.pipnet.wallenews.bean.response.Response;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    /**
     * 用户信息修改
     */
    @POST("we/author/{uid}/modify")
    @FormUrlEncoded
    Flowable<Response> modify(@Path("uid") String uid, @FieldMap Map<String, String> map);

    /**
     * 图像上传
     */
    @POST("ajaxUpload")
    @Multipart
    Flowable<UploadResponse> uploadImg(@Part List<MultipartBody.Part> parts);

    /**
     * 个人中心
     */
    @GET("myspace/me?loginUrl=/register/message/notLogin")
    Flowable<LoginInfo> mySpace();

    /**
     * 绑定手机号
     */
    @POST("myspace/bindMobile")
    @FormUrlEncoded
    Flowable<Response> bindMobile(@Field("mobilePhoneNumber") String mobilePhoneNumber, @Field("verificationCode") String verificationCode);

    /**
     * 绑定微信
     */
    @POST("myspace/bindWX")
    @FormUrlEncoded
    Flowable<Response> bindWX(@Field("username") String username);

    /**
     * 我关注的人
     */
    @GET("we/friendRel/followList")
    Flowable<FollowResponse> followList(@Query("cursor") String cursor);

    /**
     * 关注我的人
     */
    @GET("we/friendRel/followerList")
    Flowable<FollowResponse> followerList(@Query("cursor") String cursor);

    /**
     * 关注和取消关注
     */
    @POST("we/friendRel/follow")
    @FormUrlEncoded
    Flowable<Response> follow(@Field("friendId") String friendId, @Field("isConfirmed") String isConfirmed);

    /**
     * 瓦砾首页
     */
    @GET("we/feedFlow/feeds")
    Flowable<FeedResponse> feeds(@Query("cursor") long cursor, @Query("direction") String direction);

    /**
     * 资讯详情
     */
    @GET("we/content/{id}/detail")
    Flowable<FeedDetailsInfo> detail(@Path("id") long id);

    /**
     * 资讯评论
     */
    @GET("we/content/{id}/replies")
    Flowable<RepliesResponse> replies(@Path("id") long id, @Query("page") int page);

}
