package com.kerem.dist.tom.server;

import com.kerem.dist.tom.communication.MulticastOrganizerQueue;
import com.kerem.dist.tom.model.MulticastMessageModel;
import com.kerem.dist.tom.model.ResponseModel;
import com.kerem.dist.tom.util.BlockingConsoleLogger;
import com.kerem.dist.tom.util.LocalLamportClock;
import com.kerem.dist.tom.util.MessageDeserializer;
import com.kerem.dist.tom.util.ResponseDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * WorkerThread initiates the connections between processes
 * After initialization, it will be responsible for handling received messages
 * Messages received will be put in the process local synchronized queue
 * *
 * Created by keremgocen on 1/1/15.
 */
public class WorkerThread implements Runnable {

    // get instance of the communication queue
    private static final MulticastOrganizerQueue commQueue = MulticastOrganizerQueue.INSTANCE;
    private int processId;
    private Socket clientSocket;

    public WorkerThread(Socket s, int processId) {
        clientSocket = s;
        this.processId = processId;
    }

    public void run() {
        //taken from Server4SingleClient
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;

        try {
            //will use socketOut to send text to the server over the socket
            socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            //will use socketIn to receive text from the server over the socket
            socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            BlockingConsoleLogger.INSTANCE.println(processId + "-Cannot get I/O for the connection.");
            e.printStackTrace();
            System.exit(0);
        }

		/*
         * RECEIVE message
		 */

        int i = 0;

        while (true) {
            String message = null;

            String receivedHeader = null;
            
            boolean messageAdded = false;
            try {
                message = socketIn.readLine();

                BlockingConsoleLogger.INSTANCE.println(processId + "-TS:" + LocalLamportClock.INSTANCE.getClock() + " received:" + message);

                // parse message header and body
                final String[] receivedArray = message.split("|");
                receivedHeader = receivedArray[0];
                message = receivedArray[1];

                if (receivedHeader.equals(MulticastMessageModel.TOM_KILL_ALL)) {
                    break;
                } else if(receivedHeader.equals(MulticastMessageModel.TOM_MSG_CONTENT)) {
                    // multicast message received
                    
                    MulticastMessageModel receivedMessage = null;
                    try {
                        receivedMessage = MessageDeserializer.createMessageFromString(message);
                        receivedMessage.getAckArray()[processId] = true;
                        
                        messageAdded = commQueue.addMessage(receivedMessage);
						
                        BlockingConsoleLogger.INSTANCE.println(processId + "-parsed:" + message);

                    } catch (Exception e) {
                        messageAdded = false;
                        e.printStackTrace();
                    }
                } else if(receivedHeader.equals(MulticastMessageModel.TOM_MSG_ACK)) {
                    // ACK received, this is required for first starting progress which does not have a communication thread
                    BlockingConsoleLogger.INSTANCE.println(processId + "-received ACK from process:" + message + " on socket:" + clientSocket.getInetAddress());
                    
                    // mark the message
                    
                    // check delivery condition
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResponseModel response = new ResponseModel(processId);
            String responseString = null;

            if (messageAdded) {
                response.setMessageType(MulticastMessageModel.TOM_MSG_CONTENT);
                BlockingConsoleLogger.INSTANCE.println(processId + "-message added to queue on client socket:" + clientSocket.getInetAddress() + " " + MulticastMessageModel.TOM_MSG_CONTENT);
                commQueue.displayQueue();
            } else {
                response.setMessageType(MulticastMessageModel.TOM_MSG_ACK);
                BlockingConsoleLogger.INSTANCE.println(processId + "-message add failed on client socket:" + clientSocket.getInetAddress() + " " + MulticastMessageModel.TOM_MSG_ACK);
            }

            try {
                responseString = ResponseDeserializer.stringFromResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            socketOut.println(responseString);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        BlockingConsoleLogger.println(processId + "-terminating server thread on client:" + clientSocket.getInetAddress());
        
        LocalLamportClock.INSTANCE.setStopClock();

        //close all streams
        socketOut.close();

        try {
            socketIn.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
