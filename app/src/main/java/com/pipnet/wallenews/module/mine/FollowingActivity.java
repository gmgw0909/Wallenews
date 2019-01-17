package com.pipnet.wallenews.module.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.FollowAdapter;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.uihelpers.IRefreshPage;
import com.pipnet.wallenews.uihelpers.RefreshLoadMoreHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class FollowingActivity extends BaseActivity implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private RefreshLoadMoreHelper<FollowResponse.Feeds> refreshLoadMoreHelper;
    PageList list = new PageList();
    private String cursor;

    @Override
    public int setContentView() {
        return R.layout.activity_follow;
    }

    @Override
    public void initViewData() {
        title.setText("正在关注");
        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, FollowAdapter.class);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
    }

    @OnClick({R.id.btn_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void loadData() {
        NetRequest.followList(cursor, new BaseSubscriber<FollowResponse>() {
            @Override
            public void onNext(FollowResponse response) {
                if (response.feeds != null && response.feeds.size() > 0) {
                    list.setData(response.feeds);
                    if (!refreshLoadMoreHelper.isFirstPage()) {
                        cursor = response.feeds.get(response.feeds.size() - 1).cursor + "";
                    } else {
                        cursor = "";
                    }
                    refreshLoadMoreHelper.loadOk(list, cursor);
                } else {
                    refreshLoadMoreHelper.loadNoData(cursor);
                }
            }
        });
    }
}