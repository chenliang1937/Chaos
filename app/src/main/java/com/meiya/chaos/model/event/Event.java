package com.meiya.chaos.model.event;

import com.meiya.chaos.common.Constant;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class Event {

    protected Constant.Result eventResult;

    public Constant.Result getEventResult() {
        return eventResult;
    }

    public void setEventResult(Constant.Result eventResult) {
        this.eventResult = eventResult;
    }
}
