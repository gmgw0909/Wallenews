package com.pipnet.wallenews.module.message;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MsgDtlAdapter;
import com.pipnet.wallenews.adapter.MsgSystemAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;

    List<FeedsBean> list = new ArrayList<>();
    BaseQuickAdapter adapter;
    String titleName, type = "";

    @Override
    public int setContentView() {
        return R.layout.fragment_message;
    }

    @Override
    public void initViewData() {
        titleName = getIntent().getStringExtra("TYPE");
        if (TextUtils.isEmpty(titleName)) {
            titleName = "";
        } else {
            if (titleName.equals("赞")) {
                type = "like";
                adapter = new MsgDtlAdapter(list);
            } else if (titleName.equals("评论")) {
                type = "reply";
                adapter = new MsgDtlAdapter(list);
            } else if (titleName.equals("@我的")) {
                type = "mention";
                adapter = new MsgDtlAdapter(list);
            } else if (titleName.equals("系统公告")) {
                type = "sysPub";
                adapter = new MsgSystemAdapter(list);
            }
        }
        title.setText(titleName);
        refreshLayout.setRefreshHeader(new CarRefreshHeader(this));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getNetData();
    }

    private void getNetData() {
        NetRequest.getMsg(type, new BaseSubscriber<FeedResponse>() {
            @Override
            public void onNext(FeedResponse followResponse) {
                if (followResponse.feeds != null && followResponse.feeds.size() > 0) {
                    List<FeedsBean> list_ = followResponse.feeds;
                    list.clear();
                    list.addAll(list_);
                    adapter.notifyDataSetChanged();
                }
                refreshLayout.finishRefresh();
            }
        });
    }

    @OnClick({R.id.btn_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
        }
    }
}
