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
import android.widget.RadioButton;

import com.meiya.chaos.R;
import com.meiya.chaos.api.ChaosClient;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.ZhuangbiImage;
import com.meiya.chaos.model.event.Event;
import com.meiya.chaos.ui.adapter.ZhuangbiAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class ElementaryFragment extends BaseFragment {

    @BindView(R.id.elementary_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.elementary_recyclerview)
    RecyclerView recyclerView;

    private Subscription subscription;
    private ZhuangbiAdapter adapter = new ZhuangbiAdapter();

    private void search(String key){
        subscription = ChaosClient.getZhuangbiAPI()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ZhuangbiImage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(recyclerView, "数据加载失败", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<ZhuangbiImage> zhuangbiImages) {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.setImages(zhuangbiImages);
                    }
                });
    }

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked){
        if (checked){
            if (subscription != null && !subscription.isUnsubscribed()){
                subscription.unsubscribe();
            }
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
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
