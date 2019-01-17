package com.pipnet.wallenews.module.mine;

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
import com.pipnet.wallenews.util.CheckUtils;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class ModifyPhoneActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code)
    TextView btnGetCode;

    MyCount myCount;

    @Override
    public int setContentView() {
        return R.layout.activity_modify_phone;
    }

    @Override
    public void initViewData() {
        title.setText("修改手机号码");
        btnRight.setText("完成");
    }

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.btn_get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                commit();
                break;
            case R.id.btn_get_code:
                getVerCode();
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
     * 修改
     */
    private void commit() {
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
                    ToastUtil.show("修改成功");
                    LoginInfo info = SPUtils.getObject(LoginInfo.class);
                    info.mobilePhoneNumber = phone;
                    SPUtils.setObject(info);
                    EventBus.getDefault().post(Constants.REFRESH_USER);
                    finish();
                } else {
                    ToastUtil.show("服务器出错");
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
