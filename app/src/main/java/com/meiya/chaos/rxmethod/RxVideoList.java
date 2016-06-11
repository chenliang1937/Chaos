package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.VideoList;
import com.meiya.chaos.model.event.VideoEvent;
import com.meiya.chaos.model.event.VideoInitEvent;
import com.meiya.greendao.GreenVideo;
import com.meiya.greendao.GreenVideoDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/17.
 */
public class RxVideoList {

    public static Subscription getVideosFromDb(){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<VideoList.Videos>>() {
            @Override
            public void call(Subscriber<? super List<VideoList.Videos>> subscriber) {
                List<VideoList.Videos> videosList = getCacheVideo();
                subscriber.onNext(videosList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<VideoList.Videos>>() {
                    @Override
                    public void call(List<VideoList.Videos> videoses) {
                        AppService.getBus().post(new VideoInitEvent(videoses, Constant.GetWay.INIT, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new VideoInitEvent(null, Constant.GetWay.INIT, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getVideoList(String cursor){
        Subscription subscription = AppService.getWangyiAPI().getVideoList(cursor)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<VideoList>() {
                    @Override
                    public void call(VideoList videoList) {
                        if (videoList != null && videoList.getV9LG4B3A0().size() > 0) {
                            for (int i = 0; i < videoList.getV9LG4B3A0().size(); i++) {
                                cacheVideos(videoList.getV9LG4B3A0().get(i));
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoList>() {
                    @Override
                    public void call(VideoList videoList) {
                        AppService.getBus().post(new VideoEvent(videoList, Constant.GetWay.REFRESH, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new VideoEvent(null, Constant.GetWay.REFRESH, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getMoreVideos(String cursor){
        Subscription subscription = AppService.getWangyiAPI().getVideoList(cursor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<VideoList>() {
                    @Override
                    public void call(VideoList videoList) {
                        AppService.getBus().post(new VideoEvent(videoList, Constant.GetWay.LOADMORE, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new VideoEvent(null, Constant.GetWay.LOADMORE, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static void cacheVideos(VideoList.Videos videos){
        GreenVideoDao greenVideoDao = AppService.getDbHelper().getDaoSession().getGreenVideoDao();
        DeleteQuery deleteQuery = greenVideoDao.queryBuilder()
                .where(GreenVideoDao.Properties.Vid.eq(videos.getVid()))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        String tmpVideo = AppService.getGson().toJson(videos);
        GreenVideo greenVideo = new GreenVideo(null, videos.getVid(), tmpVideo);
        greenVideoDao.insert(greenVideo);
    }

    public static List<VideoList.Videos> getCacheVideo(){
        List<VideoList.Videos> list = new ArrayList<>();
        GreenVideoDao greenVideoDao = AppService.getDbHelper().getDaoSession().getGreenVideoDao();
        Query query = greenVideoDao.queryBuilder()
                .limit(10)
                .orderDesc(GreenVideoDao.Properties.Id)
                .build();
        List<GreenVideo> greenVideos = query.list();
        if (greenVideos != null && greenVideos.size() > 0){
            for (int i=0; i<greenVideos.size(); i++){
                list.add(AppService.getGson().fromJson(greenVideos.get(i).getVideolist(), VideoList.Videos.class));
            }
        }
        Collections.reverse(list);
        return list;
    }

    public static void deleteAll(){
        GreenVideoDao greenVideoDao = AppService.getDbHelper().getDaoSession().getGreenVideoDao();
        DeleteQuery deleteQuery = greenVideoDao.queryBuilder()
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

}
