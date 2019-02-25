package com.pipnet.wallenews.module.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.App;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiHeaderAdapter;
import com.pipnet.wallenews.adapter.WaLiMultiAdapter;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.db.FeedsBeanDao;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class WaLiFragment extends LazyFragment implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;

    private long upCursor = 0;
    private long downCursor = 0;
    List<FeedsBean> list = new ArrayList<>();
    List<FeedResponse.TopTopicBean> topicBeans = new ArrayList<>();
    List<FeedsBean> feedsBeans = null;
    WaLiMultiAdapter adapter;
    WaLiHeaderAdapter headerAdapter;

    FeedsBeanDao feedsBeanDao;

    boolean scrollTop;

    @Override
    protected int setContentView() {
        return R.layout.fragment_wali;
    }

    @Override
    protected void lazyLoad() {
        EventBus.getDefault().register(this);
        feedsBeanDao = App.getInstance().getDaoSession().getFeedsBeanDao();
//        upCursor = SPUtils.getLong("upCursor", 0);
        title.setText("瓦砾");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        getUerInfo();
        initView(getActivity(), adapter = new WaLiMultiAdapter(list));
    }

    //网络获取用户信息
    private void getUerInfo() {
        NetRequest.mySpace(new BaseSubscriber<LoginInfo>() {
            @Override
            public void onNext(LoginInfo info) {
                if (!TextUtils.isEmpty(info.status) && info.status.equals("OK") && info.isLogged) {
                    //刷新用户信息
                    SPUtils.setObject(info);
                } else {
                    ToastUtil.show("登录失效,重新登录");
                }
            }
        });
    }

    private void getNetData(final String direction, final long cursor) {
        if (cursor == 0) {
            feedsBeans = feedsBeanDao.queryBuilder().orderDesc(FeedsBeanDao.Properties.Cursor).limit(20).list();
        } else {
            if (direction.equals("newFeed")) {
                feedsBeans = feedsBeanDao.queryBuilder().orderDesc(FeedsBeanDao.Properties.Cursor).where(FeedsBeanDao.Properties.Cursor.gt(cursor)).limit(20).list();
            } else {
                feedsBeans = feedsBeanDao.queryBuilder().orderDesc(FeedsBeanDao.Properties.Cursor).where(FeedsBeanDao.Properties.Cursor.lt(cursor)).limit(20).list();
            }
        }
        NetRequest.feeds(cursor, direction, new BaseSubscriber<FeedResponse>() {
            @Override
            public void onNext(FeedResponse followResponse) {
                //下拉刷新获取热门话题
                if (followResponse.topTopic != null && followResponse.topTopic.size() > 0 && direction.equals("newFeed")) {
                    topicBeans.clear();
                    topicBeans.addAll(followResponse.topTopic);
                    headerAdapter.notifyDataSetChanged();
                }
                if (feedsBeans != null && feedsBeans.size() > 0) {
                    list.addAll(feedsBeans);
                } else {
                    if (followResponse.feeds != null && followResponse.feeds.size() > 0) {
                        List<FeedsBean> list_ = followResponse.feeds;
                        if (direction.equals("newFeed")) {
                            list.clear();
                        }
                        feedsBeanDao.insertOrReplaceInTx(list_);
                        list.addAll(list_);
                    }
                }
                if (list.size() > 0 && direction.equals("newFeed")) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).show) {
                            list.get(i).show = false;
                        }
                    }
                }
                upCursor = list.get(0).cursor;
                downCursor = list.get(list.size() - 1).cursor;
                adapter.notifyDataSetChanged();
                adapter.loadMoreComplete();
                refreshLayout.finishRefresh();
            }
        });
    }
//    private void getNetData(final String direction, final long cursor) {
//        NetRequest.feeds(cursor, direction, new BaseSubscriber<FeedResponse>() {
//            @Override
//            public void onNext(FeedResponse followResponse) {
//                if (followResponse.topTopic != null && followResponse.topTopic.size() > 0 && direction.equals("newFeed")) {
//                    topicBeans.clear();
//                    topicBeans.addAll(followResponse.topTopic);
//                }
//                if (followResponse.feeds != null && followResponse.feeds.size() > 0) {
//                    List<FeedsBean> list_ = followResponse.feeds;
//                    downCursor = list_.get(list_.size() - 1).cursor;
//                    if (direction.equals("newFeed")) {
//                        upCursor = list_.get(0).cursor;
//                        SPUtils.setLong("upCursor", cursor);
//                        list.clear();
//                    }
//                    list.addAll(list_);
//                    adapter.notifyDataSetChanged();
//                    adapter.loadMoreComplete();
//                }
//                adapter.loadMoreComplete();
//                refreshLayout.finishRefresh();
//            }
//        });
//    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        getNetData("newFeed", upCursor);
    }

    @Override
    public void onLoadMoreRequested() {
        getNetData("oldFeed", downCursor);
    }

    private void initView(Context context, BaseQuickAdapter adapter) {
        //初始化头部
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_wali, null);
        RecyclerView headerRV = header.findViewById(R.id.recycler_header);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        headerRV.setLayoutManager(layoutManager);
        headerRV.setAdapter(headerAdapter = new WaLiHeaderAdapter(topicBeans));
        adapter.addHeaderView(header);
        //初始化Feeds列表
        refreshLayout.setRefreshHeader(new CarRefreshHeader(context));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.line_light))
                .build(recyclerView));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setEmptyView(R.layout.layout_empty);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    scrollTop = true;
                } else {
                    scrollTop = false;
                }
            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (list.get(position).type.equals("forward")) {
            startActivity(new Intent(getActivity(), ForwardDetailActivity.class).putExtra("FORWARD_CONTENT", list.get(position).content));
        } else {
            view.findViewById(R.id.btn_topic).performClick();
            startActivity(new Intent(getActivity(), FeedDetailActivity.class)
                    .putExtra("FEED_ID", list.get(position).content.id)
                    .putExtra("FEED_CURSOR", list.get(position).cursor));
        }
        list.get(position).isRead = true;
        adapter.notifyDataSetChanged();
        feedsBeanDao.insertOrReplace(list.get(position));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        if (event.contains(Constants.COMMENT_SUCCESS)) {
            for (int i = 0; i < list.size(); i++) {
                FeedsBean feedsBean = list.get(i);
                if (feedsBean.content.id == Long.parseLong(event.replace(Constants.COMMENT_SUCCESS, ""))) {
                    feedsBean.content.commentCount += 1;
                    App.getInstance().getDaoSession().getFeedsBeanDao().update(feedsBean);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (event.contains(Constants.FORWARD_SUCCESS)) {
            for (int i = 0; i < list.size(); i++) {
                FeedsBean feedsBean = list.get(i);
                if (feedsBean.content.id == Long.parseLong(event.replace(Constants.FORWARD_SUCCESS, ""))) {
                    feedsBean.content.forwardCount += 1;
                    feedsBean.content.ifForward = true;
                    App.getInstance().getDaoSession().getFeedsBeanDao().update(feedsBean);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (event.contains(Constants.LIKED_SUCCESS)) {
            for (int i = 0; i < list.size(); i++) {
                FeedsBean feedsBean = list.get(i);
                if (feedsBean.content.id == Long.parseLong(event.split(",")[2])) {
                    if (event.split(",")[1].equals("true")) {
                        feedsBean.content.likeCount += 1;
                        feedsBean.content.ifLike = true;
                    } else {
                        feedsBean.content.likeCount -= 1;
                        feedsBean.content.ifLike = false;
                    }
                    App.getInstance().getDaoSession().getFeedsBeanDao().update(feedsBean);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        } else if (event.contains(Constants.HOME_REFRESH)) {
            if (!isHidden()) {
                if (scrollTop) {
                    refreshLayout.autoRefresh();
                } else {
                    recyclerView.smoothScrollToPosition(0);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
