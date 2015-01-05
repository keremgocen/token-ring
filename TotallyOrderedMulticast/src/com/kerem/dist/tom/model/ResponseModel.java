package com.kerem.dist.tom.model;

/**
 * Created by keremgocen on 1/4/15.
 */
public class ResponseModel {
    private int senderId;
    private String messageType;

    public ResponseModel(int senderId) {
        this.senderId = senderId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "ResponseModel{" +
                "senderId=" + senderId +
                ", messageType='" + messageType + '\'' +
                '}';
    }
}
