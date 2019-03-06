package com.pipnet.wallenews.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.pipnet.wallenews.App;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.FeedsBean;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.home.FeedDetailActivity;
import com.pipnet.wallenews.module.home.ForwardActivity;
import com.pipnet.wallenews.module.home.ReplyActivity;
import com.pipnet.wallenews.module.mine.UserDetailActivity;
import com.pipnet.wallenews.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by LeeBoo on 2019/1/13.
 */

public class WaLiMultiAdapter extends BaseMultiItemQuickAdapter<FeedsBean, BaseViewHolder> {

    public WaLiMultiAdapter(@Nullable List<FeedsBean> data) {
        super(data);
        addItemType(0, R.layout.item_wali);
        addItemType(1, R.layout.item_wali_forward);
        addItemType(2, R.layout.item_wali_video);
    }

    public BaseViewHolder getHolder(View view) {
        return createBaseViewHolder(view);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FeedsBean item) {
        SimpleDraweeView avatar = helper.getView(R.id.avatar);
        final LinearLayout llImg = helper.getView(R.id.ll_img);
        LinearLayout ll3 = helper.getView(R.id.ll_3);
        SimpleDraweeView img1_1 = helper.getView(R.id.img1_1);
        SimpleDraweeView img23_1 = helper.getView(R.id.img23_1);
        SimpleDraweeView img2_2 = helper.getView(R.id.img2_2);
        SimpleDraweeView img3_2 = helper.getView(R.id.img3_2);
        SimpleDraweeView img3_3 = helper.getView(R.id.img3_3);
        TextView btnComment = helper.getView(R.id.btn_comment);
        TextView btnForward = helper.getView(R.id.btn_forward);
        TextView btnLike = helper.getView(R.id.btn_like);
        int itemType = helper.getItemViewType();
        switch (itemType) {
            case 0:
                final LinearLayout llMore = helper.getView(R.id.ll_more);
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
                        mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra("authorId", item.content.authorId));
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
                                    App.getInstance().getDaoSession().getFeedsBeanDao().update(item);
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
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).show && mData.get(i).cursor != item.cursor) {
                                    mData.get(i).show = false;
                                }
                            }
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
                    btnTopic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).show && mData.get(i).cursor != item.cursor) {
                                    mData.get(i).show = false;
                                }
                            }
                            notifyDataSetChanged();
                        }
                    });
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
                if (item.isRead) {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                    ((TextView) helper.getView(R.id.title)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                } else {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.black));
                    ((TextView) helper.getView(R.id.title)).setTextColor(mContext.getResources().getColor(R.color.text_333));
                }
                break;
            case 1:
                if (!TextUtils.isEmpty(item.content.authorImage)) {
                    avatar.setImageURI(item.content.authorImage);
                } else {
                    avatar.setImageResource(R.mipmap.default_avatar);
                }
                helper.setText(R.id.name, item.content.authorName);
                helper.setText(R.id.time, TimeUtil.intervalTime(item.content.createTime));
                helper.setText(R.id.content, item.content.content);
                helper.setText(R.id.sourceAuthorName, item.content.sourceAuthorName);
                helper.setText(R.id.sourceContentTitle, item.content.sourceContentTitle);
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
                        mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra("authorId", item.content.authorId));
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

                if (item.content.sourceContentImageAry != null && item.content.sourceContentImageAry.size() > 0) {
                    llImg.setVisibility(View.VISIBLE);
                    List<String> images = item.content.sourceContentImageAry;
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
                helper.getView(R.id.ll_forward).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, FeedDetailActivity.class).putExtra("FEED_ID", item.content.sourceId));
                    }
                });
                if (item.isRead) {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                    ((TextView) helper.getView(R.id.content)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                    ((TextView) helper.getView(R.id.sourceAuthorName)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                    ((TextView) helper.getView(R.id.sourceContentTitle)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                } else {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.black));
                    ((TextView) helper.getView(R.id.content)).setTextColor(mContext.getResources().getColor(R.color.text_333));
                    ((TextView) helper.getView(R.id.sourceAuthorName)).setTextColor(mContext.getResources().getColor(R.color.black));
                    ((TextView) helper.getView(R.id.sourceContentTitle)).setTextColor(mContext.getResources().getColor(R.color.text_333));
                }
                break;

            case 2:
                final LinearLayout llMore_ = helper.getView(R.id.ll_more);
                final TextView btnTopic_ = helper.getView(R.id.btn_topic);
                final RecyclerView headerRV_ = helper.getView(R.id.recycler_header);
                JzvdStd jzvdStd = helper.getView(R.id.video_view);
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
                        mContext.startActivity(new Intent(mContext, UserDetailActivity.class).putExtra("authorId", item.content.authorId));
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
                                    App.getInstance().getDaoSession().getFeedsBeanDao().update(item);
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

                if (!TextUtils.isEmpty(item.content.video)) {
                    jzvdStd.setUp(item.content.video, "", Jzvd.SCREEN_WINDOW_LIST);
                    jzvdStd.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    Glide.with(mContext).load(item.content.imageArray.get(0)).into(jzvdStd.thumbImageView);
                }
                if (item.content.hasRecommend) {
                    btnTopic_.setVisibility(View.VISIBLE);
                    btnTopic_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).show && mData.get(i).cursor != item.cursor) {
                                    mData.get(i).show = false;
                                }
                            }
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
                    btnTopic_.setVisibility(View.INVISIBLE);
                    btnTopic_.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (mData.get(i).show && mData.get(i).cursor != item.cursor) {
                                    mData.get(i).show = false;
                                }
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
                if (item.show) {
                    llMore_.setVisibility(View.VISIBLE);
                    //关联话题
                    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
                    layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
                    headerRV_.setLayoutManager(layoutManager);
                    NetRequest.recommends(item.content.id, new BaseSubscriber<FeedDetailsInfo>() {
                        @Override
                        public void onNext(FeedDetailsInfo info) {
                            List<FeedResponse.TopTopicBean> data = new ArrayList<>();
                            if (info.topics != null) {
                                for (int i = 0; i < info.topics.size(); i++) {
                                    data.add(new FeedResponse.TopTopicBean(info.topics.get(i), ""));
                                }
                                headerRV_.setAdapter(new WaLiHeaderAdapter(data));
                            }
                        }
                    });
                } else {
                    llMore_.setVisibility(View.GONE);
                    headerRV_.setAdapter(null);
                }
                if (item.isRead) {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                    ((TextView) helper.getView(R.id.title)).setTextColor(mContext.getResources().getColor(R.color.text_999));
                } else {
                    ((TextView) helper.getView(R.id.name)).setTextColor(mContext.getResources().getColor(R.color.black));
                    ((TextView) helper.getView(R.id.title)).setTextColor(mContext.getResources().getColor(R.color.text_333));
                }
                break;
        }
    }
}
