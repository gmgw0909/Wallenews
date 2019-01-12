package com.pipnet.wallenews.module.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by LeeBoo on 2019/1/12.
 */

public class MineFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        return view;
    }
}
