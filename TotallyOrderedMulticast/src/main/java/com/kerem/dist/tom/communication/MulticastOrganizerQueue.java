package com.kerem.dist.tom.communication;

import com.kerem.dist.tom.model.MulticastMessageModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by keremgocen on 1/3/15.
 */
public enum MulticastOrganizerQueue {
    INSTANCE;
    
    private static final PriorityBlockingQueue<MulticastMessageModel> messageQueue = new PriorityBlockingQueue<MulticastMessageModel>(0, new Comparator<MulticastMessageModel>() {
        @Override
        public int compare(MulticastMessageModel o1, MulticastMessageModel o2) {
            return o1.getLamportClock().compareTo(o2.getLamportClock());
        }
    });

    private MulticastOrganizerQueue() {
        System.out.println("MulticastOrganizerQueue constructor");
    }
    
    public synchronized static boolean addMessage(final MulticastMessageModel message) {
        synchronized (messageQueue) {
            return messageQueue.add(message);
        }
    }

    public synchronized static MulticastMessageModel peekMessage(int messageIndex) {
        synchronized (messageQueue) {
            return messageQueue.peek();
        }
    }

    public synchronized static MulticastMessageModel removeMessage() {
        synchronized (messageQueue) {
            return messageQueue.poll();
        }
    }

    public synchronized static boolean deliverMessage(final MulticastMessageModel message) {
        synchronized (messageQueue) {
            
            // TODO create message delivery control mechanism
            
//            boolean messageRemoved = false;
//            boolean messageFound = false;
//            MulticastMessageModel msgToDeliver;
            
//            while (messageQueue.iterator().hasNext()) {
//                msgToDeliver = messageQueue.iterator().next();
//                if(msgToDeliver.equals(message)) {
//                    messageFound = true;
//                    break;
//                }
//            }
            
//            if(messageFound == true) {
//                messageRemoved = messageQueue.remove(message);
//            }
            
            if(messageQueue.contains(message)) {
                return messageQueue.remove(message);
            }

            
            return false;
        }
    }
    
    public synchronized static int getQueueSize() {
        synchronized (messageQueue) {
            return messageQueue.size();
        }
    }

    public synchronized static boolean messageExists(final MulticastMessageModel message) {
        synchronized (messageQueue) {
            return messageQueue.contains(message);
        }
    }

    public synchronized static void displayQueue() {
        synchronized (messageQueue) {
            System.out.println("queue content:" + Arrays.asList(messageQueue).toString());
        }
    }
}
