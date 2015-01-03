package com.kerem.dist.tom.server;

import com.kerem.dist.tom.communication.MulticastOrganizerQueue;
import com.kerem.dist.tom.model.MulticastMessageModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * WorkerThread initiates the connections between processes
 * After initialization, it will be responsible for handling received messages
 * Messages received will be put in the process local synchronized queue
 * * 
 * Created by keremgocen on 1/1/15.
 */
public class WorkerThread implements Runnable{

	/*
	 * multicast message definitions
	 */
	private static final String TOM_MSG_ADD_SUCCESS = "MSG_SUCCESS";
	private static final String TOM_MSG_ADD_FAIL = "MSG_FAIL";
	private static final String TOM_KILL_ALL = "MSG_KILL_ALL";
	
	private int processId;

	// get instance of the communication queue
	private static final MulticastOrganizerQueue commQueue = MulticastOrganizerQueue.INSTANCE;

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
			System.out.println("Cannot get I/O for the connection.");
			e.printStackTrace();
			System.exit(0);
		}

		/*
		 * RECEIVE message
		 */

		int i = 0;

		while(true) {
			String message = null;
			
			boolean messageAdded = false;
			try {
				message = socketIn.readLine();
				if(message.equals(TOM_KILL_ALL)) {
					break;
				} else {
					final MulticastMessageModel newMessage = new MulticastMessageModel(String.valueOf(processId));
					newMessage.setContent(message);
					// TODO get lamport clock
					newMessage.setLamportClock("");
					messageAdded = commQueue.addMessage(newMessage);
					
					if(messageAdded == true) {
						// TODO synchronize console access
						System.out.println("message added to queue on socket:" + clientSocket.getInetAddress() + " " + TOM_MSG_ADD_SUCCESS);
						commQueue.displayQueue();
					} else {
						System.out.println("message add failed on socket:" + clientSocket.getInetAddress() + " " + TOM_MSG_ADD_FAIL);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(messageAdded) {
				socketOut.println(TOM_MSG_ADD_SUCCESS);
			} else {
				socketOut.println(TOM_MSG_ADD_FAIL);
			}

		}

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
