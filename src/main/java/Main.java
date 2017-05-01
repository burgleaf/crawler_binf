import me.binf.crawler.Page;
import me.binf.crawler.Site;
import me.binf.crawler.Spider;
import me.binf.crawler.processor.PageProcessor;
import org.jsoup.select.Elements;


/**
 * Created by burgl on 2017/3/5.
 */
public class Main implements PageProcessor{


    public static void main(String[] args) {
        Spider spider =   Spider.create(new Main());
        spider.run();
    }


    @Override
    public void process(Page page) {
//        System.out.println(page.getRawText());
        System.out.println(page.getUrl());
        System.out.println(page.getDocument().select("title").toString());
        Elements elements  =  page.getDocument().select("a[href]");
        elements.forEach(element -> {
            page.addTargetRequest(element.attr("href").toString());
        });


    }

    @Override
    public Site getSite() {
        return Site.me().addStartUrl("http://www.cnblogs.com/dongdone/p/5750272.html");
    }


}
