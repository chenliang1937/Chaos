package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.VideoList;

import java.util.List;

/**
 * Created by chenliang3 on 2016/5/24.
 */
public class VideoInitEvent extends Event {

    private List<VideoList.Videos> videosList;
    private Constant.GetWay getWay;

    public VideoInitEvent(List<VideoList.Videos> videosList, Constant.GetWay getWay, Constant.Result result){
        this.videosList = videosList;
        this.getWay = getWay;
        this.eventResult = result;
    }

    public List<VideoList.Videos> getVideosList() {
        return videosList;
    }

    public Constant.GetWay getGetWay() {
        return getWay;
    }
}
