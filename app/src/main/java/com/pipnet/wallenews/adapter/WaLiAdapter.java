package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.module.mine.UserDetailActivity;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class WaLiAdapter extends BaseQuickAdapter<FeedResponse.FeedsBean, BaseViewHolder> {

    public WaLiAdapter(@Nullable List<FeedResponse.FeedsBean> data) {
        super(R.layout.item_wali, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FeedResponse.FeedsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        final LinearLayout llImg = helper.getView(R.id.ll_img);
        LinearLayout ll3 = helper.getView(R.id.ll_3);
        final LinearLayout llMore = helper.getView(R.id.ll_more);
        SimpleDraweeView img1_1 = helper.getView(R.id.img1_1);
        SimpleDraweeView img23_1 = helper.getView(R.id.img23_1);
        SimpleDraweeView img2_2 = helper.getView(R.id.img2_2);
        SimpleDraweeView img3_2 = helper.getView(R.id.img3_2);
        SimpleDraweeView img3_3 = helper.getView(R.id.img3_3);
        TextView btnComment = helper.getView(R.id.btn_comment);
        TextView btnForward = helper.getView(R.id.btn_forward);
        TextView btnLike = helper.getView(R.id.btn_like);
        TextView btnTopic = helper.getView(R.id.btn_topic);
        RecyclerView headerRV = helper.getView(R.id.recycler_header);
        if (!TextUtils.isEmpty(item.content.authorImage)) {
            avatar.setImageURI(item.content.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.content.authorName);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
        helper.setText(R.id.title, item.content.title);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailActivity.class));
            }
        });

        if (item.content.imageArray != null && item.content.imageArray.size() > 0) {
            llImg.setVisibility(View.VISIBLE);
            List<String> images = item.content.imageArray;
            if (images.size() == 1) {
                img1_1.setVisibility(View.VISIBLE);
                img23_1.setVisibility(View.GONE);
                img2_2.setVisibility(View.GONE);
                ll3.setVisibility(View.GONE);
                img3_2.setVisibility(View.GONE);
                img3_3.setVisibility(View.GONE);
                img1_1.setImageURI(images.get(0));
            } else if (images.size() == 2) {
                img1_1.setVisibility(View.GONE);
                img23_1.setVisibility(View.VISIBLE);
                img2_2.setVisibility(View.VISIBLE);
                ll3.setVisibility(View.GONE);
                img3_2.setVisibility(View.GONE);
                img3_3.setVisibility(View.GONE);
                img23_1.setImageURI(images.get(0));
                img2_2.setImageURI(images.get(1));
            } else if (images.size() == 3) {
                img1_1.setVisibility(View.GONE);
                img23_1.setVisibility(View.VISIBLE);
                img2_2.setVisibility(View.GONE);
                ll3.setVisibility(View.VISIBLE);
                img3_2.setVisibility(View.VISIBLE);
                img3_3.setVisibility(View.VISIBLE);
                img23_1.setImageURI(images.get(0));
                img3_2.setImageURI(images.get(1));
                img3_3.setImageURI(images.get(2));
            }
        } else {
            llImg.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        headerRV.setLayoutManager(layoutManager);
//        headerRV.setAdapter(new WaLiHeaderAdapter(item.content));
        btnTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llMore.getVisibility() == View.GONE) {
                    llMore.setVisibility(View.VISIBLE);
                } else {
                    llMore.setVisibility(View.GONE);
                }
            }
        });
    }

}
