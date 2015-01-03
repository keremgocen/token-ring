package com.kerem.dist.tom.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Will create a server socket and wait for connections
 *
 * Created by keremgocen on 1/1/15.
 */
public class ServerThread implements Runnable {
	//the port on which server will listen for connections
	private static int portNumber;
	//number of total processes the server thread will wait for connection before terminating itself safely
	private static int totalProcessCount;
	private static int connectedProcessCount = 0;
	private static int processId;

	public ServerThread(int processId, int portNumber, int totalProcessCount) {
		this.processId = processId;
		this.portNumber = portNumber;
		this.totalProcessCount = totalProcessCount;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;

		try {
			//initialize server socket
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server socket on port " + portNumber + " initialized...");
		} catch (IOException e) { //if this port is busy, an IOException is fired
			System.out.println("Cannot listen on port " + portNumber);
			e.printStackTrace();
			System.exit(0);
		}

		Socket clientSocket = null;

		try {
			while(true) { //infinite loop - terminate manually

				if(connectedProcessCount == totalProcessCount) {
					System.out.println("All processes are connected. Terminating server on port:" + portNumber);
					break;
				}

				//wait for client connections
				System.out.println("Waiting for a client connection on port:" + portNumber);
				try {
					clientSocket = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}

				//let us see who connected
				String clientName = clientSocket.getInetAddress().getHostName();
				System.out.println(clientName + " established a connection on host:" + serverSocket.getLocalSocketAddress());
				System.out.println();

				//assign a worker thread
				WorkerThread w = new WorkerThread(clientSocket, processId);
				new Thread(w).start();
				connectedProcessCount++;
				System.out.println("current connected process count on port " + portNumber + " is:" + connectedProcessCount);
			}
		} finally {
			//make sure that the socket is closed upon termination
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
