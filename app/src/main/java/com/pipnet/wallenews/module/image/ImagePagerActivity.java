package com.pipnet.wallenews.module.image;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.util.DisplayUtil;
import com.pipnet.wallenews.util.DownloadManager;
import com.pipnet.wallenews.util.ToastUtil;
import com.pipnet.wallenews.widgets.HackyViewPager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 图片列表
 * Created by circle on 2017/9/27.
 */

public class ImagePagerActivity extends BaseActivity implements ImagesAdapter.OnImageLongClickLisener {
    public static final String KEY_IMAGES = "images";
    public static final String KEY_INDEX = "KEY_INDEX";
    public static final String KEY_FROM_NEWS = "KEY_FROM_NEWS";
    @BindView(R.id.relItem)
    RelativeLayout relItem;
    @BindView(R.id.viewPager)
    HackyViewPager viewPager;
    @BindView(R.id.llIndicator)
    LinearLayout llIndicator;
    private String[] images;
    private List<ImageView> indicator = new ArrayList<>();
    private int currentPosition = 0;

    @Override
    public int setContentView() {
        return R.layout.activity_images;
    }

    @Override
    public void initViewData() {
        images = getIntent().getStringArrayExtra(KEY_IMAGES);
        if (images == null || images.length == 0) {
            finish();
            return;
        }


        currentPosition = getIntent().getIntExtra(KEY_INDEX, 0);
        initIndicator();
//        tv_page.setText((index+1)+"/" + images.length);
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        viewPager.setAdapter(new ImagesAdapter(this, images, this));
        viewPager.setCurrentItem(currentPosition);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentPosition = arg0;
                setIndicator();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void setIndicator() {
        for (int i = 0; i < indicator.size(); i++) {
            indicator.get(i).setImageResource(i == currentPosition ? R.drawable.indicator_circle_select : R.drawable.indicator_circle_normal);
        }
    }

    private void initIndicator() {
        int w = DisplayUtil.dip2px(5);
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = w;
            layoutParams.rightMargin = w;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(i == currentPosition ? R.drawable.indicator_circle_select : R.drawable.indicator_circle_normal);
            llIndicator.addView(imageView);
            indicator.add(imageView);
        }
    }

    @Override
    public void onImageLongClick(String url) {
        if (TextUtils.isEmpty(url)) return;
        new ImageDownPopwindow(this, url, new ImageDownPopwindow.OnImageDownClickListener() {
            @Override
            public void onDownloadImage(String imageUrl) {
                downLoadCheckPermission(imageUrl);
            }
        }).showAtLocation(relItem, Gravity.BOTTOM, 0, 0);
    }

    private void downLoadCheckPermission(final String imageUrl) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            new DownloadManager(imageUrl, new DownloadManager.DownloadListener() {
                                @Override
                                public void downloadResult(int state, File file) {
                                    //下载完成
                                    if (state == DownloadManager.DOWNLOAD_SUCC) {
                                        ToastUtil.show("下载成功:" + file.getAbsolutePath());
                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri uri = Uri.fromFile(file);
                                        intent.setData(uri);
                                        ImagePagerActivity.this.sendBroadcast(intent);
                                    } else {
                                        ToastUtil.show("下载失败");
                                    }
                                }
                            });
                        } else {
                            ToastUtil.show("请打开读写相关权限");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        indicator.clear();
        llIndicator.removeAllViews();
        viewPager.removeAllViews();
    }

    class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // 页面远离左侧页面
                page.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // 页面在由中间页滑动到左侧页面 或者 由左侧页面滑动到中间页
                page.setAlpha(1);
                page.setTranslationX(0);
                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                //页面在由中间页滑动到右侧页面 或者 由右侧页面滑动到中间页
                // 淡出效果
                page.setAlpha(1 - position);
                // 反方向移动
                page.setTranslationX(pageWidth * -position);
                // 0.75-1比例之间缩放
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
            } else { // (1,+Infinity]
                // 页面远离右侧页面
                page.setAlpha(0);
            }

        }
    }
}
