package com.pipnet.wallenews.module.mine;

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
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/14.
 */

public class EditIntroActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.et_intro)
    EditText etIntro;

    LoginInfo info;

    @Override
    public int setContentView() {
        return R.layout.activity_edit_intro;
    }

    @Override
    public void initViewData() {
        title.setText("个人简介");
        btnLeft.setText("取消");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        btnRight.setText("完成");

        info = SPUtils.getObject(LoginInfo.class);
        etIntro.setText(info.introduction);
        etIntro.setSelection(info.introduction.length());
    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                editIntro();
                break;
        }
    }

    private void editIntro() {
        final String intro = etIntro.getText().toString();
        if (TextUtils.isEmpty(intro)) {
            ToastUtil.show("输入不能为空");
            return;
        }
        if (intro.equals(info.introduction)) {
            finish();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("introduction", intro);
        NetRequest.modify(info.userId+"", map, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    ToastUtil.show("修改成功");
                    info.introduction = intro;
                    SPUtils.setObject(info);
                    EventBus.getDefault().post(Constants.REFRESH_USER);
                    finish();
                }
            }
        });
    }

}
