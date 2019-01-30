package com.pipnet.wallenews.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pipnet.wallenews.R;
import com.scwang.smartrefresh.layout.util.DensityUtil;

public class LoadingDialog extends Dialog {

    private AnimationDrawable mProgressDrawable;
    private Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.XLBDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        ImageView view = findViewById(R.id.refreshing_drawable_img);
        mProgressDrawable = (AnimationDrawable) view.getDrawable();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        mProgressDrawable.start();//开始动画
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProgressDrawable.stop();
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = DensityUtil.dp2px( 100);
        p.height = DensityUtil.dp2px( 100);
        getWindow().setAttributes(p);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}
