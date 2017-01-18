package com.devilo.sioextension;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static String TAG = NetworkChangeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkAvailable(context)) {
            Log.d(TAG, "is internet connected ");
            if (!SocketService.isStarted()) {
                context.startService(new Intent(context, SocketService.class));
            }
        } else {
            Log.d(TAG, "is internet not connected ");
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
