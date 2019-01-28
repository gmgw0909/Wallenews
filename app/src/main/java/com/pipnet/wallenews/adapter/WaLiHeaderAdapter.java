package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedResponse;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class WaLiHeaderAdapter extends BaseQuickAdapter<FeedResponse.TopTopicBean, BaseViewHolder> {

    public WaLiHeaderAdapter(@Nullable List<FeedResponse.TopTopicBean> data) {
        super(R.layout.item_header_wali, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedResponse.TopTopicBean item) {

        RelativeLayout rlTopic = helper.getView(R.id.rl_topic);
        RelativeLayout rlUser = helper.getView(R.id.rl_user);
        if (!TextUtils.isEmpty(item.type) && item.type.equals("recommendAuthor")) {
            rlUser.setVisibility(View.VISIBLE);
            rlTopic.setVisibility(View.GONE);
            SimpleDraweeView avatar = helper.getView(R.id.avatar);
            if (!TextUtils.isEmpty(item.content.image)) {
                avatar.setImageURI(item.content.image);
            } else {
                avatar.setImageResource(R.mipmap.default_avatar);
            }
            helper.setText(R.id.name, item.content.name);
            helper.setText(R.id.followCount, item.content.followerCount + "人关注");
            helper.setText(R.id.title, item.content.latestContentTitle);
        } else {
            rlUser.setVisibility(View.GONE);
            rlTopic.setVisibility(View.VISIBLE);
            SimpleDraweeView topicBg = helper.getView(R.id.iv_topic_bg);
            if (!TextUtils.isEmpty(item.content.image)) {
                topicBg.setImageURI(item.content.image);
            }
            helper.setText(R.id.tv_tag, "#" + item.content.name);
            helper.setText(R.id.title, item.content.latestContentTitle);
        }
    }
}
