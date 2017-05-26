package me.binf.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by burgl on 2017/4/29.
 */
public class Site {

    private String domain;

    private String userAgent;

    private String charset;

    private int timeOut = 5000;


    private Map<String, String> headers = new HashMap<String, String>();



    public static Site me() {
        return new Site();
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }


    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }
}
