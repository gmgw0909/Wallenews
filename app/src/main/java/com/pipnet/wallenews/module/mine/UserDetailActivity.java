package com.pipnet.wallenews.module.mine;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jaeger.library.StatusBarUtil;
import com.pipnet.wallenews.R;
import com.pipnet.wallenews.adapter.MyPagerAdapter;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.base.BaseFragment;
import com.pipnet.wallenews.module.home.WaLiFragment;
import com.pipnet.wallenews.util.DisplayUtil;
import com.pipnet.wallenews.widgets.ScaleTransitionPagerTitleView;
import com.scwang.smartrefresh.layout.util.DensityUtil;

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

public class UserDetailActivity extends BaseActivity {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    String[] tabTitles = {"瓦片", "回复", "喜欢"};
    private ArrayList<BaseFragment> fragments = new ArrayList<>();//页卡视图集合

    @Override
    public int setContentView() {
        return R.layout.activity_user_detail;
    }

    @Override
    public void initViewData() {
        StatusBarUtil.setTranslucentForCoordinatorLayout(this, 0);
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
}
