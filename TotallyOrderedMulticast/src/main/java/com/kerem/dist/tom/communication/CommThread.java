package com.kerem.dist.tom.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by keremgocen on 1/3/15.
 */
public class CommThread implements Runnable {
	//server's name or IP number
	private String serverName;
	//the port of the server to connect to
	private int portNumber;
	// get instance of the communication queue
	private static final MulticastOrganizerQueue commQueue = MulticastOrganizerQueue.INSTANCE;

	/*
	 * multicast message definitions
	 */
	private static final String TOM_MSG_ADD_SUCCESS = "MSG_SUCCESS";
	private static final String TOM_MSG_ADD_FAIL = "MSG_FAIL";
	private static final String TOM_KILL_ALL = "MSG_KILL_ALL";

    public CommThread(String serverName, int portNumber) {
        this.serverName = serverName;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
		PrintWriter socketOut = null;
		BufferedReader socketIn = null;

		try {
			//create socket and connect to the server
			clientSocket = new Socket(serverName, portNumber);
			//will use socketOut to send text to the server over the socket
			socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
			//will use socketIn to receive text from the server over the socket
			socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch(UnknownHostException e) { //if serverName cannot be resolved to an address
			System.out.println("Who is " + serverName + "?");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Cannot get I/O for the connection.");
			e.printStackTrace();
			System.exit(0);
		}

		/**
		 * Protocol (known to both parties):
		 * Server blocks on a message from the client.
		 * Client sends a message and blocks on the server's response.
		 * Upon receipt of message, server will respond: "You said: " + message
		 * This continues for 3 rounds.
		 */
		
		System.out.println("Starting up comm thread on port:" + portNumber);

		/*
		 * SEND receive message
		 */
		
		// TODO periodically check queue and send message on top if exists
		
		Scanner input = new Scanner(System.in);

		int i = 0;

		while(true) {
			System.out.println("Round (" + (i++) + ")");
			System.out.print("Type a message to be sent to the server: ");
			String message = input.nextLine();
			socketOut.println(message);
			System.out.println("Message sent, waiting for the server's response.");
			String response = null;
			try {
				response = socketIn.readLine();
				if(response.equals(TOM_MSG_ADD_SUCCESS)) {
					System.out.println("Server ACK received on client:" + clientSocket.getInetAddress());
					
					// TODO run logic
					commQueue.displayQueue();
				} else if(response.equals(TOM_MSG_ADD_FAIL)) {
					System.out.println("Server ACK failed on client:" + clientSocket.getInetAddress());
				} else if(response.equals(TOM_KILL_ALL)) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Server's response was: \n\t\"" + response + "\"");
			System.out.println();
		}

		//close all streams
		socketOut.close();
		input.close();
		try {
			socketIn.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
