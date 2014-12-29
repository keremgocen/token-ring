//package com.kerem.distributed.server;
//
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class ServerThread {
//	//the port on which server will listen for connections
//	public static int portNumber = 4444;
//
//	public static void main(String[] args) {
//
//		ServerSocket serverSocket = null;
//
//		try {
//			//initialize server socket
//			serverSocket = new ServerSocket(portNumber);
//			System.out.println("Server socket initialized.\n");
//		} catch (IOException e) { //if this port is busy, an IOException is fired
//			System.out.println("Cannot listen on port " + portNumber);
//			e.printStackTrace();
//			System.exit(0);
//		}
//
//		Socket clientSocket = null;
//
//		try {
//			while(true) { //infinite loop - terminate manually
//				//wait for client connections
//				System.out.println("Waiting for a client connection.");
//				try {
//					clientSocket = serverSocket.accept();
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.exit(0);
//				}
//
//				//let us see who connected
//				String clientName = clientSocket.getInetAddress().getHostName();
//				System.out.println(clientName + " established a connection.");
//				System.out.println();
//
//				//assign a worker thread
//				WorkerThread w = new WorkerThread(clientSocket);
//				new Thread(w).start();
//			}
//		} finally {
//			//make sure that the socket is closed upon termination
//			try {
//				serverSocket.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}
