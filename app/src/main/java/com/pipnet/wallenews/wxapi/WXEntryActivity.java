package com.pipnet.wallenews.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pipnet.wallenews.App;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.util.XLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LeeBoo on 2017/7/7.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    private static String uuid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                XLog.e("ERR_USER_CANCEL");
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                XLog.e("ERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }

    }

    /**
     * 获取openid accessToken值用于后期操作
     * @param code 请求码
     */
//    private void getAccess_token(final String code) {
//        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
//                + WEIXIN_APP_ID
//                + "&secret="
//                + APP_SECRET
//                + "&code="
//                + code
//                + "&grant_type=authorization_code";
//        LogUtils.log("getAccess_token：" + path);
//        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtils.log("getAccess_token_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String openid = jsonObject.getString("openid").toString().trim();
//                    String access_token = jsonObject.getString("access_token").toString().trim();
//                    getUserMesg(access_token, openid);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
//    }


    /**
     * 获取微信的个人信息
     * @param access_token
     * @param openid
     */
//    private void getUserMesg(final String access_token, final String openid) {
//        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
//                + access_token
//                + "&openid="
//                + openid;
//        LogUtils.log("getUserMesg：" + path);
//        //网络请求，根据自己的请求方式
//        VolleyRequest.get(this, path, "getAccess_token", false, null, new VolleyRequest.Callback() {
//            @Override
//            public void onSuccess(String result) {
//                LogUtils.log("getUserMesg_result:" + result);
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(result);
//                    String nickname = jsonObject.getString("nickname");
//                    int sex = Integer.parseInt(jsonObject.get("sex").toString());
//                    String headimgurl = jsonObject.getString("headimgurl");
//
//                    LogUtils.log("用户基本信息:");
//                    LogUtils.log("nickname:" + nickname);
//                    LogUtils.log("sex:" + sex);
//                    LogUtils.log("headimgurl:" + headimgurl);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                finish();
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//
//            }
//        });
//    }

}