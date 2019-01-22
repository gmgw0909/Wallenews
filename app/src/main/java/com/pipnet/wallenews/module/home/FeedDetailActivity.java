package com.pipnet.wallenews.module.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.CommentAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.FeedDetailsInfo;
import com.pipnet.wallenews.bean.RepliesResponse;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.interfacee.JavascriptInterface;
import com.pipnet.wallenews.widgets.CarRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedDetailActivity extends BaseActivity implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

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

    WebView webView;

    long id = 0;

    CommentAdapter adapter;

    @Override
    public int setContentView() {
        return R.layout.activity_feed_detail;
    }

    @Override
    public void initViewData() {
        List<RepliesResponse.RepliesBean> data = new ArrayList<>();
        data.add(new RepliesResponse.RepliesBean());
        initView(this, adapter = new CommentAdapter(data));
        id = getIntent().getLongExtra("FEED_ID", 0);
        NetRequest.detail(id, new BaseSubscriber<FeedDetailsInfo>() {
            @Override
            public void onNext(FeedDetailsInfo feedDetailsInfo) {
                webView.addJavascriptInterface(new JavascriptInterface(FeedDetailActivity.this, returnImageUrlsFromHtml(feedDetailsInfo.content.content)), "imagelistner");
                webView.loadDataWithBaseURL(null, feedDetailsInfo.content.content, "text/html", "utf-8", null);
            }
        });

        NetRequest.replies(id, 1, new BaseSubscriber<RepliesResponse>() {
            @Override
            public void onNext(RepliesResponse repliesResponse) {

            }
        });

    }

    @OnClick({R.id.btn_left, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_left:
                finish();
                break;
            case R.id.btn_right:
                break;
        }
    }

    private void initView(Context context, BaseQuickAdapter adapter) {
        //初始化头部
        View header = LayoutInflater.from(context).inflate(R.layout.item_feed_header, null);
        TextView name = header.findViewById(R.id.title_name);
        SimpleDraweeView avatar = header.findViewById(R.id.title_avatar);
        webView = header.findViewById(R.id.web_view);
        intWebView(webView);
        adapter.addHeaderView(header);
        //初始化Feeds列表
        refreshLayout.setRefreshHeader(new CarRefreshHeader(context));
        refreshLayout.setEnableLoadmore(false);//加载更多由BaseQuickAdapter完成
        refreshLayout.setOnRefreshListener(this);
        recyclerComment.setLayoutManager(new LinearLayoutManager(context));
        recyclerComment.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(context.getResources().getColor(R.color.line_light))
                .build(recyclerComment));
        recyclerComment.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recyclerComment);
        adapter.setEmptyView(R.layout.layout_empty);
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

    }

    @Override
    public void onLoadMoreRequested() {

    }

}
