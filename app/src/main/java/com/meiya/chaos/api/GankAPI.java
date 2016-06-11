package com.meiya.chaos.api;

import com.meiya.chaos.model.GankImage;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public interface GankAPI {

    @GET("data/福利/{number}/{page}")
    Observable<GankImage> getBeauties(@Path("number") int number, @Path("page") int page);

}
