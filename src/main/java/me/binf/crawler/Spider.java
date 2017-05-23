package me.binf.crawler;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.log.dialect.log4j2.Log4j2LogFactory;
import me.binf.crawler.downloader.Downloader;
import me.binf.crawler.downloader.UrlDownloader;
import me.binf.crawler.pipeline.ConsolePipeline;
import me.binf.crawler.pipeline.Pipeline;
import me.binf.crawler.processor.PageProcessor;
import me.binf.crawler.scheduler.QueueScheduler;
import me.binf.crawler.scheduler.Scheduler;
import me.binf.crawler.thread.CountableThreadPool;
import org.apache.commons.collections.CollectionUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by burgl on 2017/4/29.
 */
public class Spider implements Runnable{

    Log log = Log4j2LogFactory.get();



    protected List<String> startUrls;

    protected PageProcessor pageProcessor;

    protected Downloader downloader ;


    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();


    protected int threadNum = 1;

    protected ExecutorService executorService;


    protected CountableThreadPool threadPool;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    protected Site site;

    private Scheduler scheduler = new QueueScheduler();

    private int emptySleepTime = 30000;


    public static Spider create(PageProcessor pageProcessor) {
        return new Spider(pageProcessor);
    }


    public Spider(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
        this.site = pageProcessor.getSite();
        this.startUrls = pageProcessor.getSite().getStartUrls();
    }


    protected void initComponent() {
        if (downloader == null) {
            this.downloader = new UrlDownloader();
        }
        if(pipelines.isEmpty()){
            pipelines.add(new ConsolePipeline());
        }
        if(startUrls!=null){
            startUrls.forEach(url->{
                scheduler.push(url);
            });
            startUrls.clear();
        }
    }


    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }


    @Override
    public void run() {
        initComponent();
        while (!Thread.currentThread().isInterrupted()){
            final String url = scheduler.poll();
            if(url==null){
                waitNewUrl();
            }else{
                processorPage(url);
            }
        }
    }


    protected void processorPage(String url){
        Page page =downloader.download(url);
        pageProcessor.process(page);
        extractAndAddRequests(page);
        pipelines.forEach(pipeline -> {
            pipeline.process(page.getResultItems());
        });
    }


    protected void extractAndAddRequests(Page page) {
        if (CollectionUtils.isNotEmpty(page.getTargetUrls())) {
            page.getTargetUrls().forEach(url ->{
                addRequest(url);
            });
        }
    }


    protected void  addRequest(String url){
        scheduler.push(url);
    }




}
