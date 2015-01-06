package com.kerem.dist.tom.util;

import com.kerem.dist.tom.communication.MulticastOrganizerQueue;
import com.kerem.dist.tom.model.MulticastMessageModel;

import java.util.Scanner;

/**
 * Will be used to manually generate and add messages to the synchronized message queue
 * Communication threads will detect generated messages and broadcast them over the sockets
 * Worker threads receiving the generated message will add them to queue and broadcast ACK messages
 * Once ACK message is received on the client socket of a communication thread, it will run the delivery logic
 * to remove the message from queue
 *  
 * Created by keremgocen on 1/4/15.
 */
public class MessageGeneratorThread implements Runnable{

    // get instance of the communication queue
    private static final MulticastOrganizerQueue commQueue = MulticastOrganizerQueue.INSTANCE;
    
    private int senderId;

    public MessageGeneratorThread(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public void run() {
        
        Scanner input = new Scanner(System.in);
        boolean closeUp = false;
        
		while(!closeUp) {

            BlockingConsoleLogger.INSTANCE.println("Time now:" + LocalLamportClock.INSTANCE.getClock() + "\nType message content:");
			
			String message = input.nextLine();
            
			if(message.equals(MulticastMessageModel.TOM_KILL_ALL)) {
                closeUp = true;
            } else {
                String array[] = message.split("|");
                String messageBody = array[0];
                String clock = array[1];
                // generate message and add it to queue
                MulticastMessageModel messageModel = new MulticastMessageModel(4);
                messageModel.setContent(messageBody);
                messageModel.setSenderId(senderId);
                messageModel.setLamportClock(clock);
                
                BlockingConsoleLogger.INSTANCE.println("Create message:" + messageModel.toString());

                try {
                    commQueue.addMessage(messageModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                commQueue.displayQueue();
            }
			
		}
        
		input.close();
    }
}
