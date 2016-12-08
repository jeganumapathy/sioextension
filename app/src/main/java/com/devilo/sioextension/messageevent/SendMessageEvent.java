package com.devilo.sioextension.messageevent;

import java.util.HashMap;

/**
 */
public class SendMessageEvent {

    private String eventName;

    private HashMap<String, String> messageObject;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public HashMap<String, String> getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(HashMap<String, String> messageObject) {
        this.messageObject = messageObject;
    }
}
