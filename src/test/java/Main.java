import me.binf.crawler.Spider;

/**
 * Created by burgl on 2017/6/6.
 */
public class Main {


    public static void main(String[] args) {
        Spider sgSpider =   Spider.create(new SghaoyaoProcessor()).addUrl("http://www.sghaoyao.com/commodity/index");
        sgSpider.start();
        Spider oscSpider =   Spider.create(new OschinaProcess()).addUrl("http://www.oschina.net/");
        oscSpider.start();
    }
}
