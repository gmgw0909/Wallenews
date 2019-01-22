package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.RepliesResponse;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class CommentAdapter extends BaseQuickAdapter<RepliesResponse.RepliesBean, BaseViewHolder> {

    public CommentAdapter(@Nullable List<RepliesResponse.RepliesBean> data) {
        super(R.layout.item_header_wali, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RepliesResponse.RepliesBean item) {

    }
}
