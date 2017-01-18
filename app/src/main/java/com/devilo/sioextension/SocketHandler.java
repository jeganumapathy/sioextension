package com.devilo.sioextension;

import android.content.Context;

/**
 * Connect to different Namespace for a scoket and connect
 */
public class SocketHandler {

    public static SocketManager manager;

    public static void createAndConnect(Context context, String ip, SocketManager.SocketCallBack callBack) {
        if (manager != null) {
            manager = new SocketManager(context, callBack, ip);
        }
        manager.connect();
    }

}
