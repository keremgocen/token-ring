package com.kerem.dist.tom.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by keremgocen on 1/3/15.
 */
public enum LocalLamportClock implements Runnable{
    INSTANCE {
        @Override
        public void run() {
            while (!stopClock) {
                INSTANCE.firstPart += INSTANCE.clockRate;
                synchronized (INSTANCE.lamportClock) {
                    INSTANCE.lamportClock = String.valueOf(INSTANCE.firstPart + INSTANCE.clockRate) + "." + String.valueOf(INSTANCE.processId);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static long processId;
    private static long clockRate;
    private static long firstPart = 0;
    
    private static volatile boolean stopClock = false;
    
    private static volatile String lamportClock = "";

    private LocalLamportClock() {
    }

    public synchronized static void setStopClock() {
        INSTANCE.stopClock = true;
        BlockingConsoleLogger.println(processId + "-stopping lamport clock at:" + INSTANCE.lamportClock);
    }

    public synchronized static String getClock() {
        return INSTANCE.lamportClock;
    }

    public synchronized static void setProcessId(int processId, int maxProcesses) {
        INSTANCE.processId = processId;
        INSTANCE.clockRate = maxProcesses + (int)(Math.random() * ((maxProcesses - INSTANCE.processId) + 1));
        BlockingConsoleLogger.INSTANCE.println("clock rate set as:" + INSTANCE.clockRate);
    }
    
}
