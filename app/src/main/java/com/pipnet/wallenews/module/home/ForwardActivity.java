package com.pipnet.wallenews.module.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.ContentBean;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ForwardActivity extends BaseActivity {

    @BindView(R.id.my_avatar)
    SimpleDraweeView myAvatar;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.sourceAuthorName)
    TextView sourceAuthorName;
    @BindView(R.id.sourceContentTitle)
    TextView sourceContentTitle;
    @BindView(R.id.img1_1)
    SimpleDraweeView img11;
    @BindView(R.id.img23_1)
    SimpleDraweeView img231;
    @BindView(R.id.img2_2)
    SimpleDraweeView img22;
    @BindView(R.id.img3_2)
    SimpleDraweeView img32;
    @BindView(R.id.img3_3)
    SimpleDraweeView img33;
    @BindView(R.id.ll_3)
    LinearLayout ll3;
    @BindView(R.id.ll_img)
    LinearLayout llImg;

    ContentBean contentBean;

    long id = 0;

    @Override
    public int setContentView() {
        return R.layout.activity_forward;
    }

    @Override
    public void initViewData() {
        if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).avatar)) {
            myAvatar.setImageURI(SPUtils.getObject(LoginInfo.class).avatar);
        }
        contentBean = (ContentBean) getIntent().getSerializableExtra("item");
        if (!TextUtils.isEmpty(contentBean.sourceAuthorName)) {
            id = contentBean.sourceId;
            sourceAuthorName.setText(contentBean.sourceAuthorName);
            sourceContentTitle.setText(contentBean.sourceContentTitle);
            etComment.setText("//@" + contentBean.authorName + " :" + contentBean.content);
            if (contentBean.sourceContentImageAry != null && contentBean.sourceContentImageAry.size() > 0) {
                llImg.setVisibility(View.VISIBLE);
                List<String> images = contentBean.sourceContentImageAry;
                if (images.size() == 1) {
                    img11.setVisibility(View.VISIBLE);
                    img231.setVisibility(View.GONE);
                    img22.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                    img32.setVisibility(View.GONE);
                    img33.setVisibility(View.GONE);
                    img11.setImageURI(images.get(0));
                } else if (images.size() == 2) {
                    img11.setVisibility(View.GONE);
                    img231.setVisibility(View.VISIBLE);
                    img22.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.GONE);
                    img32.setVisibility(View.GONE);
                    img33.setVisibility(View.GONE);
                    img231.setImageURI(images.get(0));
                    img22.setImageURI(images.get(1));
                } else if (images.size() == 3) {
                    img11.setVisibility(View.GONE);
                    img231.setVisibility(View.VISIBLE);
                    img22.setVisibility(View.GONE);
                    ll3.setVisibility(View.VISIBLE);
                    img32.setVisibility(View.VISIBLE);
                    img33.setVisibility(View.VISIBLE);
                    img231.setImageURI(images.get(0));
                    img32.setImageURI(images.get(1));
                    img33.setImageURI(images.get(2));
                }
            } else {
                llImg.setVisibility(View.GONE);
            }
        } else {
            id = contentBean.id;
            sourceAuthorName.setText(contentBean.authorName);
            sourceContentTitle.setText(contentBean.title);
            if (contentBean.imageArray != null && contentBean.imageArray.size() > 0) {
                llImg.setVisibility(View.VISIBLE);
                List<String> images = contentBean.imageArray;
                if (images.size() == 1) {
                    img11.setVisibility(View.VISIBLE);
                    img231.setVisibility(View.GONE);
                    img22.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                    img32.setVisibility(View.GONE);
                    img33.setVisibility(View.GONE);
                    img11.setImageURI(images.get(0));
                } else if (images.size() == 2) {
                    img11.setVisibility(View.GONE);
                    img231.setVisibility(View.VISIBLE);
                    img22.setVisibility(View.VISIBLE);
                    ll3.setVisibility(View.GONE);
                    img32.setVisibility(View.GONE);
                    img33.setVisibility(View.GONE);
                    img231.setImageURI(images.get(0));
                    img22.setImageURI(images.get(1));
                } else if (images.size() == 3) {
                    img11.setVisibility(View.GONE);
                    img231.setVisibility(View.VISIBLE);
                    img22.setVisibility(View.GONE);
                    ll3.setVisibility(View.VISIBLE);
                    img32.setVisibility(View.VISIBLE);
                    img33.setVisibility(View.VISIBLE);
                    img231.setImageURI(images.get(0));
                    img32.setImageURI(images.get(1));
                    img33.setImageURI(images.get(2));
                }
            } else {
                llImg.setVisibility(View.GONE);
            }
        }
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
                    comment = "转发";
                }
                NetRequest.forward(comment, "confirmed", id + "", new BaseSubscriber<Response>() {
                    @Override
                    public void onNext(Response response) {
                        if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                            EventBus.getDefault().post(Constants.FORWARD_SUCCESS + contentBean.id);
                            finish();
                        }
                    }
                });
                break;
        }
    }
}
