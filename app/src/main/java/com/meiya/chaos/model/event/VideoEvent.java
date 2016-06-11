package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.VideoList;

/**
 * Created by chenliang3 on 2016/5/17.
 */
public class VideoEvent extends Event {

    private VideoList videoList;
    private Constant.GetWay getWay;

    public VideoEvent(VideoList videoList, Constant.GetWay getWay, Constant.Result result){
        this.videoList = videoList;
        this.getWay = getWay;
        this.eventResult = result;
    }

    public VideoList getVideoList() {
        return videoList;
    }

    public Constant.GetWay getGetWay() {
        return getWay;
    }
}
