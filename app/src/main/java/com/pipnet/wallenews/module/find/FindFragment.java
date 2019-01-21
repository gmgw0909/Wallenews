package com.pipnet.wallenews.module.find;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.FollowAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FollowResponse;
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

    @BindView(R.id.recycler_follow)
    RecyclerView recyclerFollow;
    @BindView(R.id.recycler_topic)
    RecyclerView recyclerTopic;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    FollowAdapter followAdapter;
    FollowAdapter followAdapter1;
    List<FollowResponse.Feeds> l = new ArrayList();

    @Override
    protected int setContentView() {
        return R.layout.fragment_find;
    }

    @Override
    protected void lazyLoad() {
        initView();
    }

    private void initView() {
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
        recyclerFollow.setAdapter(followAdapter = new FollowAdapter(l));
        recyclerTopic.setAdapter(followAdapter1 = new FollowAdapter(l));
        refreshLayout.autoRefresh();
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        l.add(new FollowResponse.Feeds());
        followAdapter.notifyDataSetChanged();
        followAdapter1.notifyDataSetChanged();
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
