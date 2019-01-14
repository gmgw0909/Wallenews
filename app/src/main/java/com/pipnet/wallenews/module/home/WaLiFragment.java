package com.pipnet.wallenews.module.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.adapter.WaLiHeaderAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.uihelpers.IRefreshPage;
import com.pipnet.wallenews.uihelpers.RefreshLoadMoreHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class WaLiFragment extends LazyFragment implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private RefreshLoadMoreHelper<Response> refreshLoadMoreHelper;

    @Override
    protected int setContentView() {
        return R.layout.fragment_wali;
    }

    @Override
    protected void lazyLoad() {
        List<Response> list = new ArrayList<>();
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_wali, null);
        RecyclerView headerRV = header.findViewById(R.id.recycler_header);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        headerRV.setLayoutManager(layoutManager);
        headerRV.setAdapter(new WaLiHeaderAdapter(list));
        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, WaLiAdapter.class);
        refreshLoadMoreHelper.getAdapter().addHeaderView(header);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void loadData() {
        List<Response> list = new ArrayList<>();
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        PageList pageList = new PageList();
        pageList.setData(list);
        refreshLoadMoreHelper.loadSuccess(pageList);
    }
}
