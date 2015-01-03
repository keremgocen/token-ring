package com.kerem.dist;

import com.kerem.dist.tokenring.TokenRingGenerator;

public class TokenRing_Main {

    public static void main(String[] args) {
        System.out.println("Starting up token-ring simulator...");

        // Create token-ring simulator with predefined configuration
        TokenRingGenerator tokenRingGenerator = new TokenRingGenerator(2, 4444);
    }
}
