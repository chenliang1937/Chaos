package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.SogouImage;
import com.meiya.chaos.model.event.SogouImageEvent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/6/4.
 */
public class RxSogouImg {

    public static Subscription getSogouImg(String reqType, String reqFrom, String query, int page){
        Subscription subscription = AppService.getSogouAPI().getSogouImgs(reqType, reqFrom, query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SogouImage>() {
                    @Override
                    public void call(SogouImage sogouImage) {
                        AppService.getBus().post(new SogouImageEvent(sogouImage, Constant.GetWay.REFRESH, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new SogouImageEvent(null, Constant.GetWay.REFRESH, Constant.Result.SUCCESS));
                    }
                });
        return subscription;
    }

    public static Subscription getMoreSogouImg(String reqType, String reqFrom, String query, int page){
        Subscription subscription = AppService.getSogouAPI().getSogouImgs(reqType, reqFrom, query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SogouImage>() {
                    @Override
                    public void call(SogouImage sogouImage) {
                        AppService.getBus().post(new SogouImageEvent(sogouImage, Constant.GetWay.LOADMORE, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new SogouImageEvent(null, Constant.GetWay.LOADMORE, Constant.Result.SUCCESS));
                    }
                });
        return subscription;
    }

}
