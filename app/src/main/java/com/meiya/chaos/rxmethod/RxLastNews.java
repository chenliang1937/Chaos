package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.NewsLatest;
import com.meiya.chaos.model.event.InitNewsEvent;
import com.meiya.chaos.model.event.LastNewsEvent;
import com.meiya.greendao.GreenNews;
import com.meiya.greendao.GreenNewsDao;

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
 * Created by chenliang3 on 2016/5/5.
 */
public class RxLastNews {

    public static Subscription getNewsFromDb(){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<NewsLatest>() {
            @Override
            public void call(Subscriber<? super NewsLatest> subscriber) {
                NewsLatest newsLatest = getCacheNews();
                subscriber.onNext(newsLatest);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<NewsLatest>() {
                    @Override
                    public void call(NewsLatest newsLatest) {
                        AppService.getBus().post(new InitNewsEvent(newsLatest, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new InitNewsEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static Subscription getLastNews(){
        Subscription subscription = AppService.getZhihuAPI().getLastNews()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<NewsLatest>() {
                    @Override
                    public void call(NewsLatest newsLatest) {
                        cacheNews(newsLatest);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsLatest>() {
                    @Override
                    public void call(NewsLatest newsLatest) {
                        AppService.getBus().post(new LastNewsEvent(newsLatest, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new LastNewsEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    public static void cacheNews(NewsLatest newsLatest) {
        GreenNewsDao greenNewsDao = AppService.getDbHelper().getDaoSession().getGreenNewsDao();
        DeleteQuery deleteQuery = greenNewsDao.queryBuilder()
                .where(GreenNewsDao.Properties.Date.eq(newsLatest.getDate()))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        String newsList = AppService.getGson().toJson(newsLatest);
        GreenNews greenNews = new GreenNews(null, newsLatest.getDate(), newsList);
        greenNewsDao.insert(greenNews);
    }

    private static NewsLatest getCacheNews() {
        NewsLatest newsLatest = null;
        GreenNewsDao greenNewsDao = AppService.getDbHelper().getDaoSession().getGreenNewsDao();
        Query query = greenNewsDao.queryBuilder()
                .limit(1)
                .orderDesc(GreenNewsDao.Properties.Id)
                .build();
        //查询结果以list返回
        List<GreenNews> greenNews = query.list();
        if (greenNews != null && greenNews.size() > 0){
            newsLatest = AppService.getGson().fromJson(greenNews.get(0).getNewslist(), NewsLatest.class);
        }
        return newsLatest;
    }

}
