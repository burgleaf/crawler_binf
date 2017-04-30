package me.binf.crawler.processor;

import me.binf.crawler.Page;
import me.binf.crawler.Site;

/**
 * Created by burgl on 2017/4/29.
 */
public interface PageProcessor {

    public void process(Page page);

    public Site getSite();
}
