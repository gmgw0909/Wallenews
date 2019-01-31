package com.pipnet.wallenews.http.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
     * 绑定微信
     */
    public static void bindWX(String username, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().bindWX(username), subscriber);
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

    /**
     * 瓦砾首页
     */
    public static void feeds(long cursor, String direction, Subscriber<FeedResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().feeds(cursor, direction), subscriber);
    }

    /**
     * 瓦砾资讯详情
     */
    public static void detail(long id, Subscriber<FeedDetailsInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().detail(id), subscriber);
    }

    /**
     * 评论列表
     */
    public static void replies(long id, int page, Subscriber<RepliesResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().replies(id, page), subscriber);
    }

    /**
     * 资讯关联话题列表
     */
    public static void recommends(long id, Subscriber<FeedDetailsInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().recommends(id), subscriber);
    }

    /**
     * 点赞或取消点赞
     * <p>
     * contentId:1
     * relType:content(默认)/forward/reply <非必填>
     * isConfirmed:confirmed(默认)/tobe <非必填>
     */
    public static void like(String contentId, String relType, boolean like, Subscriber<Response> subscriber) {
        String isConfirmed;
        if (like) {
            isConfirmed = "confirmed";
        } else {
            isConfirmed = "tobe";
        }
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().like(contentId, relType, isConfirmed), subscriber);
    }

    /**
     * 回复
     */
    public static void reply(String sourceId, String content, String replyTos, String replyToIds, String relType, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().reply(sourceId, content, 0, replyTos, replyToIds, relType), subscriber);
    }

    /**
     * 转发 confirmed / tobe
     */
    public static void forward(String content, String isAudit, String sourceId, Subscriber<Response> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().forward(content, isAudit, sourceId), subscriber);
    }

    /**
     * 用户详情
     */
    public static void authorDetail(long id, Subscriber<AuthorInfo> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().authorDetail(id), subscriber);
    }

    /**
     * 用户详情-瓦砾
     */
    public static void authorDetail(long id, String feedType, Subscriber<FeedResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().authorFeeds(id, feedType), subscriber);
    }

    /**
     * 发现首页
     */
    public static void findHome(int cursor, Subscriber<FindResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().findHome(cursor), subscriber);
    }

    /**
     * 搜索记录
     */
    public static void searchRecommend(Subscriber<SearchRecommend> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().searchRecommend(), subscriber);
    }

    /**
     * 搜索
     */
    public static void search(String keyword, Subscriber<FeedResponse> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().search(keyword), subscriber);
    }

    /**
     * 搜索联想
     */
    public static void searchSuggest(String keyword, Subscriber<SearchRecommend> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().searchSuggest(keyword), subscriber);
    }

    /**
     * @ 用户搜索联想
     */
    public static void authorSearch(String keyword, Subscriber<SearchRecommend> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().authorSearch(keyword), subscriber);
    }

    /**
     * # 话题搜索联想
     */
    public static void topicSearch(String keyword, Subscriber<SearchRecommend> subscriber) {
        toSubscriber(RetrofitManager.getInstance().getServiceInterface().topicSearch(keyword), subscriber);
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
