package com.pipnet.wallenews.module.find;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.FindTopicAdapter;
import com.pipnet.wallenews.adapter.FollowAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.response.Response;
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

public class FindFragment extends LazyFragment implements OnRefreshListener {

    RecyclerView recyclerTopic;

    @BindView(R.id.recycler_follow)
    RecyclerView recyclerFollow;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    FollowAdapter followAdapter;
    FindTopicAdapter findTopicAdapter;

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
        refreshLayout.autoRefresh();

        List<FollowResponse.Feeds> l = new ArrayList();
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        List<Response> li = new ArrayList();
        li.add(new Response());
        li.add(new Response());
        li.add(new Response());
        li.add(new Response());
        recyclerFollow.setAdapter(followAdapter = new FollowAdapter(l));
        recyclerTopic.setAdapter(findTopicAdapter = new FindTopicAdapter(li));
        followAdapter.addHeaderView(header);
        followAdapter.addFooterView(footer);
    }

    @Override
    public void onRefresh(final RefreshLayout refreshlayout) {

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshlayout.finishRefresh();
            }
        }, 1000);
    }
}
