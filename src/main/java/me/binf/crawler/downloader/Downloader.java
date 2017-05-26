package me.binf.crawler.downloader;

import me.binf.crawler.Page;
import me.binf.crawler.Request;
import me.binf.crawler.Site;

/**
 * Created by burgl on 2017/4/30.
 */
public interface Downloader {

    public Page download(Request request, Site site);

}
