package me.binf.crawler.pipeline;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import me.binf.crawler.ResultItems;

import java.util.Map;

/**
 * Created by burgl on 2017/5/23.
 */
public class ConsolePipeline implements Pipeline{

    private static final Log logger = LogFactory.get();
    @Override
    public void process(ResultItems resultItems) {
        resultItems.getAll().forEach((k,v)->{
            logger.info(k+":\t"+v);
        });
    }
}
