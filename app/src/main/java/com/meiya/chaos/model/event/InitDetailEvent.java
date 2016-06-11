package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;
import com.meiya.chaos.model.NewsDetail;

/**
 * Created by chenliang3 on 2016/5/6.
 */
public class InitDetailEvent extends Event {

    private NewsDetail newsDetail;

    public InitDetailEvent(NewsDetail newsDetail, Constant.Result result){
        this.newsDetail = newsDetail;
        this.eventResult = result;
    }

    public NewsDetail getNewsDetail() {
        return newsDetail;
    }
}
