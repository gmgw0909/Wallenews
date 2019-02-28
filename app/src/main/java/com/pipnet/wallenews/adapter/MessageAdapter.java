package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class MessageAdapter extends BaseQuickAdapter<FeedsBean, BaseViewHolder> {

    public MessageAdapter(@Nullable List<FeedsBean> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.content.authorImage)) {
            avatar.setImageURI(item.content.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.content.title);
        helper.setText(R.id.intro, item.content.subTitle);
        if (item.content.commentCount == 0) {
            helper.getView(R.id.count).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.count).setVisibility(View.VISIBLE);
            helper.setText(R.id.count, item.content.commentCount + "");
        }
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
    }
}
