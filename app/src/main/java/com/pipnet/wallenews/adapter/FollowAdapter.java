package com.pipnet.wallenews.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FollowResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;

import java.util.List;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class FollowAdapter extends BaseQuickAdapter<FollowResponse.Feeds, BaseViewHolder> {

    public FollowAdapter(@Nullable List<FollowResponse.Feeds> data) {
        super(R.layout.item_follow, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FollowResponse.Feeds item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        if (item.content != null && item.content.size() > 0) {
            if (!TextUtils.isEmpty(item.content.get(0).image)) {
                avatar.setImageURI(item.content.get(0).image);
            } else {
                avatar.setImageResource(R.mipmap.default_avatar);
            }
            helper.setText(R.id.name, item.content.get(0).name);
            helper.setText(R.id.intro, item.content.get(0).introduction);
            TextView follow = helper.getView(R.id.btn_follow);
            if (item.content.get(0).ifFollowed) {
                follow.setText("正在关注");
                follow.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.white, null));
                follow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_btn_follow_s));
            } else {
                follow.setText("关注");
                follow.setTextColor(ResourcesCompat.getColor(mContext.getResources(), R.color.text_tab_blue, null));
                follow.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_btn_follow));
            }
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followOrUnFollow(item, !item.content.get(0).ifFollowed);
                }
            });
        }
    }

    //取消关注或者关注
    private void followOrUnFollow(final FollowResponse.Feeds item, boolean follow) {
        NetRequest.follow(item.content.get(0).id + "", follow, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    int i = mData.indexOf(item);
                    mData.remove(item);
                    item.content.get(0).ifFollowed = !item.content.get(0).ifFollowed;
                    mData.add(i, item);
                    notifyDataSetChanged();
                }
            }
        });
    }
}
