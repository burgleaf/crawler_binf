package me.binf.crawler;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.dialect.log4j2.Log4j2LogFactory;
import me.binf.crawler.downloader.Downloader;
import me.binf.crawler.downloader.UrlDownloader;
import me.binf.crawler.pipeline.ConsolePipeline;
import me.binf.crawler.pipeline.Pipeline;
import me.binf.crawler.processor.PageProcessor;
import me.binf.crawler.scheduler.QueueScheduler;
import me.binf.crawler.scheduler.Scheduler;
import me.binf.crawler.utils.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by burgl on 2017/4/29.
 */
public class Spider implements Runnable{

    private static final Log log = Log4j2LogFactory.get();
    //初始化url
    protected List<Request> startRequests;

    //处理页面
    protected PageProcessor pageProcessor;

    //下载页面
    protected Downloader downloader ;

    //处理页面过滤后的数据
    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();



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
    }


    protected void initComponent() {
        if (downloader == null) {
            this.downloader = new UrlDownloader();
        }
        if(pipelines.isEmpty()){
            pipelines.add(new ConsolePipeline());
        }
        if(startRequests!=null){
            startRequests.forEach(request->{
                scheduler.push(request);
            });
            startRequests.clear();
        }
    }


    public void addRequest(Request request){
        if(site.getDomain()==null&&request!=null&&request.getUrl()!=null){
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }
        scheduler.push(request);
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

    public Spider addUrl(String... urls){
        for(String url: urls){
            addRequest(new Request(url));
        }
        return this;
    }


    @Override
    public void run() {
        initComponent();
        while (!Thread.currentThread().isInterrupted()){
            final Request request = scheduler.poll();
            if(request==null){
                waitNewUrl();
            }else{
                processorPage(request);
            }
        }
    }


    protected void processorPage(Request request){
        Page page =downloader.download(request);
        pageProcessor.process(page);
        extractAndAddRequests(page);
        pipelines.forEach(pipeline -> {
            pipeline.process(page.getResultItems());
        });
    }


    protected void extractAndAddRequests(Page page) {
        if (CollectionUtils.isNotEmpty(page.getTargetRequests())) {
            page.getTargetRequests().forEach(request ->{
                addRequest(request);
            });
        }
    }






}
