package com.pipnet.wallenews.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by LeeBoo on 2019/1/11.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(this);
        initViewData();
        ActivityController.addActivity(this);
    }

    public abstract int setContentView();

    public abstract void initViewData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
