package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.NewsLatest;

/**
 * Created by chenliang3 on 2016/5/5.
 */
public class LastNewsEvent extends Event {

    private NewsLatest newsLatest;

    public LastNewsEvent(NewsLatest newsLatest, Constant.Result result){
        this.newsLatest = newsLatest;
        this.eventResult = result;
    }

    public NewsLatest getNewsLatest() {
        return newsLatest;
    }
}
