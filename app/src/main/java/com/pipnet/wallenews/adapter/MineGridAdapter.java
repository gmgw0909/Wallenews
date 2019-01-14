package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.module.WebViewActivity;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class MineGridAdapter extends BaseQuickAdapter<LoginInfo.PropertiesBean, BaseViewHolder> {

    public MineGridAdapter(@Nullable List<LoginInfo.PropertiesBean> data) {
        super(R.layout.item_mine_grid, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LoginInfo.PropertiesBean item) {
        helper.setText(R.id.tv, item.name);
        SimpleDraweeView img = helper.getView(R.id.iv);
        img.setImageURI(item.image);
        helper.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.name)) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra(WebViewActivity.KEY_TITLE, item.name)
                            .putExtra(WebViewActivity.KEY_URL, item.url));
                }
            }
        });
    }
}
