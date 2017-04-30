package me.binf.crawler;

import me.binf.crawler.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burgl on 2017/4/29.
 */
public class Page {

    private String url;

    private String rawText;

    private Document document;

    private List<String> targetUrls = new ArrayList<>();


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<String> getTargetUrls() {
        return targetUrls;
    }

    public void setTargetUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }


    public void addTargetRequest(String requestString) {
        if (StringUtils.isBlank(requestString) || requestString.equals("#")) {
            return;
        }
        requestString = UrlUtils.canonicalizeUrl(requestString, url.toString());
        targetUrls.add(requestString);
    }
}
