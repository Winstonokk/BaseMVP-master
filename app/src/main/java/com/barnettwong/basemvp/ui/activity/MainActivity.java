package com.barnettwong.basemvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.barnettwong.basemvp.R;
import com.barnettwong.basemvp.app.AppConstant;
import com.barnettwong.basemvp.bean.TabEntity;
import com.barnettwong.basemvp.ui.fragment.CircleFragment;
import com.barnettwong.basemvp.ui.fragment.HomeFragment;
import com.barnettwong.basemvp.ui.fragment.MineFragment;
import com.barnettwong.basemvp.ui.fragment.MovieCommentFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.LogUtils;
import com.jaydenxiao.common.daynightmodeutils.ChangeModeController;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tab_layout)
    CommonTabLayout tabLayout;

    private String[] mTitles = {"资讯", "动态", "影评", "我的"};
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal, R.mipmap.ic_care_normal, R.mipmap.ic_video_normal, R.mipmap.ic_mine_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected, R.mipmap.ic_care_selected, R.mipmap.ic_video_selected, R.mipmap.ic_mine_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private HomeFragment checkHomeFragment;
    private CircleFragment checkCicleFragment;
    private MovieCommentFragment checkVideoFragment;
    private MineFragment checkMineFragment;

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        //初始化菜单
        initTab();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //切换daynight模式要立即变色的页面
        ChangeModeController.getInstance().init(this, R.attr.class);
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        tabLayout.setTabData(mTabEntities);
        //点击监听
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                SwitchTo(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
    }

    /**
     * 初始化碎片
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int currentTabPosition = 0;
        if (savedInstanceState != null) {
            checkHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("checkHomeFragment");
            checkCicleFragment = (CircleFragment) getSupportFragmentManager().findFragmentByTag("checkCicleFragment");
            checkVideoFragment = (MovieCommentFragment) getSupportFragmentManager().findFragmentByTag("checkVideoFragment");
            checkMineFragment = (MineFragment) getSupportFragmentManager().findFragmentByTag("checkMineFragment");
            currentTabPosition = savedInstanceState.getInt(AppConstant.HOME_CURRENT_TAB_POSITION);
        } else {
            checkHomeFragment = new HomeFragment();
            checkCicleFragment = new CircleFragment();
            checkVideoFragment = new MovieCommentFragment();
            checkMineFragment = new MineFragment();

            transaction.add(R.id.fl_body, checkHomeFragment, "checkHomeFragment");
            transaction.add(R.id.fl_body, checkCicleFragment, "checkCicleFragment");
            transaction.add(R.id.fl_body, checkVideoFragment, "checkVideoFragment");
            transaction.add(R.id.fl_body, checkMineFragment, "checkMineFragment");
        }
        transaction.commit();
        SwitchTo(currentTabPosition);
        tabLayout.setCurrentTab(currentTabPosition);
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        LogUtils.logd("主页菜单position" + position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position) {
            //首页
            case 0:
                transaction.hide(checkCicleFragment);
                transaction.hide(checkVideoFragment);
                transaction.hide(checkMineFragment);
                transaction.show(checkHomeFragment);
                transaction.commitAllowingStateLoss();
                break;
            //动态
            case 1:
                transaction.hide(checkVideoFragment);
                transaction.hide(checkMineFragment);
                transaction.hide(checkHomeFragment);
                transaction.show(checkCicleFragment);
                transaction.commitAllowingStateLoss();
                break;
            //视频
            case 2:
                transaction.hide(checkCicleFragment);
                transaction.hide(checkMineFragment);
                transaction.hide(checkHomeFragment);
                transaction.show(checkVideoFragment);
                transaction.commitAllowingStateLoss();
                break;
            //我的
            case 3:
                transaction.hide(checkCicleFragment);
                transaction.hide(checkVideoFragment);
                transaction.hide(checkHomeFragment);
                transaction.show(checkMineFragment);
                transaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //奔溃前保存位置
        LogUtils.loge("onSaveInstanceState进来了1");
        if (tabLayout != null) {
            LogUtils.loge("onSaveInstanceState进来了2");
            outState.putInt(AppConstant.HOME_CURRENT_TAB_POSITION, tabLayout.getCurrentTab());
        }
    }

}
