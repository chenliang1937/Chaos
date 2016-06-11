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
public class TokenAdvancedFragment extends BaseFragment {

    @BindView(R.id.advance_swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.advanceTv)
    TextView tokenTv;
    
    FakeToken cachedFakeToken = new FakeToken(true);
    boolean tokenUpdated;
    private Subscription subscription;
    
    @OnClick(R.id.advance_destoryBtn)
    void invalidateToken(){
        cachedFakeToken.setExpired(true);
        Snackbar.make(swipeRefreshLayout, "token 已销毁", Snackbar.LENGTH_SHORT).show();
    }
    
    @OnClick(R.id.advance_loadBtn)
    void upload(){
        tokenUpdated = false;
        swipeRefreshLayout.setRefreshing(true);
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }

        final FakeAPI fakeAPI = ChaosClient.getFakeAPI();
        subscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cachedFakeToken.getToken() == null
                                ? Observable.<FakeThing>error(new NullPointerException("token is null!"))
                                : fakeAPI.getFakeData(cachedFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Throwable> observable) {
                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException){
                                    return fakeAPI.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdated = true;
                                                    cachedFakeToken.setToken(fakeToken.getToken());
                                                    cachedFakeToken.setExpired(fakeToken.isExpired());
                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cachedFakeToken.getToken();
                        if (tokenUpdated){
                            token += "(已更新)";
                        }
                        tokenTv.setText(getString(R.string.got_token_and_data, token, fakeThing.getId(), fakeThing.getName()));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(swipeRefreshLayout, "数据加载失败", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tokenadvanced, container, false);
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
