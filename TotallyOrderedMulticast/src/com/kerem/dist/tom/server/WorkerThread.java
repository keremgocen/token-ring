package com.kerem.dist.tom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by keremgocen on 1/1/15.
 */
public class WorkerThread implements Runnable{

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

		/*
		 * RECEIVE message
		 */
		Scanner input = new Scanner(System.in);

		int i = 0;

		while(i < 100) {
			System.out.println("Round (" + (i++) + ")");
			System.out.println("Waiting for a message from the client.");
			String message = null;
			try {
				message = socketIn.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			socketOut.println("My pid:" + processId + ". You said: " + message);
			System.out.println("Client's message was: \n\t\"" + message + "\"");
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
