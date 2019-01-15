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

public class EditNameActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;
    @BindView(R.id.btn_right)
    TextView btnRight;

    @Override
    public int setContentView() {
        return R.layout.activity_edit_name;
    }

    @Override
    public void initViewData() {
        title.setText("修改昵称");
        btnLeft.setText("取消");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnRight.setText("完成");
    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:

                break;
        }
    }

}
