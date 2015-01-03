package com.kerem.dist.tokenring;

//    Token-ring algorithm

//    Initialization
//      Process 0 gets token for resource R
//    Token circulates around ring
//      From Pi to P(i+1)mod N
//    When process acquires token
//      Checks to see if it needs to enter critical section
//    If no, send token to neighbor
//    If yes, access resource
//    Hold token until done

import java.util.List;

/**
 * Created by keremgocen on 12/22/14.
 *
 */
public class TokenRingGenerator {

    private int numberOfNodes;
    private int startingNodePortNumber;
    public static String serverName = "localhost";
    private List<TokenRingNode> tokenRingList;

    public TokenRingGenerator(int numberOfNodes, int startingNodePortNumber) {
        this.numberOfNodes = numberOfNodes;
        this.startingNodePortNumber = startingNodePortNumber;
        initializeTokenRing();

    }

    // TODO initialize the ring
    private void initializeTokenRing() {

        // create first node
        TokenRingNode firstNode = new TokenRingNode(startingNodePortNumber);

        // create second node
        TokenRingNode secondNode = new TokenRingNode(startingNodePortNumber + 1);

        // connect first node
        firstNode.setupNeighborConnection(serverName, startingNodePortNumber + 1);
        // last to first
        secondNode.setupNeighborConnection(serverName, startingNodePortNumber);

        new Thread(firstNode).start();
        new Thread(secondNode).start();

//        // create third node
//        TokenRingNode thirdNode = new TokenRingNode(startingNodePortNumber + 2);
//        new Thread(thirdNode).start();
//        //connect second node
//        secondNode.setupNeighborConnection(serverName, startingNodePortNumber + 1);
//        //connect last node to first
//        thirdNode.setupNeighborConnection(serverName, startingNodePortNumber);

//        tokenRingList = new ArrayList<TokenRingNode>();
//
//        for(int i = 0; i < numberOfNodes; i++) {
//
//            // create a new node
//            TokenRingNode node = new TokenRingNode(startingNodePortNumber + i);
//            new Thread(node).start();
//            tokenRingList.add(node);
//
//
//            if(i == numberOfNodes - 1) {
//                // last node's neighbor is the first one
//                node.setupNeighborConnection(serverName, startingNodePortNumber);
//
//                // close the ring by connecting the first node to it's neighbor
//                TokenRingNode firstNode = tokenRingList.get(0);
//                if(firstNode != null) {
//                    firstNode.setupNeighborConnection(serverName, startingNodePortNumber + 1);
//                }
//            } else  if(i == 0) {
//                // first node, skip neighbor connection, close the ring later
//                continue;
//            } else {
//                // regular middle node
//                node.setupNeighborConnection(serverName, startingNodePortNumber + i + 1);
//            }
//
//        }
    }

    // TODO request operation
}
