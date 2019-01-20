package com.pipnet.wallenews.widgets;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pipnet.wallenews.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

/**
 * Created by LeeBoo on 2018/5/18.
 */

public class CarRefreshHeader extends LinearLayout implements RefreshHeader {

    private ImageView mProgressView;//刷新动画视图
    private AnimationDrawable mProgressDrawable;//刷新动画


    public CarRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public CarRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public CarRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        View v = LayoutInflater.from(context).inflate(R.layout.layout_progress, null);
        mProgressView = v.findViewById(R.id.refreshing_drawable_img);
        addView(v, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mProgressDrawable = (AnimationDrawable) mProgressView.getDrawable();
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int maxDragHeight) {
        mProgressDrawable.start();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mProgressDrawable.stop();//停止动画
//        if (success){
//            mHeaderText.setText("刷新完成");
//        } else {
//            mHeaderText.setText("刷新失败");
//        }
        return 0;//延迟500毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        switch (newState) {
//            case None:
//            case PullDownToRefresh:
//                mProgressView.setVisibility(VISIBLE);
//                break;
//            case Refreshing:
//                mProgressView.setVisibility(VISIBLE);
//                break;
//            case LoadFinish:
//                mProgressView.setVisibility(GONE);
//                break;
//        }
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

    }
}