package com.barnettwong.basemvp.mvp.presenter;

import com.barnettwong.basemvp.R;
import com.barnettwong.basemvp.bean.MovieComment;
import com.barnettwong.basemvp.mvp.contract.MovieCommentContract;
import com.jaydenxiao.common.baserx.RxSubscriber;

import java.util.List;

/**
 * Created by wang on 2018/8/8.
 **/
public class MovieCommentPresenter extends MovieCommentContract.Presenter {

    @Override
    public void startMovieCommentsRequest() {
        mRxManage.add(mModel.getMovieComments().subscribe(new RxSubscriber<List<MovieComment>>(mContext,false) {
            @Override
            public void onStart() {
                super.onStart();
                mView.showLoading(mContext.getString(R.string.loading));
            }

            @Override
            protected void _onNext(List<MovieComment> dataBeanList) {
                mView.returnResult(dataBeanList);
                mView.stopLoading();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorTip(message);
            }
        }));
    }
}
