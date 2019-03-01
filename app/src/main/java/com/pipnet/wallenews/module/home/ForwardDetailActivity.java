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
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
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

public class ForwardDetailActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.title_avatar)
    SimpleDraweeView titleAvatar;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_dz)
    ImageView ivDz;
    @BindView(R.id.iv_zf)
    ImageView ivZf;
    @BindView(R.id.load_page_view)
    ImageView loadPageView;

    ContentBean contentBean;
    long authorId = 0;
    int page = 1;
    int commentCounts = 0;
    List<ContentBean> list = new ArrayList<>();
    CommentAdapter adapter;
    boolean ifFollowed;

    @Override
    public int setContentView() {
        return R.layout.activity_feed_detail;
    }

    @Override
    public void initViewData() {
        loadPageView.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
        contentBean = (ContentBean) getIntent().getSerializableExtra("FORWARD_CONTENT");
        initView(this, adapter = new CommentAdapter(list));
        getComments(page);
//        getDetail();
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
                                                    commentCounts = commentCounts + 1;
                                                    commentCount.setText(commentCounts + "条评论");
                                                    commentCount.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.btn_left, R.id.btn_right, R.id.commentCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                followOrUnFollow();
                break;
            case R.id.commentCount:
                recyclerComment.scrollToPosition(1);
                break;
        }
    }

    private void initView(Context context, BaseQuickAdapter adapter) {
        //初始化头部
        View header = LayoutInflater.from(context).inflate(R.layout.item_header_forward_detail, null);
        SimpleDraweeView avatar = header.findViewById(R.id.avatar);
        TextView name = header.findViewById(R.id.name);
        TextView time = header.findViewById(R.id.time);
        TextView content = header.findViewById(R.id.content);
        TextView sourceAuthorName = header.findViewById(R.id.sourceAuthorName);
        TextView sourceContentTitle = header.findViewById(R.id.sourceContentTitle);
        LinearLayout llImg = header.findViewById(R.id.ll_img);
        LinearLayout ll3 = header.findViewById(R.id.ll_3);
        SimpleDraweeView img1_1 = header.findViewById(R.id.img1_1);
        SimpleDraweeView img23_1 = header.findViewById(R.id.img23_1);
        SimpleDraweeView img2_2 = header.findViewById(R.id.img2_2);
        SimpleDraweeView img3_2 = header.findViewById(R.id.img3_2);
        SimpleDraweeView img3_3 = header.findViewById(R.id.img3_3);
        if (!TextUtils.isEmpty(contentBean.authorImage)) {
            avatar.setImageURI(contentBean.authorImage);
        } else {
            avatar.setImageResource(R.mipmap.default_avatar);
        }
        name.setText(contentBean.authorName);
        time.setText(TimeUtil.intervalTime(contentBean.createTime));
        content.setText(contentBean.content);
        sourceAuthorName.setText(contentBean.sourceAuthorName);
        sourceContentTitle.setText(contentBean.sourceContentTitle);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForwardDetailActivity.this, UserDetailActivity.class).putExtra("authorId", contentBean.authorId));
            }
        });
        if (contentBean.sourceContentImageAry != null && contentBean.sourceContentImageAry.size() > 0) {
            llImg.setVisibility(View.VISIBLE);
            List<String> images = contentBean.sourceContentImageAry;
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
        header.findViewById(R.id.ll_forward).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForwardDetailActivity.this, FeedDetailActivity.class).putExtra("FEED_ID", contentBean.sourceId));
            }
        });
        adapter.addHeaderView(header);
        //初始化Feeds列表
//        refreshLayout.setRefreshHeader(new CarRefreshHeader(context));
//        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
//        refreshLayout.setOnRefreshListener(this);
        recyclerComment.setLayoutManager(new LinearLayoutManager(context));
        recyclerComment.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.line_light))
                .build(recyclerComment));
        recyclerComment.setAdapter(adapter);
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
                commentCounts = feedDetailsInfo.content.commentCount;
                if (commentCounts != 0) {
                    commentCount.setText(commentCounts + "条评论");
                    commentCount.setVisibility(View.VISIBLE);
                }
                titleName.setText(feedDetailsInfo.content.authorName);
                if (!TextUtils.isEmpty(feedDetailsInfo.content.authorImage)) {
                    titleAvatar.setImageURI(feedDetailsInfo.content.authorImage);
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
                        startActivity(new Intent(ForwardDetailActivity.this, ForwardActivity.class).putExtra("item", feedDetailsInfo.content));
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
                    btnRight.setText("正在关注");
                    btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    btnRight.setBackground(ContextCompat.getDrawable(ForwardDetailActivity.this, R.drawable.shape_btn_follow_s));
                } else {
                    btnRight.setText("关注");
                    btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                    btnRight.setBackground(ContextCompat.getDrawable(ForwardDetailActivity.this, R.drawable.shape_btn_follow));
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
                        btnRight.setText("正在关注");
                        btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        btnRight.setBackground(ContextCompat.getDrawable(ForwardDetailActivity.this, R.drawable.shape_btn_follow_s));
                    } else {
                        btnRight.setText("关注");
                        btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnRight.setBackground(ContextCompat.getDrawable(ForwardDetailActivity.this, R.drawable.shape_btn_follow));
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
}
