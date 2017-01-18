package com.devilo.sioextension.messageevent;


import java.util.HashMap;

/**
 */
public class BinaryMessageEvent {

    private String eventName;

    private HashMap<String, Object> messageObject;


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }


    public HashMap<String, Object> getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(HashMap<String, Object> messageObject) {
        this.messageObject = messageObject;
    }
}
