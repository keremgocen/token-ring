package com.kerem.dist.tom.model;

import java.util.Comparator;

/**
 * Created by keremgocen on 1/3/15.
 */
public class MulticastMessageModel implements Comparator<MulticastMessageModel>{
    
    private static String content;
    private static String senderId;
    private static String lamportClock;
    private static boolean ackArray[];

    public MulticastMessageModel(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
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
    public int compare(MulticastMessageModel o1, MulticastMessageModel o2) {
        return o1.getLamportClock().compareTo(o2.getLamportClock());
    }
}
