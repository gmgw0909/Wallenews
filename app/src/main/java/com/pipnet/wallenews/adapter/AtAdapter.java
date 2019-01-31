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
import com.pipnet.wallenews.bean.TopicsBean;
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

public class AtAdapter extends BaseQuickAdapter<TopicsBean, BaseViewHolder> {

    public AtAdapter(@Nullable List<TopicsBean> data) {
        super(R.layout.item_follow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TopicsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.image)) {
            avatar.setImageURI(item.image);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.name);
        helper.setText(R.id.intro, item.introduction);
        TextView follow = helper.getView(R.id.btn_follow);
        follow.setVisibility(View.GONE);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra("authorId", item.id));
            }
        });
    }
}
