package me.binf.crawler;

import me.binf.crawler.utils.HttpConstant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 爬虫配置信息
 * Created by burgl on 2017/4/29.
 */
public class Site {

    private String domain;

    private String userAgent;

    private String charset;

    private int timeOut = 5000;

    private int sleepTime = 1000;

    private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();

    private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;

    static {
        DEFAULT_STATUS_CODE_SET.add(HttpConstant.StatusCode.CODE_200);
    }


    private Map<String, String> headers = new HashMap<String, String>();

    public static Site me() {
        return new Site();
    }

    /**
     * 设置域名
     * @param domain
     * @return
     */
    public Site setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * 添加消息头
     * @param key
     * @param value
     * @return
     */
    public Site addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * 添加客户端信息
     * @param userAgent
     * @return
     */
    public Site setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }
    /**
     * 添加请求超时时间
     * @param timeOut
     * @return
     */
    public Site setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     *  添加可以接受的http状态码
      * @param acceptStatCode
     * @return
     */
    public Site setAcceptStatCode(Set<Integer> acceptStatCode){
        this.acceptStatCode = acceptStatCode;
        return this;
    }

    /**
     * 添加编码
     * @param charset
     * @return
     */
    public Site setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 设置每个线程休眠时间
     * @param sleepTime
     * @return
     */
    public Site setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }


    public int getSleepTime() {
        return sleepTime;
    }

    public String getCharset() {
        return charset;
    }

    public int getTimeOut() {
        return timeOut;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getDomain() {
        return domain;
    }

    public Set<Integer> getAcceptStatCode() {
        return acceptStatCode;
    }

}
