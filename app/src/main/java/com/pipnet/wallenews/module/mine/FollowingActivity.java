package com.pipnet.wallenews.module.mine;

import android.view.View;
import android.widget.TextView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class FollowingActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;

    @Override
    public int setContentView() {
        return R.layout.activity_follow;
    }

    @Override
    public void initViewData() {
        title.setText("正在关注");
    }

    @OnClick({R.id.btn_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}