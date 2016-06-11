package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.NewsDetail;
import com.meiya.chaos.model.event.InitDetailEvent;
import com.meiya.chaos.model.event.NewsDetailEvent;
import com.meiya.greendao.GreenDetail;
import com.meiya.greendao.GreenDetailDao;

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
 * Created by chenliang3 on 2016/5/6.
 */
public class RxNewsDetail {

    public static Subscription getNewsDetailFromDb(final int newsId){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<NewsDetail>() {
            @Override
            public void call(Subscriber<? super NewsDetail> subscriber) {
                NewsDetail newsDetail = getCacheDetail(newsId);
                subscriber.onNext(newsDetail);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        AppService.getBus().post(new InitDetailEvent(newsDetail, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new InitDetailEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getNewsDetail(int newsId){
        Subscription subscription = AppService.getZhihuAPI().getNewsDetail(newsId)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        cacheDetail(newsDetail);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsDetail>() {
                    @Override
                    public void call(NewsDetail newsDetail) {
                        AppService.getBus().post(new NewsDetailEvent(newsDetail, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new NewsDetailEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static void cacheDetail(NewsDetail detail){
        GreenDetailDao greenDetailDao = AppService.getDbHelper().getDaoSession().getGreenDetailDao();
        DeleteQuery deleteQuery = greenDetailDao.queryBuilder()
                .where(GreenDetailDao.Properties.NewsId.eq(detail.getId()))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        String detailStr = AppService.getGson().toJson(detail);
        GreenDetail greenDetail = new GreenDetail(null, detail.getId(), detailStr);
        greenDetailDao.insert(greenDetail);
    }

    public static NewsDetail getCacheDetail(int id){
        NewsDetail detail = null;
        GreenDetailDao greenDetailDao = AppService.getDbHelper().getDaoSession().getGreenDetailDao();
        Query query = greenDetailDao.queryBuilder()
                .where(GreenDetailDao.Properties.NewsId.eq(id))
                .build();
        List<GreenDetail> list = query.list();
        if (list != null && list.size() > 0){
            detail = AppService.getGson().fromJson(list.get(0).getDetails(), NewsDetail.class);
        }
        return detail;
    }

}
