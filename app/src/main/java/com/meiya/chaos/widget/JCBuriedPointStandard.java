package com.meiya.chaos.widget;

/**
 * Created by chenliang3 on 2016/5/14.
 */
public interface JCBuriedPointStandard extends JCBuriedPoint {

    void onClickStartThumb(String url, Object... objects);

    void onClickBlank(String url, Object... objects);

    void onClickBlankFullscreen(String url, Object... objects);

}
