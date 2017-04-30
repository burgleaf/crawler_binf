package me.binf.crawler.downloader;

import me.binf.crawler.Page;

/**
 * Created by burgl on 2017/4/30.
 */
public interface Downloader {

    public Page download(String url);


}
