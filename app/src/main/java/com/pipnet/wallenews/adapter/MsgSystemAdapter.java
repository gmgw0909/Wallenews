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

public class MsgSystemAdapter extends BaseQuickAdapter<FeedsBean, BaseViewHolder> {

    public MsgSystemAdapter(@Nullable List<FeedsBean> data) {
        super(R.layout.item_msg_sys, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedsBean item) {
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
        helper.setText(R.id.title, item.content.title);
        helper.setText(R.id.subTitle, item.content.subTitle);
    }
}
