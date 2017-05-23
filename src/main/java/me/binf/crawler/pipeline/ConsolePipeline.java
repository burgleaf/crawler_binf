package me.binf.crawler.pipeline;

import me.binf.crawler.ResultItems;

import java.util.Map;

/**
 * Created by burgl on 2017/5/23.
 */
public class ConsolePipeline implements Pipeline{


    @Override
    public void process(ResultItems resultItems) {
        resultItems.getAll().forEach((k,v)->{
            System.out.println(k+":\t"+v);
        });
    }
}
