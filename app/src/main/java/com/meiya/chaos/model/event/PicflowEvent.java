package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.PictureFlow;

import java.util.List;

/**
 * Created by chenliang3 on 2016/5/20.
 */
public class PicflowEvent extends Event {

    private List<PictureFlow> pictureFlow;
    private Constant.GetWay getWay;

    public PicflowEvent(List<PictureFlow> pictureFlow, Constant.GetWay getWay, Constant.Result result){
        this.pictureFlow = pictureFlow;
        this.getWay = getWay;
        this.eventResult = result;
    }

    public List<PictureFlow> getPictureFlow() {
        return pictureFlow;
    }

    public Constant.GetWay getGetWay() {
        return getWay;
    }
}
