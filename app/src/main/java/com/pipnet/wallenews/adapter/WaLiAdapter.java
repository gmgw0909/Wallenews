package com.pipnet.wallenews.adapter;

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
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class WaLiAdapter extends BaseQuickAdapter<FeedResponse.FeedsBean, BaseViewHolder> {

    public WaLiAdapter(@Nullable List<FeedResponse.FeedsBean> data) {
        super(R.layout.item_wali, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedResponse.FeedsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.content.authorImage)) {
            avatar.setImageURI(item.content.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.content.authorName);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
        helper.setText(R.id.title, item.content.title);
    }

}
