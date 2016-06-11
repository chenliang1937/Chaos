package com.meiya.chaos.api;

import android.support.annotation.NonNull;

import com.meiya.chaos.model.FakeThing;
import com.meiya.chaos.model.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public class FakeAPI {

    Random random = new Random();

    public Observable<FakeToken> getFakeToken(@NonNull String fakeAuth){
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String s) {
                        // Add some random delay to mock the network delay
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        FakeToken fakeToken = new FakeToken();
                        fakeToken.setToken(createToken());
                        return fakeToken;
                    }
                });
    }

    private static String createToken(){
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }

    public Observable<FakeThing> getFakeData(FakeToken fakeToken){
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                        if (fakeToken.isExpired()){
                            throw new IllegalArgumentException("Token expired!");
                        }

                        FakeThing fakeData = new FakeThing();
                        fakeData.setId((int)(System.currentTimeMillis() % 1000));
                        fakeData.setName("FAKE_USER_" + fakeData.getId());
                        return fakeData;
                    }
                });
    }

}
