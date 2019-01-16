package com.pipnet.wallenews.module.mine;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.ActivityController;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constans;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.module.login.LoginActivity;
import com.pipnet.wallenews.util.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;

    @Override
    public int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViewData() {
        EventBus.getDefault().register(this);
        title.setText("设置");
        if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).avatar)) {
            avatar.setImageURI(SPUtils.getObject(LoginInfo.class).avatar);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).mobilePhoneNumber)) {
            tvPhone.setText(SPUtils.getObject(LoginInfo.class).mobilePhoneNumber);
        }
    }

    @OnClick({R.id.btn_left, R.id.rl_edit, R.id.rl_phone, R.id.rl_about, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_edit:
                startActivity(new Intent(SettingActivity.this, EditActivity.class));
                break;
            case R.id.rl_phone:
                startActivity(new Intent(SettingActivity.this, ModifyPhoneActivity.class));
                break;
            case R.id.rl_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.btn_logout:
                SPUtils.setBoolean("isLogin", false);
                SPUtils.removeObject(LoginInfo.class);
                ActivityController.finishAll();
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        if (event.equals(Constans.REFRESH_USER)) {
            String avatarUrl = SPUtils.getObject(LoginInfo.class).avatar;
            if (!TextUtils.isEmpty(avatarUrl)) {
                avatar.setImageURI(avatarUrl);
            } else {
                avatar.setImageResource(R.mipmap.default_avatar);
            }
            if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).mobilePhoneNumber)) {
                tvPhone.setText(SPUtils.getObject(LoginInfo.class).mobilePhoneNumber);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
