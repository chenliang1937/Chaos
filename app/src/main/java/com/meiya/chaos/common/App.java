package com.meiya.chaos.common;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.iflytek.cloud.SpeechUtility;
import com.meiya.chaos.R;
import com.meiya.chaos.others.GlideImageLoader;
import com.meiya.chaos.others.GlidePauseOnScrollListener;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by chenliang3 on 2016/4/12.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(this, "appid=5750d732");//科大讯飞
        super.onCreate();
        context = getApplicationContext();
        AppService.getInstance().initService();

        //捕获异常
//		CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        initGalleryFinal();
    }

    public static Context getContext(){
        return context;
    }

    private void initGalleryFinal(){
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.colorPrimaryDark))
                .setTitleBarTextColor(Color.WHITE)
                .build();

        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(9)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setEnableCamera(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        GlideImageLoader glideImageLoader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(context, glideImageLoader, theme)
                .setFunctionConfig(config)
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();

        GalleryFinal.init(coreConfig);
    }

}
