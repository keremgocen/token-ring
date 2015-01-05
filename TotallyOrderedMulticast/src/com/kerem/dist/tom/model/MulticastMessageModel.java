package com.kerem.dist.tom.model;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by keremgocen on 1/3/15.
 */
public class MulticastMessageModel implements Comparable<MulticastMessageModel>{

    /*
	 * multicast message definitions
	 */
    public static final String TOM_MSG_CONTENT = "MSG_CONTENT";
    public static final String TOM_MSG_ACK = "MSG_ACK";
    public static final String TOM_KILL_ALL = "MSG_KILL_ALL";

    private String content;
    private int senderId;
    private String lamportClock;
    private boolean ackArray[];

    public MulticastMessageModel(int processCount) {
        ackArray = new boolean[processCount];
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }
    
    public String getLamportClock() {
        return lamportClock;
    }

    public void setLamportClock(String lamportClock) {
        this.lamportClock = lamportClock;
    }

    public boolean[] getAckArray() {
        return ackArray;
    }

    public void setAckArray(boolean[] ackArray) {
        this.ackArray = ackArray;
    }

    @Override
    public int compareTo(MulticastMessageModel o) {
        return this.lamportClock.compareTo(o.getLamportClock());
    }

    @Override
    public String toString() {
        return "MulticastMessageModel{" +
                "content='" + content + '\'' +
                ", senderId='" + senderId + '\'' +
                ", lamportClock='" + lamportClock + '\'' +
                ", ackArray=" + Arrays.toString(ackArray) +
                '}';
    }
}
