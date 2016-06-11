package com.meiya.chaos.common.base;

import android.os.Bundle;

import com.jude.swipbackhelper.SwipeBackHelper;

/**
 * Created by chenliang3 on 2016/6/2.
 */
public abstract class SwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);//滑动返回
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
