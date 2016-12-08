package com.devilo.sioextension.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devilo.sioextension.EventActivity;
import com.devilo.sioextension.R;
import com.devilo.sioextension.SocketEvent;
import com.devilo.sioextension.SocketManager;
import com.devilo.sioextension.messageevent.ReceviceMessageEvent;
import com.devilo.sioextension.messageevent.SendMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

/**
 */
public class TestActivity extends EventActivity {

    String ip = "https://chatumpa.herokuapp.com/user";
    SocketManager manager;
    String id = "ggg";
    MyView view, view1;
    boolean isSelf;
    TextView receiveText;
    SendMessageEvent messageEvent = new SendMessageEvent();
    HashMap<String, String> params = new HashMap<String, String>();
    long startTime;
    private Handler mHandler = new Handler() {
    };


    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceviceMessageEvent event) {
        if (event.getEventName().equalsIgnoreCase(SocketEvent.PONG)) {
            long dif = System.currentTimeMillis() - startTime;
            receiveText.setText("---------RECEIVE GROUP MESSAGE IN-----------" + dif);
        }
    }


    public interface TouchEvent {
        void callback(float x, float y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        receiveText = (TextView) findViewById(R.id.receiveText);
        final Button createGroup = (Button) findViewById(R.id.btnCreateGroup);
        final Button sendMessage = (Button) findViewById(R.id.btnSendMessage);
        final EditText nameOfGroup = (EditText) findViewById(R.id.edtGroup);
        view = (MyView) findViewById(R.id.view);
        view1 = (MyView) findViewById(R.id.view1);
        view.setTouch(new TouchEvent() {
            @Override
            public void callback(float x, float y) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("to", "" + id);
                map.put("x", "" + x);
                map.put("y", "" + y);
                manager.send(map, SocketEvent.EVENT_NEW_MESSAGE);

            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (manager.isConnected()) {
                    isSelf = true;
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
                isSelf = false;
                Intent intent = new Intent(TestActivity.this, ListGroup.class);
                startActivity(intent);
            }
        });


        manager = new SocketManager(this, new SocketManager.SocketCallBack() {
            @Override
            public void onSuccessListener(String eventName, Object response) {
                if (eventName != null) {
                    if (eventName.equalsIgnoreCase(Socket.EVENT_CONNECT)) {
                    } else if (eventName.equalsIgnoreCase(SocketEvent.EVENT_CREATE_USER_ACK)) {
                        Map<Object, Object> map = (Map<Object, Object>) response;
                        id = String.valueOf(map.get("id"));
                    } else if (eventName.equalsIgnoreCase(SocketEvent.EVENT_NEW_MESSAGE)) {
                        if (!isSelf) {
                            Map<Object, Object> map = (Map<Object, Object>) response;
                            map = (Map<Object, Object>) map.get("message");
                            Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_LONG).show();
                            float x = Float.valueOf((String) map.get("x"));
                            float y = Float.valueOf((String) map.get("y"));
                            view1.touch_start_inv(x, y);
                        }
                    } else if (eventName.equalsIgnoreCase(SocketEvent.PONG)) {
                        long dif = System.currentTimeMillis() - startTime;
                        receiveText.setText("---------RECEIVE GROUP MESSAGE IN-----------" + dif);
                    }
                }

            }
        }, ip);
        //manager.connect();
        mHandler.postDelayed(mRunnable, 1000);
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            params.clear();
            startTime = System.currentTimeMillis();
            messageEvent.setEventName(SocketManager.EVENT_PING);
            messageEvent.setMessageObject(params);
            EventBus.getDefault().post(messageEvent);
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    @Override
    protected void onDestroy() {
        manager.disconnect();
        super.onDestroy();
    }
}
