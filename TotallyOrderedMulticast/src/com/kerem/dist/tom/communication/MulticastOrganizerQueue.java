package com.kerem.dist.tom.communication;

import com.kerem.dist.tom.model.MulticastMessageModel;
import com.kerem.dist.tom.util.BlockingConsoleLogger;

import java.util.Arrays;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by keremgocen on 1/3/15.
 */
public enum MulticastOrganizerQueue {
    INSTANCE;
    
    private static final PriorityBlockingQueue<MulticastMessageModel> messageQueue = new PriorityBlockingQueue<MulticastMessageModel>();

    private MulticastOrganizerQueue() {
        BlockingConsoleLogger.INSTANCE.println("MulticastOrganizerQueue constructor");
    }
    
    public synchronized static boolean addMessage(final MulticastMessageModel message) throws Exception{
        synchronized (messageQueue) {
            return messageQueue.offer(message);
        }
    }

    public synchronized static MulticastMessageModel peekMessage() {
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
            
            if(messageQueue.contains(message)) {
                return messageQueue.remove(message);
            }

            
            return false;
        }
    }

    // assuming timestamp is unique for all messages
    public synchronized static boolean updateMessageAck(final String timeStamp, final int ackIndex) {
        synchronized (messageQueue) {
            while(messageQueue.iterator().hasNext()) {
                MulticastMessageModel message = messageQueue.iterator().next();
                if(message.getLamportClock().equals(timeStamp)) {
                    BlockingConsoleLogger.INSTANCE.println("\nQUEUE STATE 1");
                    MulticastOrganizerQueue.INSTANCE.displayQueue();
                    BlockingConsoleLogger.INSTANCE.println("Found message with TS:" + timeStamp + ". Updating ACK array");
                    message.getAckArray()[ackIndex] = true;
                    BlockingConsoleLogger.INSTANCE.println("Updated ACK for message:" + message.toString() + " at index:" + ackIndex);
                    BlockingConsoleLogger.INSTANCE.println("\nQUEUE STATE 2");
                    MulticastOrganizerQueue.INSTANCE.displayQueue();
                    return true;
                }
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
            BlockingConsoleLogger.INSTANCE.println("\nqueue content:" + Arrays.asList(messageQueue).toString());
        }
    }
}
