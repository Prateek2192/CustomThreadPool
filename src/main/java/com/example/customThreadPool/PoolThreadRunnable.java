package com.example.customThreadPool;

import java.util.concurrent.BlockingQueue;

public class PoolThreadRunnable implements Runnable {

    private final BlockingQueue taskQueue;
    private boolean isStopped = false;
    private Thread thread;

    public PoolThreadRunnable(BlockingQueue queue) {
        this.taskQueue = queue;
    }

    @Override
    public void run() {
        this.thread = Thread.currentThread();
        while (!isStopped) {
            try {
                Runnable runnable = (Runnable) taskQueue.take();
                runnable.run();
            } catch (Exception e) {
            }
        }
    }

    public synchronized void doStop() {
        isStopped = true;
        this.thread.interrupt();
    }

    public synchronized boolean isStopped() {
        return isStopped;
    }
}
