package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.ContentBean;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.home.ReplyActivity;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class CommentAdapter extends BaseQuickAdapter<ContentBean, BaseViewHolder> {

    public CommentAdapter(@Nullable List<ContentBean> data) {
        super(R.layout.item_comment, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ContentBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (!TextUtils.isEmpty(item.authorImage)) {
            avatar.setImageURI(item.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        helper.setText(R.id.name, item.authorName);
        helper.setText(R.id.title, item.content);
        helper.setText(R.id.time, TimeUtil.intervalTime(item.createTime));
        TextView btnLike = helper.getView(R.id.btn_like);
        TextView btnComment = helper.getView(R.id.btn_comment);
        btnLike.setText(item.likeCount + "");
        if (item.ifLike) {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_dz_h);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_dz);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnLike.setCompoundDrawables(drawable, null, null, null);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetRequest.like(item.id + "", "content", !item.ifLike, new BaseSubscriber<Response>() {
                    @Override
                    public void onNext(Response response) {
                        if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                            item.ifLike = !item.ifLike;
                            if (item.ifLike) {
                                item.likeCount += 1;
                            } else {
                                item.likeCount -= 1;
                            }
                            notifyItemChanged(helper.getAdapterPosition());
                        }
                    }
                });
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ReplyActivity.class).putExtra("item", item));
            }
        });
    }
}
