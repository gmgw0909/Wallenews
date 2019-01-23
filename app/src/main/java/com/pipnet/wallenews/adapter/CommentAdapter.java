package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class CommentAdapter extends BaseQuickAdapter<RepliesResponse.RepliesBean, BaseViewHolder> {

    public CommentAdapter(@Nullable List<RepliesResponse.RepliesBean> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RepliesResponse.RepliesBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.authorImage)) {
            avatar.setImageURI(item.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.authorName);
        helper.setText(R.id.title, item.content);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.createTime));
    }
}
