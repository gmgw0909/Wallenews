package com.pipnet.wallenews.module.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class ReplyActivity extends BaseActivity {

    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.my_avatar)
    SimpleDraweeView myAvatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.hf_name)
    TextView hfName;
    @BindView(R.id.et_comment)
    EditText etComment;

    FeedResponse.FeedsBean.ContentBean contentBean;

    @Override
    public int setContentView() {
        return R.layout.activity_reply;
    }

    @Override
    public void initViewData() {
        contentBean = (FeedResponse.FeedsBean.ContentBean) getIntent().getSerializableExtra("item");
        if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).avatar)) {
            myAvatar.setImageURI(SPUtils.getObject(LoginInfo.class).avatar);
        }
        if (!TextUtils.isEmpty(contentBean.authorImage)) {
            avatar.setImageURI(contentBean.authorImage);
        }
        name.setText(contentBean.authorName);
        hfName.setText("@" + contentBean.authorName);
    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                String comment = etComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    return;
                }
                NetRequest.reply(contentBean.id + "", comment, "", "",
                        "content", new BaseSubscriber<Response>() {
                            @Override
                            public void onNext(Response response) {
                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                    EventBus.getDefault().post(Constants.COMMENT_SUCCESS + contentBean.id);
                                    finish();
                                }
                            }
                        });
                break;
        }
    }
}
