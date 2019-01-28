package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class SearchTagAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchTagAdapter(@Nullable List<String> data) {
        super(R.layout.item_search_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tag, item);
        TextView nub = helper.getView(R.id.nub);
        if (!TextUtils.isEmpty(item)) {
            nub.setVisibility(View.VISIBLE);
            nub.setText(helper.getAdapterPosition() + 1 + "");
        } else {
            nub.setVisibility(View.GONE);
        }
    }
}
