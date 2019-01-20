package com.pipnet.wallenews.module.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.ActivityController;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.MainActivity;
import com.pipnet.wallenews.util.CheckUtils;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.widgets.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.et_phone)
    ClearEditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    TextView btnGetCode;

    MyCount myCount;
    LoginInfo info;

    @Override
    public int setContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initViewData() {
        title.setText("绑定手机");
        btnRight.setText("跳过");
        info = (LoginInfo) getIntent().getSerializableExtra("UserInfo");
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.btn_get_code, R.id.btn_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                if (TextUtils.isEmpty(info.uid)){
                    finish();
                    return;
                }
                //登录成功
                SPUtils.setBoolean("isLogin", true);
                //保存用户信息
                SPUtils.setObject(info);
                ActivityController.finishAll();
                startActivity(new Intent(BindPhoneActivity.this, MainActivity.class));
                break;
            case R.id.btn_get_code:
                getVerCode();
                break;
            case R.id.btn_bind:
                bindPhone();
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
    private void bindPhone() {
        final String phone = etPhone.getText().toString().trim();
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
        NetRequest.bindMobile(phone, code, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    info.mobilePhoneNumber = phone;
                    //登录成功
                    SPUtils.setBoolean("isLogin", true);
                    //保存用户信息
                    SPUtils.setObject(info);
                    ActivityController.finishAll();
                    startActivity(new Intent(BindPhoneActivity.this, MainActivity.class));
                } else {
                    ToastUtil.show("请求服务器失败");
                }
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
