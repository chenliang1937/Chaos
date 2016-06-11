package com.meiya.chaos.api;

import com.meiya.chaos.model.PictureFlow;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/5/20.
 */
public interface WangpicAPI {

    @GET("0096/4GJ60096/{indexId}.json")
    Observable<List<PictureFlow>> getPicFlow(@Path("indexId") String indexId);

}
