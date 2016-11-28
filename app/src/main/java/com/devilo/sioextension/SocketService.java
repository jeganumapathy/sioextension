package com.devilo.sioextension;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 *
 */
public class SocketService extends Service {
    SocketManager manager;
    String ip = "http://192.168.0.68:3000/user";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        manager = new SocketManager(getApplicationContext(), callBack, ip);
        manager.connect();
    }

    SocketManager.SocketCallBack callBack = new SocketManager.SocketCallBack() {
        @Override
        public void onSuccessListener(String eventName, Object response) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
