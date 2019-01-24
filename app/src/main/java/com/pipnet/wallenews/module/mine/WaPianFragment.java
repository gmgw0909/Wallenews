package com.pipnet.wallenews.module.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.ToastUtil;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WaPianFragment extends LazyFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    WaLiAdapter adapter;
    List<FeedResponse.FeedsBean> list = new ArrayList<>();

    boolean isInit = false;

    @Override
    protected int setContentView() {
        return R.layout.fragment_wa_pian;
    }

    @Override
    protected void lazyLoad() {
        if (!isInit) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                    .dividerColor(getActivity().getResources().getColor(R.color.line_light))
                    .build(recyclerView));
            recyclerView.setAdapter(adapter = new WaLiAdapter(list));
            getNetData("newFeed", 0);
            ToastUtil.show("loading...");
            isInit = true;
        }
    }

    private void getNetData(final String direction, final long cursor) {
        NetRequest.feeds(cursor, direction, new BaseSubscriber<FeedResponse>() {
            @Override
            public void onNext(FeedResponse followResponse) {
                if (followResponse.feeds != null && followResponse.feeds.size() > 0) {
                    List<FeedResponse.FeedsBean> list_ = followResponse.feeds;
                    list.addAll(list_);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
