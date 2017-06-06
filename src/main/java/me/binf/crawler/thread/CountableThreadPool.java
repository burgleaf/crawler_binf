package me.binf.crawler.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class CountableThreadPool {

    private int threadNum;

    public CountableThreadPool(int threadNum) {
        this.threadNum = threadNum;
        this.executorService = Executors.newFixedThreadPool(threadNum);
    }

    public CountableThreadPool(int threadNum, ExecutorService executorService) {
        this.threadNum = threadNum;
        this.executorService = executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }


    public int getThreadNum() {
        return threadNum;
    }

    private ExecutorService executorService;

    public void execute(final Runnable runnable) {
        executorService.execute(runnable);
    }

    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    public void shutdown() {
        executorService.shutdown();
    }


}
