package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.AuthorBean;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.mine.UserDetailActivity;
import com.pipnet.wallenews.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class AuthorAdapter extends BaseQuickAdapter<AuthorBean, BaseViewHolder> {

    public AuthorAdapter(@Nullable List<AuthorBean> data) {
        super(R.layout.item_follow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final AuthorBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.image)) {
            avatar.setImageURI(item.image);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.name);
        helper.setText(R.id.intro, item.introduction);
        TextView follow = helper.getView(R.id.btn_follow);
        if (item.ifFollowed) {
            follow.setText("正在关注");
            follow.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
            follow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_btn_follow_s));
        } else {
            follow.setText("关注");
            follow.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.text_tab_blue, null));
            follow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_btn_follow));
        }
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followOrUnFollow(item, !item.ifFollowed);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra("authorId", item.id));
            }
        });
    }


    //取消关注或者关注
    private void followOrUnFollow(final AuthorBean item, boolean follow) {
        NetRequest.follow(item.id + "", follow, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    int i = mData.indexOf(item);
                    mData.remove(item);
                    item.ifFollowed = !item.ifFollowed;
                    mData.add(i, item);
                    LoginInfo info = SPUtils.getObject(LoginInfo.class);
                    if (item.ifFollowed) {
                        info.followCount += 1;
                    } else {
                        info.followCount -= 1;
                    }
                    SPUtils.setObject(info);
                    EventBus.getDefault().post(Constants.REFRESH_USER);
                    notifyItemChanged(i + 1);
                }
            }
        });
    }
}
