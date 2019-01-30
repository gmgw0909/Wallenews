package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.SearchRecommend;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class SearchSuggestAdapter extends BaseQuickAdapter<SearchRecommend.TagBean, BaseViewHolder> {

    public SearchSuggestAdapter(@Nullable List<SearchRecommend.TagBean> data) {
        super(R.layout.item_search_suggest, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchRecommend.TagBean item) {
        helper.setText(R.id.tag, item.name);
    }
}
