package com.pipnet.wallenews.module.image;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.pipnet.wallenews.R;


/**
 * Created by circle on 2017/9/18.
 */

public class ImageDownPopwindow extends PopupWindow {
    private Activity mActivity;

    public ImageDownPopwindow(Activity activity, final String imageUrl, final OnImageDownClickListener listener) {
        mActivity = activity;
        final View rootView = View.inflate(activity, R.layout.layout_imagedown_popwindow, null);
        setContentView(rootView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundAlpha(0.5f);
        setBackgroundDrawable(new BitmapDrawable());
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getY() < rootView.findViewById(R.id.llPop).getTop()) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        rootView.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        rootView.findViewById(R.id.tvSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDownloadImage(imageUrl);
                dismiss();
            }
        });


    }

    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams layoutParams = mActivity.getWindow().getAttributes();
        layoutParams.alpha = alpha;
        mActivity.getWindow().setAttributes(layoutParams);
    }

    public static interface OnImageDownClickListener {

        void onDownloadImage(String imageUrl);
    }

}