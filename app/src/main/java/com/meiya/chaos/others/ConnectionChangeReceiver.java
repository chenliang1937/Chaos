package com.meiya.chaos.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.meiya.chaos.common.AppService;
import com.meiya.chaos.common.Constant;

/**
 * Created by chenliang3 on 2016/5/11.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()){
            AppService.getPrefser().put(Constant.PRE_CONNECTION_STATUS, true);
            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
        }else {
            AppService.getPrefser().put(Constant.PRE_CONNECTION_STATUS, false);
        }
    }
}
