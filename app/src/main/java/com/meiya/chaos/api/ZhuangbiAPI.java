package com.meiya.chaos.api;

import com.meiya.chaos.model.ZhuangbiImage;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public interface ZhuangbiAPI {

    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);

}
