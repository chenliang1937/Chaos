package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.NewsLatest;
import com.meiya.chaos.model.event.BeforeNewsEvent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/6.
 */
public class RxBeforeNews {

    public static Subscription getBeforeNews(String date){
        Subscription subscription = AppService.getZhihuAPI().getBeforeNews(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<NewsLatest>() {
                    @Override
                    public void call(NewsLatest newsLatest) {
                        AppService.getBus().post(new BeforeNewsEvent(newsLatest, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new BeforeNewsEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

}
