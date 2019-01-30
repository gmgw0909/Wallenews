package com.pipnet.wallenews.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.SearchRecommend;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class SearchTagAdapter extends BaseQuickAdapter<SearchRecommend.TagBean, BaseViewHolder> {

    public boolean needNumber;

    public SearchTagAdapter(@Nullable List<SearchRecommend.TagBean> data) {
        super(R.layout.item_search_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchRecommend.TagBean item) {
        helper.setText(R.id.tag, item.name);
        TextView nub = helper.getView(R.id.nub);
        if (needNumber) {
            nub.setVisibility(View.VISIBLE);
            nub.setText(helper.getAdapterPosition() + 1 + "");
            if (helper.getAdapterPosition() == 0) {
                nub.setBackgroundColor(Color.parseColor("#f23434"));
            } else if (helper.getAdapterPosition() == 1) {
                nub.setBackgroundColor(Color.parseColor("#f66b0e"));
            } else if (helper.getAdapterPosition() == 2) {
                nub.setBackgroundColor(Color.parseColor("#f2ad34"));
            }
        } else {
            nub.setVisibility(View.GONE);
        }
    }
}
