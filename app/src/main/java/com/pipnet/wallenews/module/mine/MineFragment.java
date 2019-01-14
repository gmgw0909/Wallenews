package com.pipnet.wallenews.module.mine;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MineGridAdapter;
import com.pipnet.wallenews.base.LazyFragment;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.uihelpers.GridItemDecoration;
import com.pipnet.wallenews.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class MineFragment extends LazyFragment {

    @BindView(R.id.grid_rv)
    RecyclerView gridRv;

    List<LoginInfo.PropertiesBean> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void lazyLoad() {
        LoginInfo info = SPUtils.getObject(LoginInfo.class);
        list.addAll(info.properties);
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
}
