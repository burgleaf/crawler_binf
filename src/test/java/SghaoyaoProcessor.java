import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import me.binf.crawler.Page;
import me.binf.crawler.Site;
import me.binf.crawler.Spider;
import me.binf.crawler.processor.PageProcessor;
import org.jsoup.select.Elements;



/**
 * Created by burgl on 2017/3/5.
 */
public class SghaoyaoProcessor implements PageProcessor{

    private static final Log logger = LogFactory.get();



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
        return Site.me().setDomain("http://www.sghaoyao.com").setSleepTime(100);
    }



}
