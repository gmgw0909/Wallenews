package com.pipnet.wallenews.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.CheckUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.widgets.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    TextView btnGetCode;

    MyCount myCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
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
                btnGetCode.setEnabled(false);
                myCount.start();
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
        NetRequest.login(phone, code, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {

            }
        });
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
