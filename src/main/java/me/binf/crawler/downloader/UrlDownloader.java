package me.binf.crawler.downloader;

import me.binf.crawler.Page;
import me.binf.crawler.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by burgl on 2017/4/30.
 */
public class UrlDownloader implements Downloader{



    @Override
    public Page download(Request request) {
        Page page = new Page();
        String rawText =  sendGet(request.getUrl());
        Document document = Jsoup.parse(rawText);
        page.setRawText(rawText);
        page.setDocument(document);
        page.setRequest(request);
        return page;
    }


    private  String sendGet(String url){
        String result = "";
        try {
            BufferedReader in =null;
            try {
                URL realUrl = new URL(url);
                URLConnection connection = realUrl.openConnection();
                connection.connect();
                in =new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine())!=null){
                    result+=line;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                in.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

}
