package com.meiya.chaos.ui.fragment.rxsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meiya.chaos.R;
import com.meiya.chaos.api.ChaosClient;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.GankImage;
import com.meiya.chaos.model.GankItem;
import com.meiya.chaos.model.ZhuangbiImage;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.adapter.GankAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class ZipFragment extends BaseFragment {

    @BindView(R.id.zip_swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.zip_recyclerview)
    RecyclerView recyclerView;

    GankAdapter adapter = new GankAdapter();
    private Subscription subscription;

    @OnClick(R.id.zip_loadBtn)
    void load(){
        swipeRefreshLayout.setRefreshing(true);
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        subscription = Observable.zip(
                ChaosClient.getGankAPI().getBeauties(200, 1).map(new Func1<GankImage, List<GankItem>>() {
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
                }),
                ChaosClient.getZhuangbiAPI().search("装逼"),
                new Func2<List<GankItem>, List<ZhuangbiImage>, List<GankItem>>() {
                    @Override
                    public List<GankItem> call(List<GankItem> gankItems, List<ZhuangbiImage> zhuangbiImages) {
                        List<GankItem> items = new ArrayList<GankItem>();
                        for (int i = 0; i < gankItems.size() / 2 && i < zhuangbiImages.size(); i++){
                            items.add(gankItems.get(i * 2));
                            items.add(gankItems.get(i * 2 + 1));
                            GankItem item = new GankItem();
                            ZhuangbiImage image = zhuangbiImages.get(i);
                            item.setDescription(image.getDescription());
                            item.setImageUrl(image.getImage_url());
                            items.add(item);
                        }
                        return items;
                    }
                }
        )
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
                        adapter.setImages(gankItems);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zip, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
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
