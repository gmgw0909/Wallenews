package com.pipnet.wallenews.module.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.interfacee.JavascriptInterface;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedDetailActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.title_avatar)
    SimpleDraweeView titleAvatar;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.recycler_comment)
    RecyclerView recyclerComment;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.load_page_view)
    ImageView loadPageView;
    @BindView(R.id.commentCount)
    TextView commentCount;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_dz)
    ImageView ivDz;
    @BindView(R.id.iv_zf)
    ImageView ivZf;
    TextView btnFollow;

    TextView noComment;
    LinearLayout llTop;
    TextView name, time, title, zfCount, xhCount;
    SimpleDraweeView avatar;
    WebView webView;

    long id = 0;
    long authorId = 0;
    int page = 1;
    int commentCounts = 0;
    List<RepliesResponse.RepliesBean> list = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    CommentAdapter adapter;
    boolean ifFollowed;

    @Override
    public int setContentView() {
        return R.layout.activity_feed_detail;
    }

    @Override
    public void initViewData() {
        EventBus.getDefault().register(this);
        initView(this, adapter = new CommentAdapter(list));
        id = getIntent().getLongExtra("FEED_ID", 0);
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
                                NetRequest.reply(id + "", comment, "", "",
                                        "content", new BaseSubscriber<Response>() {
                                            @Override
                                            public void onNext(Response response) {
                                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                                    etComment.setText("");
                                                    hintKeyBoard();
                                                    EventBus.getDefault().post(Constants.COMMENT_SUCCESS + id);
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
        View header = LayoutInflater.from(context).inflate(R.layout.item_feed_header, null);
        name = header.findViewById(R.id.name);
        avatar = header.findViewById(R.id.avatar);
        time = header.findViewById(R.id.time);
        title = header.findViewById(R.id.title);
        btnFollow = header.findViewById(R.id.btn_right);
        zfCount = header.findViewById(R.id.zf_count);
        xhCount = header.findViewById(R.id.xh_count);
        noComment = header.findViewById(R.id.no_comment);
        webView = header.findViewById(R.id.web_view);
        llTop = header.findViewById(R.id.ll_top);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followOrUnFollow();
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeedDetailActivity.this, UserDetailActivity.class).putExtra("authorId", authorId));
            }
        });
        intWebView(webView);
        adapter.addHeaderView(header);
        //初始化Feeds列表
        refreshLayout.setRefreshHeader(new CarRefreshHeader(context));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);
        recyclerComment.setLayoutManager(linearLayoutManager = new LinearLayoutManager(context));
        recyclerComment.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.line_light))
                .build(recyclerComment));
        recyclerComment.setAdapter(adapter);
        recyclerComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //当前条目索引
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                //根据索引来获取对应的itemView
                View firstVisibleChildView = linearLayoutManager
                        .findViewByPosition(position);
                //获取当前显示条目的高度
                int itemHeight = firstVisibleChildView.getHeight();
                //获取当前RecyclerView 偏移量
                int flag = (position) * itemHeight - firstVisibleChildView.getTop();
                if (flag >= llTop.getHeight()) {
                    //做显示布局操作
                    titleAvatar.setVisibility(View.VISIBLE);
                    titleName.setVisibility(View.VISIBLE);
                    commentCount.setVisibility(View.GONE);
                    btnRight.setVisibility(View.VISIBLE);
                } else {
                    //做隐藏布局操作
                    titleAvatar.setVisibility(View.GONE);
                    titleName.setVisibility(View.GONE);
                    btnRight.setVisibility(View.GONE);
                    if (commentCounts != 0) {
                        commentCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
//        adapter.setEmptyView(R.layout.layout_empty);
//        refreshLayout.autoRefresh();
    }

    @SuppressLint("JavascriptInterface")
    private void intWebView(final WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //这段js函数的功能就是注册监听，遍历所有的img标签，并添加onClick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
                webView.loadUrl("javascript:(function(){"
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++)  " + "{"
                        + "    objs[i].onclick=function()  " + "    {  "
                        + "        window.imagelistner.openImage(this.src);  "
                        + "    }  " + "}" + "})()");

                loadPageView.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("WrongConstant")
    public static String[] returnImageUrlsFromHtml(String htmlStr) {
        List<String> pics = new ArrayList<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics.toArray(new String[pics.size()]);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        getComments(page);
        getDetail();
    }

//    @Override
//    public void onLoadMoreRequested() {
//        page++;
//        getComments(page);
//    }

    private void getComments(final int page) {
        NetRequest.replies(id, page, new BaseSubscriber<RepliesResponse>() {
            @Override
            public void onNext(RepliesResponse repliesResponse) {
                if (repliesResponse.replies != null && repliesResponse.replies.size() > 0) {
                    List<RepliesResponse.RepliesBean> list_ = repliesResponse.replies;
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(list_);
                    adapter.notifyDataSetChanged();
                    adapter.loadMoreComplete();
                    noComment.setVisibility(View.GONE);
                } else {
                    noComment.setVisibility(View.VISIBLE);
                    adapter.loadMoreEnd();
                }
                refreshLayout.finishRefresh();
            }
        });
    }

    private void getDetail() {
        NetRequest.detail(id, new BaseSubscriber<FeedDetailsInfo>() {
            @Override
            public void onNext(final FeedDetailsInfo feedDetailsInfo) {
                zfCount.setText(feedDetailsInfo.content.forwardCount + "");
                xhCount.setText(feedDetailsInfo.content.likeCount + "");
                commentCounts = feedDetailsInfo.content.commentCount;
                if (commentCounts != 0) {
                    commentCount.setText(commentCounts + "条评论");
                    commentCount.setVisibility(View.VISIBLE);
                }
                title.setText(feedDetailsInfo.content.title);
                name.setText(feedDetailsInfo.content.authorName);
                titleName.setText(feedDetailsInfo.content.authorName);
                time.setText(TimeUtil.intervalTime(feedDetailsInfo.content.createTime));
                if (!TextUtils.isEmpty(feedDetailsInfo.content.authorImage)) {
                    avatar.setImageURI(feedDetailsInfo.content.authorImage);
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
                        startActivity(new Intent(FeedDetailActivity.this, ForwardActivity.class).putExtra("item", feedDetailsInfo.content));
                    }
                });
                ivDz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetRequest.like(id + "", "content", !feedDetailsInfo.content.ifLike, new BaseSubscriber<Response>() {
                            @Override
                            public void onNext(Response response) {
                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                    feedDetailsInfo.content.ifLike = !feedDetailsInfo.content.ifLike;
                                    if (feedDetailsInfo.content.ifLike) {
                                        ivDz.setImageResource(R.mipmap.icon_xq_dz_h);
                                    } else {
                                        ivDz.setImageResource(R.mipmap.icon_xq_dz);
                                    }
                                    EventBus.getDefault().post(Constants.LIKED_SUCCESS + "," + feedDetailsInfo.content.ifLike + "," + id);
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
                    btnRight.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow_s));
                    btnFollow.setText("正在关注");
                    btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                    btnFollow.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow_s));
                } else {
                    btnRight.setText("关注");
                    btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                    btnRight.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow));
                    btnFollow.setText("关注");
                    btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                    btnFollow.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow));
                }
                webView.addJavascriptInterface(new JavascriptInterface(FeedDetailActivity.this, returnImageUrlsFromHtml(feedDetailsInfo.content.content)), "imagelistner");
                webView.loadDataWithBaseURL(null, feedDetailsInfo.content.content, "text/html", "utf-8", null);
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
                        btnRight.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow_s));
                        btnFollow.setText("正在关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow_s));
                    } else {
                        btnRight.setText("关注");
                        btnRight.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnRight.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow));
                        btnFollow.setText("关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(FeedDetailActivity.this, R.drawable.shape_btn_follow));
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
            if (id == Long.parseLong(event.replace(Constants.FORWARD_SUCCESS, ""))) {
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
