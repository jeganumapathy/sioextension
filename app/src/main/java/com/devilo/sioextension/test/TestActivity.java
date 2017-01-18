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
import com.devilo.sioextension.messageevent.BinaryMessageEvent;
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
        if (event.getEventName() != null) {
            if (event.getEventName().equalsIgnoreCase(SocketEvent.PONG)) {
                long dif = System.currentTimeMillis() - startTime;
                receiveText.setText("---------RECEIVE GROUP MESSAGE IN-----------" + dif);
            }
            if (event.getEventName().equalsIgnoreCase(Socket.EVENT_CONNECT)) {
                Toast.makeText(getApplicationContext(), "connected to socket", Toast.LENGTH_LONG).show();
            } else if (event.getEventName().equalsIgnoreCase(SocketEvent.EVENT_CREATE_USER_ACK)) {
                Map<Object, Object> map = (Map<Object, Object>) event.getObjectsArray();
                id = String.valueOf(map.get("id"));
            } else if (event.getEventName().equalsIgnoreCase(SocketEvent.EVENT_NEW_MESSAGE)) {
                Map<Object, Object> map = (Map<Object, Object>) event.getObjectsArray();
                map = (Map<Object, Object>) map.get("message");
                Toast.makeText(getApplicationContext(), map.toString(), Toast.LENGTH_LONG).show();
                float x = Float.valueOf((String) map.get("x"));
                float y = Float.valueOf((String) map.get("y"));
                view1.touch_start_inv(x, y);
            } else if (event.getEventName().equalsIgnoreCase(SocketEvent.PONG)) {
                long dif = System.currentTimeMillis() - startTime;
                receiveText.setText("---------RECEIVE GROUP MESSAGE IN-----------" + dif);
            } else if (event.getEventName().equalsIgnoreCase(SocketEvent.EVENT_SEND_BINARY)) {
                receiveText.setText("---------RECEIVE BINARY IN-----------");

            }
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
                messageEvent.setEventName(SocketManager.EVENT_NEW_MESSAGE);
                messageEvent.setParam(map);
                EventBus.getDefault().post(messageEvent);

            }
        });
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelf = true;
                String name = nameOfGroup.getText().toString();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", name);
                messageEvent.setEventName(SocketManager.EVENT_CREATE_USER);
                messageEvent.setParam(map);
                EventBus.getDefault().post(messageEvent);

            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSelf = false;
                // Intent intent = new Intent(TestActivity.this, ListGroup.class);
                Byte[] object = new Byte[42];
                //startActivity(intent);
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("id","fff");
                data.put("binary",object);
                BinaryMessageEvent binaryMessageEvent = new BinaryMessageEvent();
                binaryMessageEvent.setMessageObject(data);
                binaryMessageEvent.setEventName(SocketManager.EVENT_SEND_BINARY);
                EventBus.getDefault().post(binaryMessageEvent);

            }
        });
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            params.clear();
            startTime = System.currentTimeMillis();
            messageEvent.setEventName(SocketManager.EVENT_PING);
            messageEvent.setParam(params);
            EventBus.getDefault().post(messageEvent);
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
