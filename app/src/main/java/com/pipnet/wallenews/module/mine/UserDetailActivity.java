package com.pipnet.wallenews.module.mine;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jaeger.library.StatusBarUtil;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MyPagerAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.BaseFragment;
import com.pipnet.wallenews.bean.FeedResponse;
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

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.iv_header_bg)
    ImageView ivHeaderBg;
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
    FeedResponse.FeedsBean.ContentBean contentBean;

    @Override
    public int setContentView() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initViewData() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
        contentBean = (FeedResponse.FeedsBean.ContentBean) getIntent().getSerializableExtra("item");
        fragments.add(new WaPianFragment());
        fragments.add(new WaPianFragment());
        fragments.add(new WaPianFragment());
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
    }

    @OnClick({R.id.btn_back, R.id.bar_btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.bar_btn_back:
                finish();
                break;
        }
    }
}
