package com.example.customThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {
    private boolean isStopped;
    private final List<PoolThreadRunnable> runnables = new ArrayList<>();
    private BlockingQueue<Runnable> taskQueue = null;


    public ThreadPool(int noOfThreads, int maxNoOfTasks) {
        taskQueue = new ArrayBlockingQueue(maxNoOfTasks);

        for (int i = 0; i < noOfThreads; i++) {
            PoolThreadRunnable poolThreadRunnable = new PoolThreadRunnable(taskQueue);
            runnables.add(poolThreadRunnable);
            new Thread(poolThreadRunnable).start();
        }
        /*for (PoolThreadRunnable runnable : runnables) {
            new Thread(runnable).start();
        }*/
    }

    public synchronized void execute(Runnable task) {
        if (isStopped)
            throw new IllegalStateException("ThreadPool is stopped");

        taskQueue.offer(task);
    }

    public synchronized void waitUntilAllTasksCompleted() {
        while (!taskQueue.isEmpty()) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void stop() {
        isStopped = true;
        for (PoolThreadRunnable runnable : runnables) {
            runnable.doStop();
        }
    }
}
