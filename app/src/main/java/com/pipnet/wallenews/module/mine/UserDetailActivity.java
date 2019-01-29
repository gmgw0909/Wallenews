package com.pipnet.wallenews.module.mine;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MyPagerAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.BaseFragment;
import com.pipnet.wallenews.bean.AuthorInfo;
import com.pipnet.wallenews.bean.response.Response;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;
import com.pipnet.wallenews.util.DisplayUtil;
import com.pipnet.wallenews.widgets.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class UserDetailActivity extends BaseActivity {

    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.iv_header_bg)
    SimpleDraweeView ivHeaderBg;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.intro)
    TextView intro;
    @BindView(R.id.zf_count)
    TextView zfCount;
    @BindView(R.id.xh_count)
    TextView xhCount;
    @BindView(R.id.btn_follow)
    TextView btnFollow;
    @BindView(R.id.bar_title)
    TextView barTitle;
    @BindView(R.id.bar_btn_back)
    TextView barBtnBack;

    String[] tabTitles = {"瓦片", "回复", "喜欢"};
    private ArrayList<BaseFragment> fragments = new ArrayList<>();//页卡视图集合
    int authorId = 0;
    boolean ifFollowed;

    @Override
    public int setContentView() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initViewData() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
        authorId = getIntent().getIntExtra("authorId", 0);
        fragments.add(WaPFragment.newInstance(authorId, "all"));
        fragments.add(WaPFragment.newInstance(authorId, "content"));
        fragments.add(WaPFragment.newInstance(authorId, "user"));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mViewPager.setOffscreenPageLimit(2);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return tabTitles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setTextSize(14);
                TextPaint tp = simplePagerTitleView.getPaint();
                tp.setFakeBoldText(true);
                int w = DisplayUtil.getScreenWidth(UserDetailActivity.this);
                simplePagerTitleView.setPadding((w / 10), 0, (w / 10), 0);
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.black));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.text_tab_blue));
                simplePagerTitleView.setText(tabTitles[index]);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(getResources().getColor(R.color.text_tab_blue));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
        appbar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    barTitle.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    barTitle.setVisibility(View.VISIBLE);
                } else {
                    //中间状态
                    barTitle.setVisibility(View.VISIBLE);
                }
            }
        });

        getAuthorDetail();
    }

    @OnClick({R.id.btn_back, R.id.bar_btn_back, R.id.btn_follow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.bar_btn_back:
                finish();
                break;
            case R.id.btn_follow:
                followOrUnFollow();
                break;
        }
    }

    private void getAuthorDetail() {
        NetRequest.authorDetail(authorId, new BaseSubscriber<AuthorInfo>() {
            @Override
            public void onNext(AuthorInfo response) {
                if (response.author != null) {
                    if (!TextUtils.isEmpty(response.author.image)) {
                        avatar.setImageURI(response.author.image);
                    } else {
                        avatar.setImageResource(R.mipmap.default_avatar);
                    }
                    if (response.author.latestContentImageArray != null && response.author.latestContentImageArray.size() > 0) {
                        ivHeaderBg.setImageURI(response.author.latestContentImageArray.get(0));
                    }
                    if (!TextUtils.isEmpty(response.author.name)) {
                        title.setText(response.author.name);
                        barTitle.setText(response.author.name);
                    }
                    if (!TextUtils.isEmpty(response.author.introduction)) {
                        intro.setText(response.author.introduction);
                    }
                    zfCount.setText(response.author.followCount + "");
                    xhCount.setText(response.author.followerCount + "");
                    ifFollowed = response.author.ifFollowed;
                    if (response.author.ifFollowed) {
                        btnFollow.setText("正在关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(UserDetailActivity.this, R.drawable.shape_btn_follow_s));
                    } else {
                        btnFollow.setText("关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(UserDetailActivity.this, R.drawable.shape_btn_follow));
                    }
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
                        xhCount.setText(Integer.parseInt(xhCount.getText().toString()) + 1 + "");
                        btnFollow.setText("正在关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(UserDetailActivity.this, R.drawable.shape_btn_follow_s));
                    } else {
                        xhCount.setText(Integer.parseInt(xhCount.getText().toString()) - 1 + "");
                        btnFollow.setText("关注");
                        btnFollow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.text_tab_blue, null));
                        btnFollow.setBackground(ContextCompat.getDrawable(UserDetailActivity.this, R.drawable.shape_btn_follow));
                    }
                }
            }
        });
    }

    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }
}
