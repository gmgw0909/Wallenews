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
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.uihelpers.GridItemDecoration;
import com.pipnet.wallenews.util.SPUtils;

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

    List<LoginInfo.PropertiesBean> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {
        LoginInfo info = SPUtils.getObject(LoginInfo.class);
        list.addAll(info.properties);
        initUserInfo(info);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                .setHorizontalSpan(2f)
                .setVerticalSpan(2f)
                .setColorResource(R.color.line_light)
                .setShowLastLine(false)
                .build();
        gridRv.addItemDecoration(divider);
        gridRv.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 9 - info.properties.size(); i++) {
            list.add(new LoginInfo.PropertiesBean());
        }
        gridRv.setAdapter(new MineGridAdapter(list));
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

    @OnClick({R.id.btn_setting, R.id.name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.name:
                startActivity(new Intent(getActivity(), EditActivity.class));
                break;
        }
    }
}
