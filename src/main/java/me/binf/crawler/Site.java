package me.binf.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burgl on 2017/4/29.
 */
public class Site {

    private String domain;

    protected List<String> startUrls = new ArrayList<>();

    public static Site me() {
        return new Site();
    }


    public Site addStartUrl(String startUrl){
        this.startUrls.add(startUrl);
        return this;
    }

    public List<String> getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(List<String> startUrls) {
        this.startUrls = startUrls;
    }
}
