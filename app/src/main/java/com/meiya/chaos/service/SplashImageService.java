package com.meiya.chaos.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by chenliang3 on 2016/5/18.
 */
public class SplashImageService extends Service {

//    private Subscription subscription;
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        subscription = AppService.getZhihuAPI().getStartImg()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<StartImage>() {
//                    @Override
//                    public void call(StartImage startImage) {
//                        if (!startImage.getImg().equals(AppService.getPrefser().get(Constant.PRE_SPLASH_IMAGEURL, String.class, ""))){
//                            AppService.getPrefser().put(Constant.PRE_SPLASH_IMAGEURL, startImage.getImg());
//                            ImageView imageView = new ImageView(getApplicationContext());
//                            Glide.with(getApplicationContext())
//                                    .load(startImage.getImg())
//                                    .listener(new RequestListener<String, GlideDrawable>() {
//                                        @Override
//                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                            return false;
//                                        }
//
//                                        @Override
//                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            stopSelf();
//                                            return false;
//                                        }
//                                    })
//                                    .into(imageView);
//                        }
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        stopSelf();
//                    }
//                });
//
//        return super.onStartCommand(intent, flags, startId);
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        subscription.unsubscribe();
//        stopSelf();
//    }
}
