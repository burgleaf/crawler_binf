import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import me.binf.crawler.Page;
import me.binf.crawler.Site;
import me.binf.crawler.processor.PageProcessor;
import org.jsoup.select.Elements;

/**
 * Created by burgl on 2017/6/6.
 */
public class OschinaProcess implements PageProcessor{





    @Override
    public void process(Page page) {
        page.putField(page.getDocument().select("title").toString(),page.getRequest().getUrl());
        Elements elements  =  page.getDocument().select("a[href]");
        elements.forEach(element -> {
            page.addTargetRequest(element.attr("href").toString());
        });

    }

    @Override
    public Site getSite() {
        return Site.me().setDomain("http://www.oschina.net").setSleepTime(100);

    }


}
