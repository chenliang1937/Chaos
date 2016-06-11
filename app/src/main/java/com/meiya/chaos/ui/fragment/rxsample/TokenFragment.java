package com.meiya.chaos.ui.fragment.rxsample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meiya.chaos.R;
import com.meiya.chaos.api.ChaosClient;
import com.meiya.chaos.api.FakeAPI;
import com.meiya.chaos.common.base.BaseFragment;
import com.meiya.chaos.model.FakeThing;
import com.meiya.chaos.model.FakeToken;
import com.meiya.chaos.model.event.Event;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/5/30.
 */
public class TokenFragment extends BaseFragment {

    @BindView(R.id.token_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tokenTv)
    TextView tokenTv;

    private Subscription subscription;

    @OnClick(R.id.token_loadBtn)
    void loadCls(){
        swipeRefreshLayout.setRefreshing(true);
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
        final FakeAPI fakeAPI = ChaosClient.getFakeAPI();
        subscription = fakeAPI.getFakeToken("fake_auth_code")
                .flatMap(new Func1<FakeToken, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(FakeToken fakeToken) {
                        return fakeAPI.getFakeData(fakeToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        tokenTv.setText(getString(R.string.got_data, fakeThing.getId(), fakeThing.getName()));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(swipeRefreshLayout, "加载失败", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
