package com.kerem.dist.tokenring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by keremgocen on 12/22/14.
 */
public class TokenRingNode implements Runnable {

    private final static String TAG = "node";

    private String neighborNodeName;
    private int portNumber;
    private int neighborPortNumber;

    // initialize listener and sender sockets

    // listenerSocket will receive token from the successor node
    private ServerSocket listenerSocket;
    // senderSocket will be passing the token to the neighbor
    private Socket senderSocket;

    private BufferedReader socketIn;
    private PrintWriter socketOut;

    // TODO token is represent with an integer, use real resource
    private String token = "tempToken";

    public TokenRingNode(int portNumber) {
        this.portNumber = portNumber;

        listenerSocket = null;

        try {
            //initialize listener socket
            listenerSocket = new ServerSocket(portNumber);
            System.out.println(TAG + portNumber + " Listener socket initialized.\n");
        } catch (IOException e) { //if this port is busy, an IOException is fired
            System.out.println(TAG + portNumber + " Cannot listen on port " + portNumber);
            e.printStackTrace();
        }

        senderSocket = null;
        socketIn = null;
        socketOut = null;
    }

    public boolean setupNeighborConnection(String neighborNodeName, int neighborPortNumber) {
        this.neighborPortNumber = neighborPortNumber;
        this.neighborNodeName = neighborNodeName;

        try {
            //create socket and connect to the server
            senderSocket = new Socket(neighborNodeName, neighborPortNumber);
            //will use socketOut to send text to the server over the socket
            socketOut = new PrintWriter(senderSocket.getOutputStream(), true);

            System.out.println(TAG + portNumber + " created neighbor connection at:" + senderSocket.getLocalSocketAddress() + " port:" + neighborPortNumber);

            return true;

        } catch (UnknownHostException e) { //if serverName cannot be resolved to an address
            System.out.println(TAG + portNumber + " Who is " + neighborNodeName + "?");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(TAG + portNumber + " Cannot get I/O for the connection.");
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void run() {

        Socket listeningSocket = null;

        // try to receive token
        // check resource access condition

        try {
            while (true) {

                //wait for client connections

                // TODO replace println with synchronized log writer
                System.out.println(TAG + portNumber + " Waiting for a connection...");
                try {
                    listeningSocket = listenerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (listeningSocket.isConnected()) {
                    System.out.println(TAG + portNumber + " established a connection to " + listenerSocket.getInetAddress().getHostName() + " on socket:" + listenerSocket.getLocalSocketAddress());

                    try {
                        socketIn = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    socketOut.println(token + portNumber);
//                    System.out.println("Message sent, waiting for the server's response.");
//                    String response = null;
//                    try {
//                        response = socketIn.readLine();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Server's response was: \n\t\"" + response + "\"");
//                    System.out.println();

                    System.out.println(TAG + portNumber + " waiting for a token from successor.");
                    try {
                        token = socketIn.readLine();

                        System.out.println(TAG + portNumber + " received token:" + token + " from successor.");

                        continue;
                    } catch (IOException e) {
                        System.out.println(TAG + portNumber + " Cannot get I/O for the connection.");
                        e.printStackTrace();
                    }
                }

                // pass the token

                // TODO check if we are done with the token
                if (senderSocket != null && senderSocket.isConnected()) {
                    socketOut.println(token);
                    System.out.println(TAG + portNumber + " " + token + " message sent to " + senderSocket.getPort());
                }
                else {
                    System.out.println(TAG + portNumber + " sender is not connected!");
                }


            }
        } finally {
            //make sure that the socket is closed upon termination
            try {
                if(socketIn != null) {
                    socketIn.close();
                }

                if(socketOut != null) {
                    socketOut.close();
                }

                if(listeningSocket != null) {
                    listeningSocket.close();
                }

                if(listenerSocket != null) {
                    listenerSocket.close();
                }

                if(senderSocket != null) {
                    senderSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
