package me.binf.crawler;

import me.binf.crawler.utils.HttpConstant;
import me.binf.crawler.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burgl on 2017/4/29.
 */
public class Page {

    private ResultItems resultItems = new ResultItems();

    private Request request;

    private String rawText;

    private int statusCode = HttpConstant.StatusCode.CODE_200;

    private String url;

    private Document document;

    private List<Request> targetRequests = new ArrayList<Request>();

    private boolean downloadSuccess = true;

    public Page(){};


    public static Page fail(){
        Page page = new Page();
        page.setDownloadSuccess(false);
        return page;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }


    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    public void setTargetRequests(List<Request> targetRequests) {
        this.targetRequests = targetRequests;
    }

    /**
     * 添加一个request对象
     * @param request
     */
    public void addTargetRequest(Request request) {
        targetRequests.add(request);
    }

    /**
     * 直接添加一个url
     * @param requestString
     */
    public void addTargetRequest(String requestString){
        if(StringUtils.isBlank(requestString)|| requestString.equals("#")){
            return;
        }
        requestString = UrlUtils.canonicalizeUrl(requestString, url);
        targetRequests.add(new Request(requestString));
    }


    public Page setSkip(boolean skip) {
        resultItems.setSkip(skip);
        return this;

    }

    public void putField(String key, Object field) {
        resultItems.put(key, field);
    }

    public ResultItems getResultItems() {
        return resultItems;
    }

    public void setResultItems(ResultItems resultItems) {
        this.resultItems = resultItems;
    }


    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isDownloadSuccess() {
        return downloadSuccess;
    }

    public void setDownloadSuccess(boolean downloadSuccess) {
        this.downloadSuccess = downloadSuccess;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
