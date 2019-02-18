package com.pipnet.wallenews.module.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.home.FeedDetailActivity;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WaPFragment extends LazyFragment implements  BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    WaLiAdapter adapter;
    List<FeedsBean> list = new ArrayList<>();

    boolean isInit = false;

    @Override
    protected int setContentView() {
        return R.layout.fragment_wa_pian;
    }

    public static WaPFragment newInstance(long id, String feedType) {
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("feedType", feedType);
        WaPFragment fragment = new WaPFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (!isInit) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                    .dividerColor(getActivity().getResources().getColor(R.color.line_light))
                    .build(recyclerView));
            recyclerView.setAdapter(adapter = new WaLiAdapter(list));
            getNetData();
            isInit = true;
        }
    }

    private void getNetData() {
        NetRequest.authorDetail(getArguments().getLong("id"), getArguments().getString("feedType"), new BaseSubscriber<FeedResponse>() {
            @Override
            public void onNext(FeedResponse followResponse) {
                if (followResponse.feeds != null && followResponse.feeds.size() > 0) {
                    List<FeedsBean> list_ = followResponse.feeds;
                    list.addAll(list_);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        view.findViewById(R.id.btn_topic).performClick();
        startActivity(new Intent(getActivity(), FeedDetailActivity.class).putExtra("FEED_ID", list.get(position).content.id));
    }
}
