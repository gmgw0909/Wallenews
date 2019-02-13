package com.pipnet.wallenews.module.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.AtAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.Constants;
import com.pipnet.wallenews.bean.ContentBean;
import com.pipnet.wallenews.bean.LoginInfo;
import com.pipnet.wallenews.bean.SearchRecommend;
import com.pipnet.wallenews.bean.TopicsBean;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.SPUtils;
import com.pipnet.wallenews.widgets.ClearEditText;
import com.pipnet.wallenews.widgets.MyPopWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ReplyActivity extends BaseActivity {

    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.my_avatar)
    SimpleDraweeView myAvatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.hf_name)
    TextView hfName;
    @BindView(R.id.et_comment)
    EditText etComment;

    ContentBean contentBean;
    MyPopWindow popWindow;
    AtAdapter atAdapter;
    List<TopicsBean> searchList = new ArrayList<>();
    List<TopicsBean> selectedList = new ArrayList<>();

    @Override
    public int setContentView() {
        return R.layout.activity_reply;
    }

    @Override
    public void initViewData() {
        contentBean = (ContentBean) getIntent().getSerializableExtra("item");
        if (!TextUtils.isEmpty(SPUtils.getObject(LoginInfo.class).avatar)) {
            myAvatar.setImageURI(SPUtils.getObject(LoginInfo.class).avatar);
        }
        if (!TextUtils.isEmpty(contentBean.authorImage)) {
            avatar.setImageURI(contentBean.authorImage);
        }
        name.setText(contentBean.authorName);
        hfName.setText("@" + contentBean.authorName);
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().substring(s.length() - 1).equals("@")) {
                        showPopSearch(0);
                    } else if (s.toString().substring(s.length() - 1).equals("#")) {
                        showPopSearch(1);
                    }
                }

                String comment = etComment.getText().toString();
                if (!TextUtils.isEmpty(comment) && comment.length() > 1 && selectedList.size() > 0) {
                    if (comment.contains("@") || comment.contains("#")) {
                        for (int i = 0; i < selectedList.size(); i++) {
                            if (comment.endsWith("@" + selectedList.get(i).name)) {
                                etComment.setText(comment.replace("@" + selectedList.get(i).name, ""));
                                etComment.setSelection(etComment.getText().toString().length());
                                selectedList.remove(i);
                            } else if (comment.endsWith("#" + selectedList.get(i).name)) {
                                etComment.setText(comment.replace("#" + selectedList.get(i).name, ""));
                                etComment.setSelection(etComment.getText().toString().length());
                                selectedList.remove(i);
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                String comment = etComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    return;
                }
                StringBuffer replyTos = new StringBuffer("");
                StringBuffer replyToIds = new StringBuffer("");
                if (selectedList.size() > 0) {
                    for (int i = 0; i < selectedList.size(); i++) {
                        if (i == selectedList.size() - 1) {
                            if (selectedList.get(i).labelId == 0) {
                                replyTos.append("@" + selectedList.get(i).name);
                            } else {
                                replyTos.append("#" + selectedList.get(i).name);
                            }
                            replyToIds.append(selectedList.get(i).id + "");
                        } else {
                            if (selectedList.get(i).labelId == 0) {
                                replyTos.append("@" + selectedList.get(i).name + " ");
                            } else {
                                replyTos.append("#" + selectedList.get(i).name + " ");
                            }
                            replyToIds.append(selectedList.get(i).id + " ");
                        }
                    }
                }
                NetRequest.reply(contentBean.id + "", comment, replyTos.toString(), replyToIds.toString(),
                        "content", new BaseSubscriber<Response>() {
                            @Override
                            public void onNext(Response response) {
                                if (!TextUtils.isEmpty(response.status) && response.status.equals("OK")) {
                                    EventBus.getDefault().post(Constants.COMMENT_SUCCESS + contentBean.id);
                                    finish();
                                }
                            }
                        });
                break;
        }
    }

    private void showPopSearch(final int type) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_pop_at_someone, null);
        final TextView btnRight = contentView.findViewById(R.id.btn_right);
        final ClearEditText etSearch = contentView.findViewById(R.id.et_search);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));
        recyclerView.setAdapter(atAdapter = new AtAdapter(searchList));
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRight.getText().toString().equals("取消")) {
                    popWindow.dissmiss();
                } else {
                    String searchStr = etSearch.getText().toString();
                    searchList.clear();
                    if (type == 0) {
                        searchAuthor(searchStr);
                    } else {
                        searchTopic(searchStr);
                    }
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnRight.setText("搜索");
                } else {
                    btnRight.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        popWindow = new MyPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .setAnimationStyle(R.style.PopupAnimation)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .create()
                .showAtLocation(etComment, Gravity.BOTTOM, 0, 0);
        //默认打开就搜索全部
        searchList.clear();
        if (type == 0) {
            searchAuthor("");
        } else {
            searchTopic("");
        }
        //点击回调
        atAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String nowCommentStr = etComment.getText().toString();
                etComment.setText(nowCommentStr + searchList.get(position).name + " ");
                etComment.setSelection(etComment.getText().toString().length());
                selectedList.add(searchList.get(position));
                popWindow.dissmiss();
            }
        });
    }

    private void searchTopic(String keyword) {
        NetRequest.topicSearch(keyword, new BaseSubscriber<SearchRecommend>() {
            @Override
            public void onNext(SearchRecommend searchRecommend) {
                searchList.addAll(searchRecommend.topics);
                atAdapter.notifyDataSetChanged();
            }
        });
    }

    private void searchAuthor(String keyword) {
        NetRequest.authorSearch(keyword, new BaseSubscriber<SearchRecommend>() {
            @Override
            public void onNext(SearchRecommend searchRecommend) {
                searchList.addAll(searchRecommend.authors);
                atAdapter.notifyDataSetChanged();
            }
        });
    }
}
