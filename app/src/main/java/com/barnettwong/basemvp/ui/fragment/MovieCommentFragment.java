package com.barnettwong.basemvp.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.barnettwong.basemvp.R;
import com.barnettwong.basemvp.bean.MovieComment;
import com.barnettwong.basemvp.mvp.contract.MovieCommentContract;
import com.barnettwong.basemvp.mvp.model.MovieCommentModel;
import com.barnettwong.basemvp.mvp.presenter.MovieCommentPresenter;
import com.barnettwong.basemvp.ui.adapter.MovieCommentAdapter;
import com.jaydenxiao.common.base.BaseFragment;
import com.jaydenxiao.common.commonutils.ToastUitl;
import com.jaydenxiao.common.commonwidget.LoadingTip;
import com.jaydenxiao.common.commonwidget.NormalTitleBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieCommentFragment extends BaseFragment<MovieCommentPresenter,MovieCommentModel> implements MovieCommentContract.View, OnRefreshListener, OnLoadmoreListener {
    @BindView(R.id.ntb)
    NormalTitleBar ntb;
    @BindView(R.id.fragment_news_recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;
    @BindView(R.id.fragment_news_smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    Unbinder unbinder;

    private MovieCommentAdapter adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_movie_comment;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initView() {
        ntb.setBackVisibility(false);
        ntb.setTitleText("影评");
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter=new MovieCommentAdapter(getContext(),R.layout.item_movie_comment);
        recyclerview.setAdapter(adapter);

        mPresenter.startMovieCommentsRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void returnResult(List<MovieComment> newsList) {
        if (newsList != null) {
            if (adapter.getPageBean().isRefresh()) {
                smartrefreshlayout.finishRefresh();
                adapter.replaceAll(newsList);
            } else {
                if (newsList.size() > 0) {
                    adapter.addAll(newsList);
                } else {
                    ToastUitl.showShort(getString(R.string.str_end));
                }
                smartrefreshlayout.finishLoadmore();
            }
        }
    }

    @Override
    public void showLoading(String title) {
        if( adapter.getPageBean().isRefresh()) {
            if(adapter.getSize()<=0) {
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
            }
        }
    }

    @Override
    public void stopLoading() {
        if(adapter.getSize()==0){
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
        }else{
            loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        }
    }

    @Override
    public void showErrorTip(String msg) {
        if( adapter.getPageBean().isRefresh()) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadedTip.setLoadingTip(LoadingTip.LoadStatus.error);
                    loadedTip.setTips(msg);
                }
            },2000);
            adapter.clear();
            smartrefreshlayout.finishRefresh(false);
        }else{
            smartrefreshlayout.finishLoadmore(false);
        }
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        adapter.getPageBean().setRefresh(false);
        //发起请求
//        mPresenter.startMovieCommentsRequest();
        smartrefreshlayout.finishLoadmore(true);
        ToastUitl.showShort("没有更多哦～");
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        adapter.getPageBean().setRefresh(true);
        //发起请求
        mPresenter.startMovieCommentsRequest();
    }
}
