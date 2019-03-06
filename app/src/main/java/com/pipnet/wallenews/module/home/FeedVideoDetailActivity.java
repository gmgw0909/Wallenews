package com.pipnet.wallenews.module.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.CommentAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.ContentBean;
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.mine.UserDetailActivity;
import com.pipnet.wallenews.util.TimeUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class FeedVideoDetailActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_dz)
    ImageView ivDz;
    @BindView(R.id.iv_zf)
    ImageView ivZf;
    @BindView(R.id.btn_right)
    TextView btnFollow;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.video_view)
    JzvdStd videoView;

    ContentBean contentBean;
    long authorId = 0;
    int page = 1;
    List<ContentBean> list = new ArrayList<>();
    CommentAdapter adapter;
    boolean ifFollowed;

    @Override
    public int setContentView() {
        return R.layout.activity_feed_video_detail;
    }

    @Override
    public void initViewData() {
        EventBus.getDefault().register(this);
        contentBean = (ContentBean) getIntent().getSerializableExtra("FORWARD_CONTENT");
        adapter = new CommentAdapter(list);
        recyclerComment.setLayoutManager(new LinearLayoutManager(FeedVideoDetailActivity.this));
        recyclerComment.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getResources().getColor(R.color.line_light))
                .build(recyclerComment));
        recyclerComment.setAdapter(adapter);
        getComments(page);
        getDetail();
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            //发送请求
                            String comment = etComment.getText().toString();
                            if (!TextUtils.isEmpty(comment)) {
                                NetRequest.reply(contentBean.id + "", comment, "", "",
                                        "content", new BaseSubscriber<Response>() {
                                            @Override
                                            public void onNext(Response response) {
                                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                                    etComment.setText("");
                                                    hintKeyBoard();
                                                    EventBus.getDefault().post(Constants.COMMENT_SUCCESS + contentBean.id);
                                                    getComments(page);
                                                }
                                            }
                                        });
                            }
                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_right, R.id.avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                followOrUnFollow();
                break;
            case R.id.avatar:
                startActivity(new Intent(FeedVideoDetailActivity.this, UserDetailActivity.class).putExtra("authorId", authorId));
                break;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        getComments(page);
//        getDetail();
    }

    private void getComments(final int page) {
        NetRequest.replies(contentBean.id, page, new BaseSubscriber<RepliesResponse>() {
            @Override
            public void onNext(RepliesResponse repliesResponse) {
                if (repliesResponse.replies != null && repliesResponse.replies.size() > 0) {
                    List<ContentBean> list_ = repliesResponse.replies;
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(list_);
                    adapter.notifyDataSetChanged();
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
//                refreshLayout.finishRefresh();
            }
        });
    }

    private void getDetail() {
        NetRequest.detail(contentBean.id, new BaseSubscriber<FeedDetailsInfo>() {
            @Override
            public void onNext(final FeedDetailsInfo feedDetailsInfo) {
                name.setText(feedDetailsInfo.content.authorName);
                videoView.setUp(feedDetailsInfo.content.video, "", Jzvd.SCREEN_WINDOW_LIST);
                videoView.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(FeedVideoDetailActivity.this).load(feedDetailsInfo.content.imageArray.get(0)).into(videoView.thumbImageView);
                videoView.startVideo();
                if (!TextUtils.isEmpty(feedDetailsInfo.content.authorImage)) {
                    avatar.setImageURI(feedDetailsInfo.content.authorImage);
                }
                if (feedDetailsInfo.content.ifForward) {
                    ivZf.setImageResource(R.mipmap.icon_xq_zf_h);
                } else {
                    ivZf.setImageResource(R.mipmap.icon_xq_zf);
                }
                if (feedDetailsInfo.content.ifLike) {
                    ivDz.setImageResource(R.mipmap.icon_xq_dz_h);
                } else {
                    ivDz.setImageResource(R.mipmap.icon_xq_dz);
                }
                ivZf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(FeedVideoDetailActivity.this, ForwardActivity.class).putExtra("item", feedDetailsInfo.content));
                    }
                });
                ivDz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetRequest.like(contentBean.id + "", "content", !feedDetailsInfo.content.ifLike, new BaseSubscriber<Response>() {
                            @Override
                            public void onNext(Response response) {
                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                    feedDetailsInfo.content.ifLike = !feedDetailsInfo.content.ifLike;
                                    if (feedDetailsInfo.content.ifLike) {
                                        ivDz.setImageResource(R.mipmap.icon_xq_dz_h);
                                    } else {
                                        ivDz.setImageResource(R.mipmap.icon_xq_dz);
                                    }
                                    EventBus.getDefault().post(Constants.LIKED_SUCCESS + "," + feedDetailsInfo.content.ifLike + "," + contentBean.id);
                                }
                            }
                        });
                    }
                });
                authorId = feedDetailsInfo.content.authorId;
                ifFollowed = feedDetailsInfo.content.authorIfFollowed;
                if (feedDetailsInfo.content.authorIfFollowed) {
                    btnFollow.setText("正在关注");
                    btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    btnFollow.setBackground(ContextCompat.getDrawable(FeedVideoDetailActivity.this, R.drawable.shape_btn_follow_s));
                } else {
                    btnFollow.setText("关注");
                    btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                    btnFollow.setBackground(ContextCompat.getDrawable(FeedVideoDetailActivity.this, R.drawable.shape_btn_follow));
                }
            }
        });
    }

    //取消关注或者关注
    private void followOrUnFollow() {
        NetRequest.follow(authorId + "", !ifFollowed, new BaseSubscriber<Response>() {
            @Override
            public void onNext(Response response) {
                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                    ifFollowed = !ifFollowed;
                    if (ifFollowed) {
                        btnFollow.setText("正在关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(FeedVideoDetailActivity.this, R.drawable.shape_btn_follow_s));
                    } else {
                        btnFollow.setText("关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(FeedVideoDetailActivity.this, R.drawable.shape_btn_follow));
                    }
                }
            }
        });
    }

    //关闭键盘
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event) {
        if (event.contains(Constants.FORWARD_SUCCESS)) {
            if (contentBean.id == Long.parseLong(event.replace(Constants.FORWARD_SUCCESS, ""))) {
                ifFollowed = true;
                ivZf.setImageResource(R.mipmap.icon_xq_zf_h);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
