package com.devilo.sioextension.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.devilo.sioextension.R;
import com.devilo.sioextension.SocketManager;

import java.util.HashMap;

/**
 * Created by Administrator on 11/30/2016.
 */
public class CreateGroup extends Activity {

    private Button btnCreateGroup;
    private EditText edtCreateGroup;
    private SocketManager manager;
    String ip = "https://chatumpa.herokuapp.com/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        manager = new SocketManager(this, new SocketManager.SocketCallBack() {
            @Override
            public void onSuccessListener(String eventName, Object response) {
            }
        }, ip);
        btnCreateGroup = (Button) findViewById(R.id.btnCreateGroupButton);
        edtCreateGroup = (EditText) findViewById(R.id.edtCreateGroup);
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = edtCreateGroup.getText().toString();
                if (data != null && data.length() > 0) {
                    HashMap<String, String> a = new HashMap<String, String>();
                    a.put("id", "" + data);
                    manager.send(a, SocketManager.EVENT_CREATE_USER);
                }
            }
        });
        manager.connect();
    }
}
