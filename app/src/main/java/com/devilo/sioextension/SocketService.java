package com.devilo.sioextension;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.devilo.sioextension.messageevent.ReceviceMessageEvent;
import com.devilo.sioextension.messageevent.SendMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class SocketService extends Service {

    static public SocketService service;
    static SocketManager manager;
    String ip = "https://chatumpa.herokuapp.com/user";
    private ActiveSocketDispatcher dispatcher;

    public static boolean isStarted() {
        if (service == null || (manager != null && !manager.isConnected()))
            return false;
        return true;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendMessageEvent event) {
        manager.send(event.getMessageObject(), event.getEventName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        dispatcher = new ActiveSocketDispatcher();
        manager = new SocketManager(getApplicationContext(), callBack, ip);
        manager.connect();
        service = this;
        Toast.makeText(getApplicationContext(), "Start SERVICE :", Toast.LENGTH_LONG).show();

    }

    SocketManager.SocketCallBack callBack = new SocketManager.SocketCallBack() {
        @Override
        public void onSuccessListener(String eventName, Object response) {
            ReceviceMessageEvent me = new ReceviceMessageEvent();
            me.setEventName(eventName);
            me.setObjectsArray(response);
            dispatcher.addwork(me);
        }
    };


    public class ActiveSocketDispatcher {
        private BlockingQueue<Runnable> dispatchQueue
                = new LinkedBlockingQueue<Runnable>();

        public ActiveSocketDispatcher() {
            Thread mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            dispatchQueue.take().run();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mThread.start();
        }

        private void addwork(final Object packet) {
            try {
                dispatchQueue.put(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(packet);
                    }
                });
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Toast.makeText(getApplicationContext(), "onDestroy SERVICE :", Toast.LENGTH_LONG).show();
        service = null;
        super.onDestroy();
    }
}
