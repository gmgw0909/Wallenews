package com.pipnet.wallenews.module.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.pipnet.wallenews.App;
import com.pipnet.wallenews.R;

import java.util.HashMap;

public class ImagesAdapter extends PagerAdapter {
    private Activity activity;
    private String[] images;
    private HashMap<Integer, View> viewmap;
    private OnImageLongClickLisener lisener;

    public ImagesAdapter(Activity activity, String[] images, OnImageLongClickLisener lisener) {
        this.activity = activity;
        this.images = images;
        this.lisener = lisener;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0.equals(arg1);
    }

    @SuppressLint({"UseSparseArrays", "InflateParams"})
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        if (viewmap == null) {
            viewmap = new HashMap<Integer, View>();
            for (int i = 0; i < images.length; i++) {
                String imageUrl = images[i];
                PhotoView imageView = (PhotoView) LayoutInflater.from(activity).inflate(R.layout.layout_photoview, null);
                if (imageUrl.contains("'")) {
                    imageUrl = imageUrl.replace("'", "");
                }
                Glide.with(App.getInstance()).load(imageUrl).into(imageView);
                viewmap.put(i, imageView);
            }
        }
        View view = viewmap.get(position);
        if (view.getParent() == null)
            container.addView(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lisener.onImageLongClick(images[position]);
                return true;
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public static interface OnImageLongClickLisener {
        void onImageLongClick(String url);
    }
}
