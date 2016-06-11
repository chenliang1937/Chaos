package com.meiya.chaos.api;

import com.meiya.chaos.model.NewsDetail;
import com.meiya.chaos.model.NewsLatest;
import com.meiya.chaos.model.StartImage;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public interface ZhihuAPI {

    @GET("start-image/720*1184")
    Observable<StartImage> getStartImg();
    @GET("news/latest")
    Observable<NewsLatest> getLastNews();
    @GET("news/before/{date}")
    Observable<NewsLatest> getBeforeNews(@Path("date") String date);
    @GET("news/{newsId}")
    Observable<NewsDetail> getNewsDetail(@Path("newsId") int newsId);

}
