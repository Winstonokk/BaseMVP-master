package com.barnettwong.basemvp.mvp.model;

import com.barnettwong.basemvp.api.Api;
import com.barnettwong.basemvp.api.HostType;
import com.barnettwong.basemvp.bean.MovieComment;
import com.barnettwong.basemvp.mvp.contract.MovieCommentContract;
import com.jaydenxiao.common.baserx.RxSchedulers;

import java.util.List;

import rx.Observable;

/**
 * Created by wang on 2018/8/8.
 **/
public class MovieCommentModel implements MovieCommentContract.Model{


    @Override
    public Observable<List<MovieComment>> getMovieComments() {
        return Api.getDefault(HostType.YINGPIN_TV)
                .getMovieReview()
                .map(resultBean -> resultBean)
                .compose(RxSchedulers.io_main());
    }
}
