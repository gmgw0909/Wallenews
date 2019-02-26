package com.pipnet.wallenews.module.mine;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MineGridAdapter;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.uihelpers.GridItemDecoration;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class MineFragment extends LazyFragment {

    @BindView(R.id.grid_rv)
    RecyclerView gridRv;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.followCount)
    TextView followCount;
    @BindView(R.id.followedCount)
    TextView followedCount;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    MineGridAdapter adapter;

    List<LoginInfo.PropertiesBean> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {
        EventBus.getDefault().register(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                .setHorizontalSpan(2f)
                .setVerticalSpan(2f)
                .setColorResource(R.color.line_light)
                .setShowLastLine(false)
                .build();
        gridRv.addItemDecoration(divider);
        gridRv.setLayoutManager(gridLayoutManager);
        gridRv.setAdapter(adapter = new MineGridAdapter(list));
        getUerInfo();
    }

    //网络获取用户信息
    private void getUerInfo() {
        NetRequest.mySpace(new BaseSubscriber<LoginInfo>() {
            @Override
            public void onNext(LoginInfo info) {
                if (!TextUtils.isEmpty(info.status) && info.status.equals("OK") && info.isLogged) {
                    //刷新用户信息
                    SPUtils.setObject(info);
                    initUserInfo(info);
                    if (info.properties != null) {
                        list.addAll(info.properties);
                        for (int i = 0; i < 9 - info.properties.size(); i++) {
                            list.add(new LoginInfo.PropertiesBean());
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtil.show("登录失效,重新登录");
                }
            }
        });
    }

    private void initUserInfo(LoginInfo info) {
        if (!TextUtils.isEmpty(info.nickName)) {
            name.setText(info.nickName);
        }
        followCount.setText(info.followCount + "");
        followedCount.setText(info.followedCount + "");
        if (!TextUtils.isEmpty(info.avatar)) {
            avatar.setImageURI(info.avatar);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
    }

    @OnClick({R.id.btn_setting, R.id.name, R.id.ll_follow, R.id.ll_followed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.name:
                startActivity(new Intent(getActivity(), EditActivity.class));
                break;
            case R.id.ll_follow:
                startActivity(new Intent(getActivity(), FollowingActivity.class));
                break;
            case R.id.ll_followed:
                startActivity(new Intent(getActivity(), FollowerActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        if (event.equals(Constants.REFRESH_USER)) {
            initUserInfo(SPUtils.getObject(LoginInfo.class));
        } else if (event.equals(Constants.REFRESH_USER_NET)) {
            if (!isHidden())
                getUerInfo();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
