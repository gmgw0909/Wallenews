package com.pipnet.wallenews.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.pipnet.wallenews.base.ActivityController;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.MainActivity;
import com.pipnet.wallenews.module.login.BindPhoneActivity;
import com.pipnet.wallenews.module.login.LoginActivity;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.util.XLog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by LeeBoo on 2017/7/7.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    String state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.handleIntent(this.getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
        XLog.e("onReq");
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        XLog.e("onResp");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                XLog.e("ERR_OK");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    state = sendResp.state;
                    getAccessToken(code);
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                XLog.e("ERR_USER_CANCEL");
                //发送取消
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                XLog.e("ERR_AUTH_DENIED");
                //发送被拒绝
                finish();
                break;
            default:
                //发送返回
                finish();
                break;
        }

    }

    /**
     * 获取openid accessToken
     */
    private void getAccessToken(String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Constants.APP_ID
                + "&secret="
                + Constants.APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(path).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
            }

            @Override
            public void onResponse(Call call, Response response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    String access_token = jsonObject.getString("access_token");
                    String openid = jsonObject.getString("openid");
                    Log.e("Ok==access_token=", jsonObject.toString());
                    if (!TextUtils.isEmpty(access_token)) {
                        getUserInfo(access_token, openid);
                    } else {
                        ToastUtil.show("微信授权失败");
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserInfo(String access_token, String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token
                + "&openid="
                + openid;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(path).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败的处理
            }

            @Override
            public void onResponse(Call call, Response response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    jsonObject.put("source", "WX");
                    Log.e("Ok==wx_info=", jsonObject.toString());
                    if (!TextUtils.isEmpty(state)) {
                        if (state.equals("login")) {
                            //登录
                            wxLogin(jsonObject.toString());
                        } else {
                            //绑定
                            NetRequest.bindWX(jsonObject.toString(), new BaseSubscriber<com.pipnet.wallenews.bean.response.Response>() {
                                @Override
                                public void onNext(com.pipnet.wallenews.bean.response.Response response) {
                                    if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                        EventBus.getDefault().post("BIND_WX_OK");
                                        ToastUtil.show("绑定成功");
                                    }
                                    finish();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 微信登录
     */
    private void wxLogin(String jsonStr) {
        NetRequest.login(jsonStr, "123456", new BaseSubscriber<LoginInfo>() {
            @Override
            public void onNext(LoginInfo info) {
                if (!TextUtils.isEmpty(info.status) && info.status.equals("OK")) {
                    //保存用户信息
                    SPUtils.setObject(info);
                    //登录成功
                    SPUtils.setBoolean("isLogin", true);
                    if (TextUtils.isEmpty(info.mobilePhoneNumber)) {
                        startActivity(new Intent(WXEntryActivity.this, BindPhoneActivity.class)
                                .putExtra("UserInfo", info));
                        return;
                    }
                    startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
                    finish();
                } else {
                    ToastUtil.show("微信登录失败");
                    finish();
                }
            }
        });
    }
}