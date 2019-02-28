package com.pipnet.wallenews.module.message;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MessageAdapter;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.adapter.WaLiHeaderAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.uihelpers.IRefreshPage;
import com.pipnet.wallenews.uihelpers.RefreshLoadMoreHelper;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class MessageFragment extends LazyFragment implements OnRefreshListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;

    List<FeedsBean> list = new ArrayList<>();
    MessageAdapter adapter;

    @Override
    protected int setContentView() {
        return R.layout.fragment_message;
    }

    @Override
    protected void lazyLoad() {
        title.setText("消息");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getActivity().getResources().getColor(R.color.line_light))
                .build(recyclerView));
        recyclerView.setAdapter(adapter = new MessageAdapter(list));
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshHeader(new CarRefreshHeader(getActivity()));
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);
        adapter.setOnItemClickListener(this);
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.autoRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(getActivity(), MessageActivity.class));
    }

    private void getNetData() {
        NetRequest.getMsg("message", new BaseSubscriber<FeedResponse>() {
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

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getNetData();
    }
}
