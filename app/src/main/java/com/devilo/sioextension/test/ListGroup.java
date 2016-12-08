package com.devilo.sioextension.test;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 12/1/2016.
 */
public class ListGroup extends EventActivity {

    private ListView list;
    private ListAdatapter adapter;
    private ArrayList<ListData> data;
    private SendMessageEvent messageEvent = new SendMessageEvent();
    HashMap<String, String> params = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_group);
        list = (ListView) findViewById(R.id.list);
        data = new ArrayList<>();
        adapter = new ListAdatapter(this, data);
        list.setAdapter(adapter);
        messageEvent.setEventName(SocketManager.EVENT_TOTAL_GROUP);
        messageEvent.setMessageObject(params);
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceviceMessageEvent event) {
        if (event.getEventName().equalsIgnoreCase(SocketEvent.EVENT_TOTAL_GROUP)) {
            Toast.makeText(getApplicationContext(), "EVENT_TOTAL_GROUP", Toast.LENGTH_LONG).show();
        }
    }

    public static class ListAdatapter extends BaseAdapter {

        ArrayList<ListData> myList = new ArrayList();
        LayoutInflater inflater;
        Context context;


        public ListAdatapter(Context context, ArrayList myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public ListData getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_list_item, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            ListData currentListData = getItem(position);
            mViewHolder.tvTitle.setText(currentListData.getTitle());
            mViewHolder.tvDesc.setText(currentListData.getDescription());
            mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());
            return convertView;
        }

        private class MyViewHolder {
            TextView tvTitle, tvDesc;
            ImageView ivIcon;

            public MyViewHolder(View item) {
                tvTitle = (TextView) item.findViewById(R.id.tvTitle);
                tvDesc = (TextView) item.findViewById(R.id.tvDesc);
                ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
            }
        }
    }
}
