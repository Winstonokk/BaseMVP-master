package com.barnettwong.basemvp.api;

import com.barnettwong.basemvp.bean.MovieComment;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 */
public interface ApiService {

    @GET("MobileMovie/Review.api?needTop=false")
    Observable<List<MovieComment>> getMovieReview();//影评

}
