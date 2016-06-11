package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.SogouImage;

/**
 * Created by chenliang3 on 2016/6/4.
 */
public class SogouImageEvent extends Event {

    private SogouImage sogouImage;
    private Constant.GetWay getWay;

    public SogouImageEvent(SogouImage image, Constant.GetWay getWay, Constant.Result result){
        this.sogouImage = image;
        this.getWay = getWay;
        this.eventResult = result;
    }

    public SogouImage getSogouImages() {
        return sogouImage;
    }

    public Constant.GetWay getGetWay() {
        return getWay;
    }
}
