package com.devilo.sioextension;

/**
 */
public interface SocketEvent {

    String EVENT_NEW_MESSAGE = "new_message";
    String EVENT_CREATE_USER = "add_user";
    String EVENT_CREATE_USER_ACK = "usercreated";
    String EVENT_USER_JOINED = "user joined";
    String EVENT_HEARTBEAT = "Heartbeat";
    String EVENT_MESSAGE_RES = "MessageRes";
    String EVENT_STOP_TYPING = "stop typing";
    String EVENT_MESSAGE = "Message";
    String EVENT_GROUP = "group";
    String EVENT_GET_ALL_CHAT_HISTORY = "getAllChatHistory";
    String EVENT_GET_MESSAGE = "GetMessages";
    String EVENT_MESSAGE_STATUS_UPDATE = "MessageStatusUpdate";
    String EVENT_MESSAGE_ACK = "MessageAck";
    String EVENT_GET_CURRENT_TIME_STATUS = "getCurrentTimeStatus";
    String EVENT_GET_MESSAGE_ACK = "GetMessageAcks";
    String EVENT_CAHNGE_ONLINE_STATUS = "ChangeOnlineStatus";
    String EVENT_UPDATE_ONLINE_STATUS = "UpdateOnlineStatus";
    String EVENT_BROADCAST_PROFILE = "broadCastProfile";
    String EVENT_CHANGE_ST = "changeSt";
    String EVENT_TYPING = "typing";
    String EVENT_CREATE_ROOM = "create room";
    String EVENT_ADD_CONTACT = "add contact";
    String EVENT_GET_FAVORITE = "GetFavorite";
    String EVENT_GET_CONTACTS = "GetContacts";
    String EVENT_QR_DATA = "qrdata";
    String ROOM_STRING = "room";
    String EVENT_TOTAL_GROUP = "total_group";
    String EVENT_PING = "ping";
    String PONG = "pong";


}
