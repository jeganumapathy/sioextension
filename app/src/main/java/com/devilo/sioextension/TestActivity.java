package com.devilo.sioextension;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

/**
 */
public class TestActivity extends Activity {

    String ip = "http://192.168.0.15:3000/user";
    SocketManager manager;
    String id = "ggg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final TextView receiveText = (TextView) findViewById(R.id.receiveText);
        final Button createGroup = (Button) findViewById(R.id.btnCreateGroup);
        final Button sendMessage = (Button) findViewById(R.id.btnSendMessage);
        final EditText nameOfGroup = (EditText) findViewById(R.id.edtGroup);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager.isConnected()) {
                    String name = nameOfGroup.getText().toString();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", name);
                    manager.send(map, SocketEvent.EVENT_CREATE_USER);
                } else {
                    Toast.makeText(getApplicationContext(), "NOT CONNETED", Toast.LENGTH_LONG).show();
                    manager.connect();
                }

            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("to", "" + id);
                map.put("data", "Hi Hello");
                manager.send(map, SocketEvent.EVENT_NEW_MESSAGE);
            }
        });
        manager = new SocketManager(this, new SocketManager.SocketCallBack() {
            @Override
            public void onSuccessListener(String eventName, Object response) {
                if (eventName.equalsIgnoreCase(Socket.EVENT_CONNECT)) {
                } else if (eventName.equalsIgnoreCase(SocketEvent.EVENT_CREATE_USER_ACK)) {
                    Map<Object, Object> map = (Map<Object, Object>) response;
                    Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_LONG).show();
                    id = String.valueOf(map.get("id"));
                } else if (eventName.equalsIgnoreCase(SocketEvent.EVENT_NEW_MESSAGE)) {
                    Map<Object, Object> map = (Map<Object, Object>) response;
                    Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, ip);
        manager.connect();
    }
}
