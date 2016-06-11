package com.meiya.chaos.rxmethod;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.StartImage;
import com.meiya.chaos.model.event.StartImgEvent;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class RxStartImg {

    public static Subscription getStartImg(){
        Subscription subscription = AppService.getZhihuAPI().getStartImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StartImage>() {
                    @Override
                    public void call(StartImage startImage) {
                        AppService.getBus().post(new StartImgEvent(startImage, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new StartImgEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

}
