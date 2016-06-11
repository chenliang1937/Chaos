package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.StartImage;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class StartImgEvent extends Event {

    private StartImage mStartImage;

    public StartImgEvent(StartImage image, Constant.Result result){
        this.mStartImage = image;
        this.eventResult = result;
    }

    public StartImage getmStartImage(){
        return mStartImage;
    }

}
