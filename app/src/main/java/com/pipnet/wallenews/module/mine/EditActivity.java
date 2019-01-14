package com.pipnet.wallenews.module.mine;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class EditActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_intro)
    TextView tvIntro;

    @Override
    public int setContentView() {
        return R.layout.activity_edit;
    }

    @Override
    public void initViewData() {
        title.setText("编辑个人资料");
    }

    @OnClick({R.id.btn_left, R.id.rl_avatar, R.id.rl_name, R.id.intro})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.rl_avatar:
                break;
            case R.id.rl_name:
                break;
            case R.id.intro:
                break;
        }
    }
}
