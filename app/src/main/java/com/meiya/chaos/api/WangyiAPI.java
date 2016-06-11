package com.meiya.chaos.api;

import com.meiya.chaos.model.VideoList;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/5/17.
 */
public interface WangyiAPI {

    //热点视屏api
    @GET("V9LG4B3A0/n/{cursor}-10.html")
    Observable<VideoList> getVideoList(@Path("cursor") String cursor);

}
