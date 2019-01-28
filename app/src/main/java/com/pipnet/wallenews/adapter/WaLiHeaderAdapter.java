package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.response.Response;

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
        } else {
            rlUser.setVisibility(View.GONE);
            rlTopic.setVisibility(View.VISIBLE);
        }
    }
}
