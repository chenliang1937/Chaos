package com.meiya.chaos.common;

/**
 * Created by chenliang3 on 2016/4/20.
 */
public class Constant {

    public enum Result{
        SUCCESS, FAIL, NORMAL;
    }

    public enum GetWay{
        INIT, REFRESH, LOADMORE;
    }

    public static final int CONTENT_CLICKED = 1000;//smart adapter点击事件
    public static final int VIDEO_RIPPLE_CLICKED = 1001;//视屏列表中的item详情 点击事件
    public static final int VIDEO_FAVO_CLICKED = 1002;//视屏列表中的item 收藏按钮 点击事件
    public static final int PIC_FLOW_CLICKED = 1003;//图片流item 点击事件
    public static final int SEARCH_IMAGE_CLICKED = 1004;//搜索图片item 点击事件

    public static final String PRE_CONNECTION_STATUS = "pre_connection_status";//网络连接状态SharePreference
    public static final String PRE_SPLASH_IMAGEURL = "pre_splash_imageurl";//欢迎界面图片的SharePreference
    public static final String PRE_PIC_FLOW_INDEX = "pre_pic_flow_index";//图片流的id
    public static final String PRE_PHOTO_PATH = "pre_photo_path";//选择的头像途径

}
