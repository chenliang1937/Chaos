package com.meiya.chaos.api;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class ChaosClient {

    private ZhihuAPI zhihuAPI;
    private WangyiAPI wangyiAPI;
    private WangpicAPI wangpicAPI;
    private static ZhuangbiAPI zhuangbiAPI;
    private static GankAPI gankAPI;
    private static FakeAPI fakeAPI;
    private static SogouAPI sogouAPI;

    public ChaosClient(){
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/api/4/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://c.3g.163.com/nc/video/list/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit3 = new Retrofit.Builder()
                .baseUrl("http://c.m.163.com/photo/api/morelist/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit4 = new Retrofit.Builder()
                .baseUrl("http://zhuangbi.info/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit5 = new Retrofit.Builder()
                .baseUrl("http://gank.io/api/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit6 = new Retrofit.Builder()
                .baseUrl("http://pic.sogou.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        zhihuAPI = retrofit1.create(ZhihuAPI.class);
        wangyiAPI = retrofit2.create(WangyiAPI.class);
        wangpicAPI = retrofit3.create(WangpicAPI.class);
        zhuangbiAPI = retrofit4.create(ZhuangbiAPI.class);
        gankAPI = retrofit5.create(GankAPI.class);
        sogouAPI = retrofit6.create(SogouAPI.class);
    }

    public ZhihuAPI getZhihuClient(){
        return zhihuAPI;
    }

    public WangyiAPI getWangyiClient() {
        return wangyiAPI;
    }

    public WangpicAPI getWangpicAPI() {
        return wangpicAPI;
    }

    public static ZhuangbiAPI getZhuangbiAPI(){
        return zhuangbiAPI;
    }

    public static GankAPI getGankAPI(){
        return gankAPI;
    }

    public static FakeAPI getFakeAPI(){
        if (fakeAPI == null){
            fakeAPI = new FakeAPI();
        }
        return fakeAPI;
    }

    public SogouAPI getSogouAPI(){
        return sogouAPI;
    }
}
