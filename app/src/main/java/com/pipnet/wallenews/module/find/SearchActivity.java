package com.pipnet.wallenews.module.find;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.SearchSuggestAdapter;
import com.pipnet.wallenews.adapter.SearchTagAdapter;
import com.pipnet.wallenews.adapter.WaLiMultiAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.bean.FeedResponse;
import com.pipnet.wallenews.bean.SearchRecommend;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.module.home.FeedDetailActivity;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.widgets.ClearEditText;
import com.pipnet.wallenews.widgets.LoadingDialog;

import net.arvin.itemdecorationhelper.ItemDecorationFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeeBoo on 2019/1/27.
 */

public class SearchActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.rv_like)
    RecyclerView rvLike;
    @BindView(R.id.et_search)
    ClearEditText etSearch;
    @BindView(R.id.btn_right)
    TextView btnRight;
    @BindView(R.id.show)
    ImageView show;
    @BindView(R.id.nest_view)
    NestedScrollView nestView;
    @BindView(R.id.recycler_association)
    RecyclerView rvAssociation;
    @BindView(R.id.recycler_search)
    RecyclerView rvSearch;

    SearchTagAdapter hotAdapter;
    SearchTagAdapter historyAdapter;
    SearchTagAdapter likeAdapter;
    SearchSuggestAdapter suggestAdapter;
    WaLiMultiAdapter adapter;

    List<SearchRecommend.TagBean> hotList = new ArrayList<>();
    List<SearchRecommend.TagBean> historyList = new ArrayList<>();
    List<SearchRecommend.TagBean> likeList = new ArrayList<>();
    List<SearchRecommend.TagBean> suggestList = new ArrayList<>();
    List<FeedResponse.FeedsBean> list = new ArrayList<>();

    LoadingDialog loadingDialog;
    Gson gson;

    @Override
    public int setContentView() {
        return R.layout.activity_search;
    }

    @Override
    public void initViewData() {
        gson = new Gson();
        List<SearchRecommend.TagBean> list_ = gson.fromJson(SPUtils.getString("historyList", "[]"),
                new TypeToken<List<SearchRecommend.TagBean>>() {
                }.getType());
        if (list_ != null) {
            historyList.addAll(list_);
        }
        loadingDialog = new LoadingDialog(this);
        hotAdapter = new SearchTagAdapter(hotList);
        historyAdapter = new SearchTagAdapter(historyList);
        likeAdapter = new SearchTagAdapter(likeList);
        suggestAdapter = new SearchSuggestAdapter(suggestList);
        adapter = new WaLiMultiAdapter(list);
        hotAdapter.needNumber = true;
        adapter.setOnItemClickListener(this);

        rvHot.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvHistory.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvLike.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
        rvAssociation.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.addItemDecoration(new ItemDecorationFactory.DividerBuilder()
                .dividerColor(getResources().getColor(R.color.line_light))
                .build(rvSearch));

        rvHot.setAdapter(hotAdapter);
        rvHistory.setAdapter(historyAdapter);
        rvLike.setAdapter(likeAdapter);
        rvAssociation.setAdapter(suggestAdapter);
        rvSearch.setAdapter(adapter);
        suggestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearch.setText(suggestList.get(position).name);
                etSearch.setSelection(etSearch.getText().length());
                search();
            }
        });
        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearch.setText(hotList.get(position).name);
                etSearch.setSelection(etSearch.getText().length());
                search();
            }
        });
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearch.setText(historyList.get(position).name);
                etSearch.setSelection(etSearch.getText().length());
                search();
            }
        });
        likeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                etSearch.setText(likeList.get(position).name);
                etSearch.setSelection(etSearch.getText().length());
                search();
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    nestView.setVisibility(View.GONE);
                    rvAssociation.setVisibility(View.VISIBLE);
                    rvSearch.setVisibility(View.GONE);
                    btnRight.setText("搜索");
                    searchSuggest(s.toString());
                } else {
                    nestView.setVisibility(View.VISIBLE);
                    rvAssociation.setVisibility(View.GONE);
                    rvSearch.setVisibility(View.GONE);
                    btnRight.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //点击话题进入
        String key = getIntent().getStringExtra("keyword");
        if (!TextUtils.isEmpty(key)) {
            etSearch.setText(key);
            etSearch.setSelection(etSearch.getText().length());
            search();
        }
        //获取搜索记录
        getSearchRecommend();
    }

    private void getSearchRecommend() {
        NetRequest.searchRecommend(new BaseSubscriber<SearchRecommend>() {
            @Override
            public void onNext(SearchRecommend searchRecommend) {
                hotList.addAll(searchRecommend.hotTopics);
                likeList.addAll(searchRecommend.suggTopics);
                hotAdapter.notifyDataSetChanged();
                likeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchSuggest(String keyword) {
        NetRequest.searchSuggest(keyword, new BaseSubscriber<SearchRecommend>() {
            @Override
            public void onNext(SearchRecommend searchRecommend) {
                suggestList.clear();
                suggestList.addAll(searchRecommend.feeds);
                suggestAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.btn_right, R.id.delete, R.id.show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
                if (btnRight.getText().toString().equals("取消")) {
                    finish();
                } else {
                    search();
                }
                break;
            case R.id.delete:
                SPUtils.setString("historyList", "");
                historyList.clear();
                historyAdapter.notifyDataSetChanged();
                break;
            case R.id.show:
                if (rvLike.getVisibility() == View.VISIBLE) {
                    show.setImageResource(R.mipmap.icon_xs);
                    rvLike.setVisibility(View.GONE);
                } else {
                    rvLike.setVisibility(View.VISIBLE);
                    show.setImageResource(R.mipmap.icon_yc);
                }
                break;
        }
    }

    private void search() {
        nestView.setVisibility(View.GONE);
        rvAssociation.setVisibility(View.GONE);
        rvSearch.setVisibility(View.VISIBLE);
        String keyword = etSearch.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        setHistory(keyword);
        loadingDialog.show();
        NetRequest.search(keyword, new BaseSubscriber<FeedResponse>() {
            @Override
            public void onNext(FeedResponse feedResponse) {
                list.clear();
                list.addAll(feedResponse.feeds);
                adapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * 存搜索历史记录
     *
     * @param keyword
     */
    private void setHistory(String keyword) {
        for (int i = 0; i < historyList.size(); i++) {
            if (keyword.equals(historyList.get(i).name)) {
                return;
            }
        }
        historyList.add(new SearchRecommend.TagBean(keyword));
        String str = gson.toJson(historyList);
        SPUtils.setString("historyList", str);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        view.findViewById(R.id.btn_topic).performClick();
        startActivity(new Intent(SearchActivity.this, FeedDetailActivity.class).putExtra("FEED_ID", list.get(position).content.id));
    }
}
