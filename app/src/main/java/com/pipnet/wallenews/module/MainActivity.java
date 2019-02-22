package com.pipnet.wallenews.module;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pipnet.wallenews.R;
import com.pipnet.wallenews.base.BaseActivity;
import com.pipnet.wallenews.module.find.FindFragment;
import com.pipnet.wallenews.module.home.WaLiFragment;
import com.pipnet.wallenews.module.message.MessageFragment;
import com.pipnet.wallenews.module.mine.MineFragment;
import com.pipnet.wallenews.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.frame_content)
    FrameLayout frameContent;
    @BindView(R.id.ivHome)
    ImageView ivHome;
    @BindView(R.id.tvHome)
    TextView tvHome;
    @BindView(R.id.ivFind)
    ImageView ivFind;
    @BindView(R.id.tvFind)
    TextView tvFind;
    @BindView(R.id.ivMessage)
    ImageView ivMessage;
    @BindView(R.id.tvMessage)
    TextView tvMessage;
    @BindView(R.id.ivMy)
    ImageView ivMy;
    @BindView(R.id.tvMy)
    TextView tvMy;

    private ImageView[] imageViews;
    private TextView[] textViews;
    private int[] res_normal = {R.mipmap.tab_icon_zy_d, R.mipmap.tab_icon_ss_d, R.mipmap.tab_icon_xx_d, R.mipmap.tab_icon_wd_d};
    private int[] res_selected = {R.mipmap.tab_icon_zy_s, R.mipmap.tab_icon_ss_s, R.mipmap.tab_icon_xx_s, R.mipmap.tab_icon_wd_s};

    private WaLiFragment waliFragment;
    private FindFragment findFragment;
    private MessageFragment messageFragment;
    private MineFragment myFragment;

    private Long mExitTime = 0L;

    private List<Fragment> fragmentList = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewData() {
        imageViews = new ImageView[]{ivHome, ivFind, ivMessage, ivMy};
        textViews = new TextView[]{tvHome, tvFind, tvMessage, tvMy};
        waliFragment = new WaLiFragment();
        findFragment = new FindFragment();
        messageFragment = new MessageFragment();
        myFragment = new MineFragment();
        fragmentList.add(waliFragment);
        fragmentList.add(findFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(myFragment);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, waliFragment, "0").commit();
    }

    @OnClick({R.id.llHome, R.id.llFind, R.id.llMessage, R.id.llMy})
    public void onViewClicked(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.llHome:
                selectTab(index = 0);
                break;
            case R.id.llFind:
                selectTab(index = 1);
                break;
            case R.id.llMessage:
                selectTab(index = 2);
                break;
            case R.id.llMy:
                selectTab(index = 3);
                break;
        }
        if (index == currentIndex) {
            return;
        }
        Fragment fragment = fragmentList.get(index);
        //判断当前点击的Fragment有没有被添加到Activity中
        if (fragment.isAdded()) {
            //仅仅显示这个点击的Fragment，同时将老的隐藏掉
            showFragment(fragment, fragmentList.get(currentIndex));
        } else {
            //先添加当前点击的Fragment，同时将老的隐藏掉
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_content, fragment, index + "")
                    .hide(fragmentList.get(currentIndex))
                    .commit();
        }

        currentIndex = index;
    }

    private void selectTab(int index) {
        for (int i = 0; i < imageViews.length; i++) {
            if (i == index) {
                imageViews[i].setImageResource(res_selected[i]);
                textViews[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_blue));
            } else {
                imageViews[i].setImageResource(res_normal[i]);
                textViews[i].setTextColor(ContextCompat.getColor(this, R.color.text_tab_normal));
            }
        }
    }

    private void showFragment(Fragment showFragment, Fragment hideFragment) {
        getSupportFragmentManager().beginTransaction()
                .show(showFragment)
                .hide(hideFragment)
                .commit();
    }

//    //对返回键进行监听
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            exit();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    public void exit() {
//        if ((System.currentTimeMillis() - mExitTime) > 2000) {
//            ToastUtil.show("再按一次退出瓦砾");
//            mExitTime = System.currentTimeMillis();
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
