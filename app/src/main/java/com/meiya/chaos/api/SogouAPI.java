package com.meiya.chaos.api;

import com.meiya.chaos.model.SogouImage;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/6/3.
 */
public interface SogouAPI {

    @GET("pics")
    Observable<SogouImage> getSogouImgs(@Query("reqType") String reqType, @Query("reqFrom") String reqFrom, @Query("query") String query, @Query("page") int page);

}
