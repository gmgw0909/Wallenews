package com.pipnet.wallenews.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pipnet.wallenews.App;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by LeeBoo on 2017/7/7.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    public static final String Tag = "WXEntryActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(App.getInstance(), Constants.APP_ID);
        try {
            boolean result = api.handleIntent(getIntent(), this);
            if (!result) {
                Log.d(Tag, "参数不合法，未被SDK处理，退出");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp == null) {
            ToastUtil.show("WeChat Error");
            finish();
            return;
        }
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
                    //登录
                    String code = ((SendAuth.Resp) baseResp).code;
//                    wxHttp(Constants.ACC_TOKEN_URL, Constants.APP_ID, Constants.APP_SECRET, code, "authorization_code");
                }
                break;
            default:
                finish();
                break;
        }

    }
}