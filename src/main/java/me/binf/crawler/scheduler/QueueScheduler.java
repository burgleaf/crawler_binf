package me.binf.crawler.scheduler;

import me.binf.crawler.Request;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by burgl on 2017/4/29.
 */
public class QueueScheduler implements Scheduler{

    private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();

    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public boolean isDuplicate(Request request) {
        return !urls.add(getUrl(request));
    }

    @Override
    public void push(Request request) {
        if(!isDuplicate(request)){
            queue.add(request);
        }
    }

    @Override
    public Request poll() {
        return queue.poll();
    }


    protected String getUrl(Request request) {
        return request.getUrl();
    }
}
