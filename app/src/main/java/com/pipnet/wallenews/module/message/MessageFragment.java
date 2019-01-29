package com.pipnet.wallenews.module.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiHeaderAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.uihelpers.IRefreshPage;
import com.pipnet.wallenews.uihelpers.RefreshLoadMoreHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class MessageFragment extends LazyFragment implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;

    private RefreshLoadMoreHelper<FeedResponse.TopTopicBean> refreshLoadMoreHelper;

    @Override
    protected int setContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void lazyLoad() {
        title.setText("消息");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, WaLiHeaderAdapter.class);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void loadData() {
        List<Response> list = new ArrayList<>();
        PageList pageList = new PageList();
        pageList.setData(list);
        refreshLoadMoreHelper.loadSuccess(pageList);
    }
}
