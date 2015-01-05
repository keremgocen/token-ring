package com.kerem.dist.tom.util;

/**
 * Created by keremgocen on 1/4/15.
 */
public enum BlockingConsoleLogger {
    INSTANCE;
    
    public synchronized static void println(final String message) {
        synchronized (System.out) {
            System.out.println(message);
        }
    }
}
