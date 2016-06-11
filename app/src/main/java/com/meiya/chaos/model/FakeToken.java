package com.meiya.chaos.model;

/**
 * Created by chenliang3 on 2016/5/31.
 */
public class FakeToken {

    private String token;
    private boolean expired;

    public FakeToken(){

    }

    public FakeToken(boolean expired){
        this.expired = expired;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
