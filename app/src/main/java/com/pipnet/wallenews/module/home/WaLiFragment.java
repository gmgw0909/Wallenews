package com.pipnet.wallenews.module.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.WaLiAdapter;
import com.pipnet.wallenews.adapter.WaLiHeaderAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.PageList;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.uihelpers.IRefreshPage;
import com.pipnet.wallenews.uihelpers.RefreshLoadMoreHelper;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class WaLiFragment extends LazyFragment implements IRefreshPage, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler_article)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_left)
    TextView btnLeft;

    private RefreshLoadMoreHelper<Response> refreshLoadMoreHelper;

    @Override
    protected int setContentView() {
        return R.layout.fragment_wali;
    }

    @Override
    protected void lazyLoad() {
        title.setText("瓦砾");
        btnLeft.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        getUerInfo();
        List<Response> list = new ArrayList<>();
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.header_wali, null);
        RecyclerView headerRV = header.findViewById(R.id.recycler_header);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        headerRV.setLayoutManager(layoutManager);
        headerRV.setAdapter(new WaLiHeaderAdapter(list));
        refreshLoadMoreHelper = new RefreshLoadMoreHelper<>(this, refreshLayout, recyclerView, WaLiAdapter.class);
        refreshLoadMoreHelper.getAdapter().addHeaderView(header);
        refreshLoadMoreHelper.setOnItemClickListener(this);
        refreshLoadMoreHelper.autoRefresh();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void loadData() {
        List<Response> list = new ArrayList<>();
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        list.add(new Response());
        PageList pageList = new PageList();
        pageList.setData(list);
        refreshLoadMoreHelper.loadSuccess(pageList);
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
}
