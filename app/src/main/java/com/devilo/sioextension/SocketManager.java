package com.devilo.sioextension;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 */
public class SocketManager implements SocketEvent, SocketListener {

    private Activity activity;
    private SocketCallBack callBack;
    private HashSet<Object> uniqueBucket;
    private boolean isAdded = false;
    private boolean isConnected;
    private String ip;
    private Context context;
    private SocketParser parser;
    private static Socket mSocket;
    private ArrayList<Emitter.Listener> listener;


    public static interface SocketCallBack {
        void onSuccessListener(String eventName, Object response);
    }


    public SocketManager(Activity activity, SocketCallBack callBack, String ip) {
        this.activity = activity;
        this.callBack = callBack;
        this.ip = ip;
        getSocketIp();
    }


    public SocketManager(Context activity, SocketCallBack callBack, String ip) {
        this.callBack = callBack;
        this.context = activity;
        this.ip = ip;
        getSocketIp();
    }

    private void getSocketIp() {
        this.listener = new ArrayList<Emitter.Listener>();
        this.parser = new SocketParser();
        try {
            if (mSocket != null) {
                mSocket.close();
                mSocket.off();
                mSocket = null;
            }
            mSocket = IO.socket(ip);
            mSocket.io().reconnection(true);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void addSocketListeners() {
        Field[] events = SocketEvent.class.getFields();
        for (Field event : events) {
            try {
                mSocket.on((String) event.get(null), this);
            } catch (IllegalAccessException e) {
            }
        }
    }

    private void removeSocketListeners() {
        Field[] events = SocketEvent.class.getFields();
        for (Field event : events) {
            try {
                mSocket.off((String) event.get(null), this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void call(Object... args) {
        Map<String, Object> map = parser.parse(args);
        Object object = map.get(JSONSchema.TAG_EVENT_NAME);
        invokeCallBack(object.toString(), map);
        Log.d("SOCKET MANAGER", "CALL BACK" + object.toString());
    }


    @Override
    public void connect() {
        try {
            Log.d("SOCKET MANAGER", "connecting to socket");
            if (!mSocket.connected()) {
                mSocket.off();
                mSocket.on(Socket.EVENT_CONNECT, onConnectMessage);
                mSocket.on(Socket.EVENT_DISCONNECT, onDisconnectMessage);
                mSocket.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }


    private Emitter.Listener onConnectMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SOCKET MANAGER", "Connected");
            isConnected = true;
            removeSocketListeners();
            addSocketListeners();
            invokeCallBack(Socket.EVENT_CONNECT, args);
        }
    };


    private Emitter.Listener onDisconnectMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("SOCKET MANAGER", "DISCONNECTED");
            isConnected = false;
            invokeCallBack(Socket.EVENT_DISCONNECT, args);
        }
    };


    public void disconnect() {
        try {
            removeSocketListeners();
            mSocket.off(Socket.EVENT_CONNECT, onConnectMessage);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnectMessage);
            mSocket.disconnect();
        } catch (Exception e) {
        }
    }


    public void send(HashMap<String, String> map, String eventName) {
        JSONObject message = new JSONObject(map);
        boolean hasEventName = false;
        //TODO Need Check if it is need
        Field[] events = SocketEvent.class.getFields();
        for (Field event : events) {
            try {
                String mEventName = (String) event.get(null);
                if (mEventName.equalsIgnoreCase(eventName)) {
                    hasEventName = true;
                    break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (!hasEventName) {
            throw new RuntimeException("NO EVENT WITH THE NAME :" + eventName + " EXIST");
        }
        mSocket.emit(eventName, message);
    }

    public void invokeCallBack(final String eventName, final Object args) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (callBack != null) {
                        callBack.onSuccessListener(eventName, args);
                    }
                }
            });
        } else {
            if (callBack != null) {
                callBack.onSuccessListener(eventName, args);
            }
        }
    }


}


