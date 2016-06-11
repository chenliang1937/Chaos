package com.meiya.chaos.cache;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.meiya.chaos.api.ChaosClient;
import com.meiya.chaos.model.GankImage;
import com.meiya.chaos.model.GankItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public class Data {

    private static Data instance;
    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;
    @IntDef({DATA_SOURCE_MEMORY, DATA_SOURCE_DISK, DATA_SOURCE_NETWORK}) @interface DataSource{}

    BehaviorSubject<List<GankItem>> cache;

    private int dataSource;

    private Data(){

    }

    public static Data getInstance(){
        if (instance == null){
            instance = new Data();
        }
        return instance;
    }

    private void setDataSource(@DataSource int dataSource){
        this.dataSource = dataSource;
    }

    public String getDataSourceText(){
        String dataSourceText;
        switch (dataSource){
            case DATA_SOURCE_MEMORY:
                dataSourceText = "内存";
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceText = "网络";
                break;
            case DATA_SOURCE_DISK:
                dataSourceText = "磁盘";
                break;
            default:
                dataSourceText = "网络";
        }
        return dataSourceText;
    }

    public void loadFromNetwork(){
        ChaosClient.getGankAPI()
                .getBeauties(100, 1)
                .subscribeOn(Schedulers.io())
                .map(new Func1<GankImage, List<GankItem>>() {
                    @Override
                    public List<GankItem> call(GankImage gankImage) {
                        List<GankImage.theResults> resultses = gankImage.getResults();
                        List<GankItem> items = new ArrayList<>(resultses.size());
                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
                        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                        for (GankImage.theResults results : resultses){
                            GankItem item = new GankItem();
                            try{
                                Date date = inputFormat.parse(results.getCreatedAt());
                                item.setDescription(outputFormat.format(date));
                            }catch (ParseException e){
                                e.printStackTrace();
                                item.setDescription("unknown date");
                            }
                            item.setImageUrl(results.getUrl());
                            items.add(item);
                        }
                        return items;
                    }
                })
                .doOnNext(new Action1<List<GankItem>>() {
                    @Override
                    public void call(List<GankItem> items) {
                        Database.getInstance().writeItems(items);
                    }
                })
                .subscribe(new Action1<List<GankItem>>() {
                    @Override
                    public void call(List<GankItem> items) {
                        cache.onNext(items);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public Subscription subscribeData(@NonNull Observer<List<GankItem>> observer){
        if (cache == null){
            cache = BehaviorSubject.create();
            Observable.create(new Observable.OnSubscribe<List<GankItem>>(){

                @Override
                public void call(Subscriber<? super List<GankItem>> subscriber) {
                    List<GankItem> items = Database.getInstance().readItems();
                    if (items == null){
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetwork();
                    }else {
                        setDataSource(DATA_SOURCE_DISK);
                        subscriber.onNext(items);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(cache);
        }else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return cache.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    public void clearMemoryCache(){
        cache = null;
    }

    public void clearMemoryAndDiskCache(){
        clearMemoryCache();
        Database.getInstance().delete();
    }

}
