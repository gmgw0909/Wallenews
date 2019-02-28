package com.pipnet.wallenews.http.service;

import com.pipnet.wallenews.bean.AuthorInfo;
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FindResponse;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.bean.SearchRecommend;
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

    /**
     * 资讯话题列表
     */
    @GET("we/content/{id}/recommends")
    Flowable<FeedDetailsInfo> recommends(@Path("id") long id);

    /**
     * 点赞和取消点赞
     */
    @POST("we/likeRel/submit")
    @FormUrlEncoded
    Flowable<Response> like(@Field("contentId") String contentId, @Field("relType") String relType, @Field("isConfirmed") String isConfirmed);

    /**
     * 转发
     * sourceId:7
     * content:转发API测试（取消时候，非必填）
     * isAudit:confirmed(默认)/tobe(取消)/deleted
     */
    @POST("we/forward/submit")
    @FormUrlEncoded
    Flowable<Response> forward(@Field("content") String content, @Field("isAudit") String isAudit, @Field("sourceId") String sourceId);

    /**
     * 回复
     * sourceId:7
     * content:转发API测试
     * isAudit:0
     * replyTos:@我的回复
     * replyToIds:1 2 123
     * relType:content(默认)/forward
     */
    @POST("we/reply/submit")
    @FormUrlEncoded
    Flowable<Response> reply(@Field("sourceId") String sourceId, @Field("content") String content, @Field("isAudit") int isAudit,
                             @Field("replyTos") String replyTos, @Field("replyToIds") String replyToIds, @Field("relType") String relType);

    /**
     * 用户详情
     */
    @GET("we/author/{id}/detail")
    Flowable<AuthorInfo> authorDetail(@Path("id") long id);

    /**
     * 用户详情-瓦砾
     */
    @GET("we/author/{id}/feeds")
    Flowable<FeedResponse> authorFeeds(@Path("id") long id, @Query("feedType") String feedType);

    /**
     * 发现首页
     */
    @GET("we/feedFlow/search")
    Flowable<FindResponse> findHome(@Query("cursor") int cursor);

    /**
     * 搜索记录
     */
    @GET("we/feedFlow/searchRecommend")
    Flowable<SearchRecommend> searchRecommend();

    /**
     * 搜索
     */
    @POST("we/feedFlow/search")
    @FormUrlEncoded
    Flowable<FeedResponse> search(@Field("keyword") String keyword);

    /**
     * 搜索联想
     */
    @POST("we/feedFlow/searchSuggest")
    @FormUrlEncoded
    Flowable<SearchRecommend> searchSuggest(@Field("keyword") String keyword);

    /**
     * @ 搜索用户联想
     */
    @GET("we/author/search")
    Flowable<SearchRecommend> authorSearch(@Query("keyword") String keyword);

    /**
     * # 搜索话题联想
     */
    @GET("we/topic/search")
    Flowable<SearchRecommend> topicSearch(@Query("keyword") String keyword);

    /**
     * 消息
     */
    @GET("we/feedFlow/mine")
    Flowable<FeedResponse> getMsg(@Query("type") String type);

}
