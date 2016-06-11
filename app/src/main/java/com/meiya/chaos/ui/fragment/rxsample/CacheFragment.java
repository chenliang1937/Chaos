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
import android.widget.TextView;

import com.meiya.chaos.R;
import com.meiya.chaos.cache.Data;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.GankItem;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.adapter.GankAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class CacheFragment extends BaseFragment {

    @BindView(R.id.cache_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cache_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.cache_loadTimeTv)
    TextView loadTimeTv;

    GankAdapter adapter = new GankAdapter();
    private long startingTime;
    private Subscription subscription;

    @OnClick(R.id.clearMemoryCacheBt)
    void clearMemoryCache(){
        Data.getInstance().clearMemoryCache();
        adapter.setImages(null);
        Snackbar.make(swipeRefreshLayout, "内存缓存已清空", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.clearMemoryAndDiskCacheBt)
    void clearMemoryAndDiskCache(){
        Data.getInstance().clearMemoryAndDiskCache();
        adapter.setImages(null);
        Snackbar.make(swipeRefreshLayout, "内存缓存和磁盘缓存已清空", Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.cache_loadBtn)
    void load(){
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        subscription = Data.getInstance()
                .subscribeData(new Observer<List<GankItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(swipeRefreshLayout, "数据加载失败", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<GankItem> items) {
                        swipeRefreshLayout.setRefreshing(false);
                        int loadingTime = (int)(System.currentTimeMillis() - startingTime);
                        loadTimeTv.setText(getString(R.string.loading_time_and_source, loadingTime, Data.getInstance().getDataSourceText()));
                        adapter.setImages(items);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
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
