package com.devilo.sioextension;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 */
public class TestActivity extends Activity {

    String ip = "http://192.168.0.15:3000/user";
    SocketManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = new SocketManager(this, new SocketManager.SocketCallBack() {
            @Override
            public void onSuccessListener(String eventName, Object response) {
                if (eventName.equalsIgnoreCase(Socket.EVENT_CONNECT)) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("id", "jegan");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    manager.send(object, SocketEvent.EVENT_CREATE_USER);
                }
            }
        }, ip);
        manager.connect();
    }
}
