package me.binf.crawler;


import me.binf.crawler.model.HttpRequestBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by burgl on 2017/5/24.
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 2062192774891352043L;


    private String url;

    private String method;

    private HttpRequestBody requestBody;


    private Map<String,Object> extras;

    private Map<String, String> cookies = new HashMap<String, String>();

    private Map<String, String> headers = new HashMap<String, String>();


    public java.lang.Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Request putExtra(String key, java.lang.Object value) {
        if (extras == null) {
            extras = new HashMap<String, java.lang.Object>();
        }
        extras.put(key, value);
        return this;
    }


    public Request() {
    }

    public Request(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The http method of the request. Get for default.
     * @return httpMethod
     * @since 0.5.0
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Request addCookie(String name, String value) {
        cookies.put(name, value);
        return this;
    }

    public Request addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(HttpRequestBody requestBody) {
        this.requestBody = requestBody;
    }


    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", extras=" + extras +
                ", headers=" + headers +
                ", cookies="+ cookies+
                '}';
    }
}
