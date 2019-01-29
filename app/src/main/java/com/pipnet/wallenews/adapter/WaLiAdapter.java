package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.home.ForwardActivity;
import com.pipnet.wallenews.module.home.ReplyActivity;
import com.pipnet.wallenews.module.mine.UserDetailActivity;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class WaLiAdapter extends BaseQuickAdapter<FeedResponse.FeedsBean, BaseViewHolder> {

    public WaLiAdapter(@Nullable List<FeedResponse.FeedsBean> data) {
        super(R.layout.item_wali, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedResponse.FeedsBean item) {
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
        final TextView btnTopic = helper.getView(R.id.btn_topic);
        final RecyclerView headerRV = helper.getView(R.id.recycler_header);
        if (!TextUtils.isEmpty(item.content.authorImage)) {
            avatar.setImageURI(item.content.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.content.authorName);
        helper.setText(R.id.more_name, item.content.authorName);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
        helper.setText(R.id.title, item.content.title);
        btnComment.setText(item.content.commentCount + "");
        btnForward.setText(item.content.forwardCount + "");
        btnLike.setText(item.content.likeCount + "");
        if (item.content.ifForward) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_zf_h);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnForward.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_zf);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnForward.setCompoundDrawables(drawable, null, null, null);
        }
        if (item.content.ifLike) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_dz_h);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_dz);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnLike.setCompoundDrawables(drawable, null, null, null);
        }
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, UserDetailActivity.class));
            }
        });
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequest.like(item.content.id + "", "content", !item.content.ifLike, new BaseSubscriber<Response>() {
                    @Override
                    public void onNext(Response response) {
                        if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                            item.content.ifLike = !item.content.ifLike;
                            if (item.content.ifLike) {
                                item.content.likeCount += 1;
                            } else {
                                item.content.likeCount -= 1;
                            }
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ReplyActivity.class).putExtra("item", item.content));
            }
        });
        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ForwardActivity.class).putExtra("item", item.content));
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
        if (item.content.hasRecommend) {
            btnTopic.setVisibility(View.VISIBLE);
            btnTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.show) {
                        item.show = false;
                        notifyDataSetChanged();
                    } else {
                        item.show = true;
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            btnTopic.setVisibility(View.INVISIBLE);
        }
        if (item.show) {
            llMore.setVisibility(View.VISIBLE);
            //关联话题
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
            headerRV.setLayoutManager(layoutManager);
            NetRequest.recommends(item.content.id, new BaseSubscriber<FeedDetailsInfo>() {
                @Override
                public void onNext(FeedDetailsInfo info) {
                    List<FeedResponse.TopTopicBean> data = new ArrayList<>();
                    if (info.topics != null) {
                        for (int i = 0; i < info.topics.size(); i++) {
                            data.add(new FeedResponse.TopTopicBean(info.topics.get(i), ""));
                        }
                        headerRV.setAdapter(new WaLiHeaderAdapter(data));
                    }
                }
            });
        } else {
            llMore.setVisibility(View.GONE);
            headerRV.setAdapter(null);
        }
    }
}
