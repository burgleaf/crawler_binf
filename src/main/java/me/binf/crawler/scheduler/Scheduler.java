package me.binf.crawler.scheduler;

import me.binf.crawler.Request;

/**
 * Created by burgl on 2017/4/29.
 */
public interface Scheduler {


    public void push(Request request);

    public Request poll();


}
