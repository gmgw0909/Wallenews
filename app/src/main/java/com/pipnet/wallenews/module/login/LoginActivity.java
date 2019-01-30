package com.pipnet.wallenews.module.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.MainActivity;
import com.pipnet.wallenews.util.CheckUtils;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.widgets.ClearEditText;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    TextView btnGetCode;

    MyCount myCount;
    IWXAPI api;//微信api

    @Override
    public int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initViewData() {
        regToWx();
    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
    }

    @OnClick({R.id.btn_get_code, R.id.btn_login, R.id.btn_wx_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_code:
                getVerCode();
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_wx_login:
                sendOauthRequest();
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerCode() {
        String mobileNumber = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNumber)) {
            ToastUtil.show("请输入手机号");
            etPhone.requestFocus();
            return;
        }
        if (!CheckUtils.isMobileNO(mobileNumber) && !CheckUtils.isTestNO(mobileNumber)) {
            ToastUtil.show("请输入正确手机号");
            etPhone.setSelection(mobileNumber.length());
            etPhone.requestFocus();
            return;
        }
        //创建倒计时任务
        myCount = new MyCount(61000, 1000);
        NetRequest.sendMobileCode(mobileNumber, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                ToastUtil.show(response.msg);
                if (response.success) {
                    btnGetCode.setEnabled(false);
                    myCount.start();
                }
            }
        });
    }

    /**
     * 登陆
     */
    private void login() {
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号");
            etPhone.requestFocus();
            return;
        }
        if (!CheckUtils.isMobileNO(phone) && !CheckUtils.isTestNO(phone)) {
            ToastUtil.show("请输入正确手机号");
            etPhone.setSelection(phone.length());
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show("请输入验证码");
            etCode.requestFocus();
            return;
        }
        if (code.length() != 6) {
            ToastUtil.show("请输入6位验证码");
            etCode.requestFocus();
            return;
        }
        //"VerificationCode:" +
        NetRequest.login("VerificationCode:" + phone, code, new BaseSubscriber<LoginInfo>() {
            @Override
            public void onNext(LoginInfo info) {
                if (!TextUtils.isEmpty(info.status) && info.status.equals("OK")) {
                    //登录成功
                    SPUtils.setBoolean("isLogin", true);
                    //保存用户信息
                    SPUtils.setObject(info);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    ToastUtil.show(info.error);
                }
            }
        });
    }

    //发送微信登录请求
    private void sendOauthRequest() {
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("您还未安装微信客户端");
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "login";
        api.sendReq(req);
    }

    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btnGetCode.setText("获取验证码");
            btnGetCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String value = millisUntilFinished / 1000 + "";
            btnGetCode.setText(value);
        }
    }
}
