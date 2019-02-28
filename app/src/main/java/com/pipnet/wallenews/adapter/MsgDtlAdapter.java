package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

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

public class MsgDtlAdapter extends BaseQuickAdapter<FeedsBean, BaseViewHolder> {

    public MsgDtlAdapter(@Nullable List<FeedsBean> data) {
        super(R.layout.item_msg_dtl, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.content.authorImage)) {
            avatar.setImageURI(item.content.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        SimpleDraweeView img = helper.getView(R.id.img);
        if (item.content.sourceContentImageAry != null && item.content.sourceContentImageAry.size() > 0) {
            img.setImageURI(item.content.sourceContentImageAry.get(0));
        }
        helper.setText(R.id.name, item.content.authorName);
        helper.setText(R.id.wpCount, item.content.content);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
        helper.setText(R.id.title, item.content.sourceAuthorName);
        helper.setText(R.id.content, item.content.sourceContentTitle);
    }
}
