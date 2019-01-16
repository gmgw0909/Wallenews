package com.pipnet.wallenews.module.mine;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    public int setContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewData() {
        title.setText("关于瓦砾");
        tvVersion.setText("Version" + getVersion());
    }

    @OnClick({R.id.btn_left, R.id.rl_check_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_check_version:
                ToastUtil.show("已经是最新版");
                break;
        }
    }

    //获取版本号
    private String getVersion() {
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "x.x.x";
        }
    }

}
