package com.meiya.chaos.api;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class ChaosFactory {

    private static ZhihuAPI zhihuAPI = null;
    private static WangyiAPI wangyiAPI = null;
    private static WangpicAPI wangpicAPI = null;
    private static SogouAPI sogouAPI = null;
    private static final Object WATCH_DOG = new Object();

    private ChaosFactory(){

    }

    public static ZhihuAPI getZhihuAPI(){
        synchronized (WATCH_DOG){
            if (zhihuAPI == null){
                ChaosClient chaosClient = new ChaosClient();
                zhihuAPI = chaosClient.getZhihuClient();
            }
            return zhihuAPI;
        }
    }

    public static WangyiAPI getWangyiAPI(){
        synchronized (WATCH_DOG){
            if (wangyiAPI == null){
                ChaosClient chaosClient = new ChaosClient();
                wangyiAPI = chaosClient.getWangyiClient();
            }
            return wangyiAPI;
        }
    }

    public static WangpicAPI getWangpicAPI() {
        synchronized (WATCH_DOG){
            if (wangpicAPI == null){
                ChaosClient chaosClient = new ChaosClient();
                wangpicAPI = chaosClient.getWangpicAPI();
            }
            return wangpicAPI;
        }
    }

    public static SogouAPI getSogouAPI(){
        synchronized (WATCH_DOG){
            if (sogouAPI == null){
                ChaosClient chaosClient = new ChaosClient();
                sogouAPI = chaosClient.getSogouAPI();
            }
            return sogouAPI;
        }
    }
}
