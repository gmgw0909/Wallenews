package com.pipnet.wallenews.module.find;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.SearchTagAdapter;
import com.pipnet.wallenews.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/27.
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.rv_like)
    RecyclerView rvLike;
    @BindView(R.id.et_search)
    EditText etSearch;

    SearchTagAdapter hotAdapter;
    SearchTagAdapter historyAdapter;
    SearchTagAdapter likeAdapter;

    List<String> list = new ArrayList<>();

    @Override
    public int setContentView() {
        return R.layout.activity_search;
    }

    @Override
    public void initViewData() {
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        list.add("特朗普");
        hotAdapter = new SearchTagAdapter(list);
        historyAdapter = new SearchTagAdapter(list);
        likeAdapter = new SearchTagAdapter(list);
        rvHot.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvHistory.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvLike.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvHot.setAdapter(hotAdapter);
        rvHistory.setAdapter(historyAdapter);
        rvLike.setAdapter(likeAdapter);
    }
}
