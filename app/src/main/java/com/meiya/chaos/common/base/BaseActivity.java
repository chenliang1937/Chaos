package com.meiya.chaos.common.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meiya.chaos.R;
import com.meiya.chaos.common.AppManager;
import com.meiya.chaos.common.AppService;
import com.meiya.chaos.others.ConnectionChangeReceiver;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by chenliang3 on 2016/4/9.
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    public enum TransitionMode{
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    private ConnectionChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        switch (getOverridePendingTransitionMode()){
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in, R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            default:
                break;
        }
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);//恢复状态
        setContentView(getContentViewId());
        AppService.getInstance().addCompositeSub(getTaskId());
        AppService.getInstance().getBus().register(this);
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        registerReceiver();
        initViews();
    }

    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new ConnectionChangeReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);//存储状态
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppService.getInstance().removeCompositeSub(getTaskId());
        AppService.getBus().unregister(this);
        this.unregisterReceiver(receiver);
        System.runFinalization();//通知垃圾管理器打算进行垃圾回收，但是否回收是不确定的
        Runtime.getRuntime().gc();
        System.gc();
    }

    protected abstract int getContentViewId();
    protected abstract void initViews();
    protected abstract TransitionMode getOverridePendingTransitionMode();

    @Override
    public void finish() {
        super.finish();
        AppManager.getInstance().removeActivity(this);
        switch (getOverridePendingTransitionMode()){
            case LEFT:
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                break;
            case RIGHT:
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                break;
            case TOP:
                overridePendingTransition(R.anim.top_in,R.anim.top_out);
                break;
            case BOTTOM:
                overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                break;
            case SCALE:
                overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                break;
            case FADE:
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                break;

            default:
                break;
        }
    }

}
