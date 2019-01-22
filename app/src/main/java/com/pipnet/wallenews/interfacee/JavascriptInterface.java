package com.pipnet.wallenews.interfacee;

import android.content.Context;
import android.content.Intent;

import com.pipnet.wallenews.module.image.ImagePagerActivity;


/**
 * Created by LeeBoo on 2018/4/2.
 */

public class JavascriptInterface {
    private Context context;
    private String[] imageUrls;

    public JavascriptInterface(Context context, String[] imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        int position = 0;
        if (imageUrls != null && imageUrls.length > 0) {
            for (int i = 0; i < imageUrls.length; i++) {
                if (imageUrls[i].equals(img)) {
                    position = i;
                }
            }
        }
        Intent intent = new Intent();
        intent.putExtra(ImagePagerActivity.KEY_IMAGES, imageUrls);
        intent.putExtra(ImagePagerActivity.KEY_INDEX, position);
        intent.putExtra(ImagePagerActivity.KEY_FROM_NEWS, true);
        intent.setClass(context, ImagePagerActivity.class);
        context.startActivity(intent);
    }
}
