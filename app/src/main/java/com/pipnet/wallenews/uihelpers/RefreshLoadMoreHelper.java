package com.pipnet.wallenews.uihelpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvinljw on 2018/11/20 10:39
 * Function：
 * Desc：
 */
public class RefreshLoadMoreHelper<T> implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int firstPage = 0;
    private int currPage = firstPage;

    private SmartRefreshLayout refreshLayout;

    private List<T> items;
    private BaseQuickAdapter<T, BaseViewHolder> adapter;
    private IRefreshPage refreshPage;

    public RefreshLoadMoreHelper(IRefreshPage refreshPage, SmartRefreshLayout refreshLayout, RecyclerView recyclerView, Class<? extends BaseQuickAdapter<T, BaseViewHolder>> adapterClass, int... adapterLayoutId) {
        this.refreshPage = refreshPage;
        this.refreshLayout = refreshLayout;

        Context context = recyclerView.getContext();
//        refreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.colorPrimary));
        refreshLayout.setRefreshHeader(new CarRefreshHeader(context));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.line_light))
                .build(recyclerView));
        this.items = new ArrayList<>();
        try {
            if (adapterLayoutId != null && adapterLayoutId.length > 0) {
                this.adapter = adapterClass.getConstructor(Integer.class, List.class).newInstance(adapterLayoutId[0], items);
            } else {
                this.adapter = adapterClass.getConstructor(List.class).newInstance(items);
            }
            recyclerView.setAdapter(this.adapter);
            adapter.setOnLoadMoreListener(this, recyclerView);
            adapter.setEmptyView(R.layout.layout_empty);
        } catch (Exception e) {
            throw new RuntimeException("Adapter's constructor must be Adapter(layoutId,List) or Adapter(List)");
        }
    }

    public void autoRefresh() {
        refreshLayout.autoRefresh();
    }

    public void loadSuccess(PageList<T> response) {
        if (firstPage + 1 == response.getCurPage()) {
            items.clear();
        }
        items.addAll(response.getData());
        if (firstPage == 0) {
            adapter.setEnableLoadMore(response.hasNextStartWithZero());
        } else {
            adapter.setEnableLoadMore(response.hasNext());
        }
        if (currPage > firstPage) {
            adapter.loadMoreComplete();
        } else {
            refreshLayout.finishRefresh();
            adapter.notifyDataSetChanged();
        }
    }

    public void loadOk(PageList<T> list, String cursor) {
        if (TextUtils.isEmpty(cursor)) {
            items.clear();
        }
        items.addAll(list.getData());
        if (firstPage == 0) {
            adapter.setEnableLoadMore(list.hasNextStartWithZero());
        } else {
            adapter.setEnableLoadMore(list.hasNext());
        }
        if (currPage > firstPage) {
            adapter.loadMoreComplete();
        } else {
            refreshLayout.finishRefresh();
            adapter.notifyDataSetChanged();
        }
    }

    public void loadNoData(String cursor) {
        if (TextUtils.isEmpty(cursor)) {//刷新
            refreshLayout.finishRefresh();
        } else {//加载更多
            adapter.loadMoreFail();
        }
    }

    public void loadError() {
        if (currPage > firstPage) {
            adapter.loadMoreFail();
        } else {
            refreshLayout.finishRefresh();
        }
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        currPage = firstPage;
        if (refreshPage != null) {
            refreshPage.loadData();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        currPage++;
        if (refreshPage != null) {
            refreshPage.loadData();
        }
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener listener) {
        adapter.setOnItemClickListener(listener);
    }

    public T getItem(int position) {
        if (items == null || items.size() < position || position < 0) {
            return null;
        }
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public BaseQuickAdapter<T, BaseViewHolder> getAdapter() {
        return adapter;
    }

    public boolean isFirstPage() {
        return currPage == firstPage;
    }

    public void onDestroy() {
        refreshLayout = null;
        adapter.setOnItemClickListener(null);
        adapter = null;
        refreshPage = null;
    }
}
