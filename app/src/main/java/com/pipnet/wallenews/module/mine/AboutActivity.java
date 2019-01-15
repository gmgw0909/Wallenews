package com.pipnet.wallenews.module.mine;

import android.view.View;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class AboutActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;

    @Override
    public int setContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void initViewData() {
        title.setText("关于瓦砾");
    }

    @OnClick({R.id.btn_left, R.id.rl_check_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_check_version:

                break;
        }
    }

}
