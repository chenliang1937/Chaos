package com.meiya.chaos.common;

import android.os.Handler;
import android.os.HandlerThread;

import com.github.pwittchen.prefser.library.Prefser;
import com.google.gson.Gson;
import com.meiya.chaos.api.ChaosFactory;
import com.meiya.chaos.api.SogouAPI;
import com.meiya.chaos.api.WangpicAPI;
import com.meiya.chaos.api.WangyiAPI;
import com.meiya.chaos.api.ZhihuAPI;
import com.meiya.chaos.rxmethod.RxBeforeNews;
import com.meiya.chaos.rxmethod.RxLastNews;
import com.meiya.chaos.rxmethod.RxNewsDetail;
import com.meiya.chaos.rxmethod.RxPicFlow;
import com.meiya.chaos.rxmethod.RxSogouImg;
import com.meiya.chaos.rxmethod.RxStartImg;
import com.meiya.chaos.rxmethod.RxVideoList;
import com.meiya.greendao.DBHelper;
import com.squareup.okhttp.OkHttpClient;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class AppService {

    private static final AppService CHAOS_SERVICE = new AppService();
    private static Gson gson;
    private static EventBus eventBus;
    private static ZhihuAPI zhihuAPI;
    private static WangyiAPI wangyiAPI;
    private static WangpicAPI wangpicAPI;
    private static SogouAPI sogouAPI;
    private static DBHelper dbHelper;
    private Map<Integer, CompositeSubscription> compositeSubByTaskId;
    private Handler handler;
    private static Prefser prefser;
    private static OkHttpClient client;

    private AppService(){

    }

    public static AppService getInstance(){
        return CHAOS_SERVICE;
    }

    public void initService(){
        eventBus = EventBus.getDefault();
        gson = new Gson();
        compositeSubByTaskId = new HashMap<>();
        prefser = new Prefser(App.getContext());
        client = new OkHttpClient();
        backGroundInit();
    }

    private void backGroundInit(){
        HandlerThread ioThread = new HandlerThread("IoThread");
        ioThread.start();
        handler = new Handler(ioThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                zhihuAPI = ChaosFactory.getZhihuAPI();
                wangyiAPI = ChaosFactory.getWangyiAPI();
                wangpicAPI = ChaosFactory.getWangpicAPI();
                sogouAPI = ChaosFactory.getSogouAPI();
                dbHelper = DBHelper.getInstance(App.getContext());
            }
        });
    }

    public void addCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }
    }

    public void removeCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId != null && compositeSubByTaskId.get(taskId) != null){
            compositeSubscription = compositeSubByTaskId.get(taskId);
            compositeSubscription.unsubscribe();
            compositeSubByTaskId.remove(taskId);
        }
    }

    private CompositeSubscription getCompositeSubscription(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }else {
            compositeSubscription = compositeSubByTaskId.get(taskId);
        }
        return compositeSubscription;
    }

    public static EventBus getBus(){
        return eventBus;
    }

    public static ZhihuAPI getZhihuAPI(){
        return zhihuAPI;
    }

    public static WangyiAPI getWangyiAPI(){
        return wangyiAPI;
    }

    public static WangpicAPI getWangpicAPI() {
        return wangpicAPI;
    }

    public static SogouAPI getSogouAPI(){
        return sogouAPI;
    }

    public static Gson getGson(){
        return gson;
    }

    public static DBHelper getDbHelper(){
        return dbHelper;
    }

    public static Prefser getPrefser(){
        return prefser;
    }

    public OkHttpClient getOkhttpClient(){
        return client;
    }

    public void getStartImage(int taskId){
        getCompositeSubscription(taskId).add(RxStartImg.getStartImg());
    }

    public void getLastNews(int taskId){
        getCompositeSubscription(taskId).add(RxLastNews.getLastNews());
    }

    public void getNewFromDb(int taskId){
        getCompositeSubscription(taskId).add(RxLastNews.getNewsFromDb());
    }

    public void getBeforeNews(int taskId, String date){
        getCompositeSubscription(taskId).add(RxBeforeNews.getBeforeNews(date));
    }

    public void getNewsDetail(int taskId, int newsId){
        getCompositeSubscription(taskId).add(RxNewsDetail.getNewsDetail(newsId));
    }

    public void getNewsDetailFromDb(int taskId, int newsId){
        getCompositeSubscription(taskId).add(RxNewsDetail.getNewsDetailFromDb(newsId));
    }

    public void getVideoList(int taskId, String cursor){
        getCompositeSubscription(taskId).add(RxVideoList.getVideoList(cursor));
    }

    public void getMoreVideos(int taskId, String cursor){
        getCompositeSubscription(taskId).add(RxVideoList.getMoreVideos(cursor));
    }

    public void getVideosFromDb(int taskId){
        getCompositeSubscription(taskId).add(RxVideoList.getVideosFromDb());
    }

    public void getPicFlow(int taskId, String indexId){
        getCompositeSubscription(taskId).add(RxPicFlow.getPicflow(indexId));
    }

    public void getMorePic(int taskId, String indexId){
        getCompositeSubscription(taskId).add(RxPicFlow.loadMorePics(indexId));
    }

    public void getPicsFromDb(int taskId){
        getCompositeSubscription(taskId).add(RxPicFlow.getPicsFromDb());
    }

    public void getSogouImg(int taskId, String reqType, String reqFrom, String query, int page){
        getCompositeSubscription(taskId).add(RxSogouImg.getSogouImg(reqType, reqFrom, query, page));
    }

    public void getMoreSogouImg(int taskId, String reqType, String reqFrom, String query, int page){
        getCompositeSubscription(taskId).add(RxSogouImg.getMoreSogouImg(reqType, reqFrom, query, page));
    }

}
