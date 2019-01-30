package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.module.home.FeedDetailActivity;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class FindTopicAdapter extends BaseQuickAdapter<FeedResponse.TopTopicBean, BaseViewHolder> {

    public FindTopicAdapter(@Nullable List<FeedResponse.TopTopicBean> data) {
        super(R.layout.item_topic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedResponse.TopTopicBean item) {
        helper.setText(R.id.title, helper.getAdapterPosition() + 1 + " " + item.content.name);
        helper.setText(R.id.wpCount, item.content.contentCount + "瓦片");
        helper.setText(R.id.content, item.content.latestContentTitle);
        SimpleDraweeView img = helper.getView(R.id.img);
        if (item.content.latestContentImageArray != null && item.content.latestContentImageArray.size() > 0) {
            img.setImageURI(item.content.latestContentImageArray.get(0));
        }
        helper.getView(R.id.ll_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, FeedDetailActivity.class).putExtra("FEED_ID", item.content.latestContentId));
            }
        });
    }
}
