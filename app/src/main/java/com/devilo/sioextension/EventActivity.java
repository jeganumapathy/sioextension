package com.devilo.sioextension;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.devilo.sioextension.messageevent.ReceviceMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 *
 */
public abstract class EventActivity extends Activity {

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SocketService.isStarted()) {
            startService(new Intent(this, SocketService.class));
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public abstract void onMessageEvent(ReceviceMessageEvent event);
}
