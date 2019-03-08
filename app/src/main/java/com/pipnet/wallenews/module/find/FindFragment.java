package com.pipnet.wallenews.module.find;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.AuthorAdapter;
import com.pipnet.wallenews.adapter.FindTopicAdapter;
import com.pipnet.wallenews.adapter.FollowAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.AuthorBean;
import com.pipnet.wallenews.bean.AuthorInfo;
import com.pipnet.wallenews.bean.ContentBean;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FindResponse;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class FindFragment extends LazyFragment implements OnRefreshListener {

    RecyclerView recyclerTopic;

    @BindView(R.id.recycler_follow)
    RecyclerView recyclerFollow;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    AuthorAdapter followAdapter;
    FindTopicAdapter findTopicAdapter;

    List<FeedResponse.TopTopicBean> topList = new ArrayList();
    List<AuthorBean> authList = new ArrayList();

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void lazyLoad() {
        initView();
    }

    private void initView() {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_find_header, null);
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.item_find_footer, null);
        recyclerTopic = header.findViewById(R.id.recycler_topic);
        refreshLayout.setRefreshHeader(new CarRefreshHeader(getActivity()));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);
        recyclerFollow.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerFollow.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getActivity().getResources().getColor(R.color.line_light))
                .build(recyclerFollow));
        recyclerTopic.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerTopic.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getActivity().getResources().getColor(R.color.line_light))
                .build(recyclerTopic));
        recyclerFollow.setAdapter(followAdapter = new AuthorAdapter(authList));
        recyclerTopic.setAdapter(findTopicAdapter = new FindTopicAdapter(topList));
        followAdapter.addHeaderView(header);
        followAdapter.addFooterView(footer);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {
        getNetData();
    }

    @OnClick({R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
        }
    }

    private void getNetData() {
        NetRequest.findHome(1, new BaseSubscriber<FindResponse>() {
            @Override
            public void onNext(FindResponse response) {
                authList.clear();
                topList.clear();
                List<AuthorInfo> list_ = response.authFeeds;
                if (list_ != null && list_.size() > 0) {
                    for (int i = 0; i < list_.size(); i++) {
                        authList.add(list_.get(i).content);
                    }
                }
                if (response.topicFeeds != null && response.topicFeeds.size() > 0) {
                    topList.addAll(response.topicFeeds);
                }
                findTopicAdapter.notifyDataSetChanged();
                followAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh();
            }
        });
    }
}
