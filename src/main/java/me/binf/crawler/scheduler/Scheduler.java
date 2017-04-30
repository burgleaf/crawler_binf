package me.binf.crawler.scheduler;

/**
 * Created by burgl on 2017/4/29.
 */
public interface Scheduler {


    public void push(String url);

    public String poll();


}
