package me.binf.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burgl on 2017/4/29.
 */
public class Site {

    private String domain;

    private String userAgent;

    private String charset;



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
}
