package com.pipnet.wallenews.module.mine;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;

    @Override
    public int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initViewData() {
        title.setText("设置");
    }

    @OnClick({R.id.btn_left, R.id.rl_edit, R.id.rl_phone, R.id.rl_about, R.id.btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_edit:
                break;
            case R.id.rl_phone:
                break;
            case R.id.rl_about:
                break;
            case R.id.btn_logout:
                SPUtils.clear();
                break;
        }
    }
}
