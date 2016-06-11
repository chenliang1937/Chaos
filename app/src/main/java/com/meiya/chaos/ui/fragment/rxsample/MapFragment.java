package com.meiya.chaos.ui.fragment.rxsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.meiya.chaos.R;
import com.meiya.chaos.api.ChaosClient;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.GankImage;
import com.meiya.chaos.model.GankItem;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.adapter.GankAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class MapFragment extends BaseFragment {

    @BindView(R.id.map_swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.map_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.map_pageTv)
    TextView pageTv;
    @BindView(R.id.map_prePageBtn)
    Button prePageBtn;

    GankAdapter adapter = new GankAdapter();
    private Subscription subscription;
    private int thePage = 0;

    @OnClick(R.id.map_prePageBtn)
    void prePage(){
        loadPage(--thePage);
        if (thePage == 1){
            prePageBtn.setEnabled(false);
        }
    }

    @OnClick(R.id.map_nextPageBtn)
    void nextPage(){
        loadPage(++thePage);
        if (thePage == 2){
            prePageBtn.setEnabled(true);
        }
    }

    private void loadPage(final int page){
        swipeRefreshLayout.setRefreshing(true);
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = ChaosClient.getGankAPI().getBeauties(10, page)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<GankItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(swipeRefreshLayout, "数据加载失败", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<GankItem> gankItems) {
                        swipeRefreshLayout.setRefreshing(false);
                        pageTv.setText(getString(R.string.page_with_number, thePage));
                        adapter.setImages(gankItems);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.BLACK);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void onEventMainThread(Event event){

    }
}
