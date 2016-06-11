package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.PictureFlow;
import com.meiya.chaos.model.event.PicflowEvent;
import com.meiya.greendao.GreenPicflow;
import com.meiya.greendao.GreenPicflowDao;

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
 * Created by chenliang3 on 2016/5/20.
 */
public class RxPicFlow {

    public static Subscription getPicsFromDb(){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<PictureFlow>>() {
            @Override
            public void call(Subscriber<? super List<PictureFlow>> subscriber) {
                List<PictureFlow> pictureFlows = getCachePicflow();
                subscriber.onNext(pictureFlows);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<List<PictureFlow>>() {
                    @Override
                    public void call(List<PictureFlow> flows) {
                        AppService.getBus().post(new PicflowEvent(flows, Constant.GetWay.INIT, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new PicflowEvent(null, Constant.GetWay.INIT, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getPicflow(String indexId){
        Subscription subscription = AppService.getWangpicAPI().getPicFlow(indexId)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<List<PictureFlow>>() {
                    @Override
                    public void call(List<PictureFlow> flows) {
                        if (flows != null && flows.size() > 0) {
                            for (int i = 0; i < flows.size(); i++) {
                                cachePicflow(flows.get(i));
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PictureFlow>>() {
                    @Override
                    public void call(List<PictureFlow> flows) {
                        AppService.getBus().post(new PicflowEvent(flows, Constant.GetWay.REFRESH, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new PicflowEvent(null, Constant.GetWay.REFRESH, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription loadMorePics(String indexId){
        Subscription subscription = AppService.getWangpicAPI().getPicFlow(indexId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PictureFlow>>() {
                    @Override
                    public void call(List<PictureFlow> flows) {
                        AppService.getBus().post(new PicflowEvent(flows, Constant.GetWay.LOADMORE, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new PicflowEvent(null, Constant.GetWay.LOADMORE, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static void cachePicflow(PictureFlow pictureFlow){
        GreenPicflowDao greenPicflowDao = AppService.getDbHelper().getDaoSession().getGreenPicflowDao();
        DeleteQuery deleteQuery = greenPicflowDao.queryBuilder()
                .where(GreenPicflowDao.Properties.Setid.eq(pictureFlow.getSetid()))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        String picflow = AppService.getGson().toJson(pictureFlow);
        GreenPicflow greenPicflow = new GreenPicflow(null, pictureFlow.getSetid(), picflow);
        greenPicflowDao.insert(greenPicflow);
    }

    public static List<PictureFlow> getCachePicflow(){
        List<PictureFlow> list = new ArrayList<>();
        GreenPicflowDao greenPicflowDao = AppService.getDbHelper().getDaoSession().getGreenPicflowDao();
        Query query = greenPicflowDao.queryBuilder()
                .limit(10)
                .orderDesc(GreenPicflowDao.Properties.Id)
                .build();
        List<GreenPicflow> greenPicflows = query.list();
        if (greenPicflows != null && greenPicflows.size() > 0){
            for (int i=0; i<greenPicflows.size(); i++){
                list.add(AppService.getGson().fromJson(greenPicflows.get(i).getPicflow(), PictureFlow.class));
            }
        }
        Collections.reverse(list);
        return list;
    }

    public static void deleteAll(){
        GreenPicflowDao greenPicflowDao = AppService.getDbHelper().getDaoSession().getGreenPicflowDao();
        DeleteQuery deleteQuery = greenPicflowDao.queryBuilder()
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
    }

}
