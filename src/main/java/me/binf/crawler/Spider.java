package me.binf.crawler;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.dialect.log4j2.Log4j2LogFactory;
import me.binf.crawler.downloader.Downloader;
import me.binf.crawler.downloader.HttpClientDownloader;
import me.binf.crawler.pipeline.ConsolePipeline;
import me.binf.crawler.pipeline.Pipeline;
import me.binf.crawler.processor.PageProcessor;
import me.binf.crawler.scheduler.QueueScheduler;
import me.binf.crawler.scheduler.Scheduler;
import me.binf.crawler.thread.CountableThreadPool;
import me.binf.crawler.utils.UrlUtils;
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

    private static final Log logger = Log4j2LogFactory.get();

    protected Site site;

    //初始化url
    protected List<Request> startRequests;

    //处理页面
    protected PageProcessor pageProcessor;

    //页面下载器
    protected Downloader downloader ;

    //URL队列
    private Scheduler scheduler = new QueueScheduler();

    //处理页面过滤后的数据
    protected List<Pipeline> pipelines = new ArrayList<Pipeline>();

    protected int threadNum = 1;


    protected CountableThreadPool threadPool;

    protected ExecutorService executorService;


    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();



    private int emptySleepTime = 30000;


    public static Spider create(PageProcessor pageProcessor) {
        return new Spider(pageProcessor);
    }


    public Spider(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
        this.site = pageProcessor.getSite();
    }

    /**
     * 设置线程数
     * @param threadNum
     * @return
     */
    public Spider thread(int threadNum){
        this.threadNum= threadNum;
        if(threadNum<=0){
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    /**
     * 初始化各模块
     */
    protected void initComponent() {
        //初始化下载模块
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();
        }
        //初始化处理数据模块
        if(pipelines.isEmpty()){
            pipelines.add(new ConsolePipeline());
        }
        //初始化URL
        if(startRequests!=null){
            startRequests.forEach(request->{
                scheduler.push(request);
            });
            startRequests.clear();
        }
        //初始化线程池
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }

    }


    @Override
    public void run() {
        initComponent();
        while (!Thread.currentThread().isInterrupted()){
            final Request request = scheduler.poll();
            if(request==null){
                waitNewUrl();
            }else{
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            processRequest(request);
                        }catch (Exception e){
                            logger.error("process request " + request + " error", e);
                        }
                    }
                });

            }
        }
    }



    public void addRequest(Request request){
        if(site.getDomain()==null&&request!=null&&request.getUrl()!=null){
            site.setDomain(UrlUtils.getDomain(request.getUrl()));
        }
        scheduler.push(request);
    }

    /**
     * 等待加入新URL
     */
    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    /**
     * 添加新的url到队列
     * @param urls
     * @return
     */
    public Spider addUrl(String... urls){
        for(String url: urls){
            addRequest(new Request(url));
        }
        return this;
    }


    /**
     * 处理request
     * @param request
     */
    protected void processRequest(Request request){
        //下载页面
        Page page =downloader.download(request,site);
        if(page.isDownloadSuccess()){
            onDownloadSuccess(request, page);
        }

    }

    /**
     * 添加新URL到队列
     * @param page
     */
    protected void extractAndAddRequests(Page page) {
        if (CollectionUtils.isNotEmpty(page.getTargetRequests())) {
            page.getTargetRequests().forEach(request ->{
                addRequest(request);
            });
        }
    }


    /**
     * 处理下载成功的请求
     * @param request
     * @param page
     */
    private void onDownloadSuccess(Request request,Page page){
        if(site.getAcceptStatCode().contains(page.getStatusCode())){
            //处理页面
            pageProcessor.process(page);
            extractAndAddRequests(page);
            //过滤页面信息
            if(!page.getResultItems().isSkip()){
                pipelines.forEach(pipeline -> {
                    pipeline.process(page.getResultItems());
                });
            }
        }
        sleep(site.getSleepTime());
    }

    /**
     * 每个线程请求间隔
     * @param time
     */
    protected void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
          logger.error("Thread interrupted when sleep",e);
        }
    }

    /**
     * 异步运行爬虫线程
     */
    public void runAsync(){
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    /**
     * 异步启动线程
     */
    public void start(){
        runAsync();
    }

}
