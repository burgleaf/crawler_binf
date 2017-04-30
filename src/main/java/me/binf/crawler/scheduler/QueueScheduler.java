package me.binf.crawler.scheduler;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by burgl on 2017/4/29.
 */
public class QueueScheduler implements Scheduler{

    private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public boolean isDuplicate(String url) {
        return !urls.add(url);
    }

    @Override
    public void push(String url) {
        if(!isDuplicate(url)){
            queue.add(url);
        }
    }

    @Override
    public String poll() {
        return queue.poll();
    }
}
