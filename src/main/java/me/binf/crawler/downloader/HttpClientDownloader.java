package me.binf.crawler.downloader;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import me.binf.crawler.Page;
import me.binf.crawler.Request;
import me.binf.crawler.Site;
import me.binf.crawler.utils.CharsetUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by burgl on 2017/5/24.
 */
public class HttpClientDownloader implements Downloader{

    private static final Log logger = LogFactory.get();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();

    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();

    private CloseableHttpClient getHttpClient(Site site){
        if(site==null){
            return httpClientGenerator.generateClient(null);
        }else{
            return httpClientGenerator.generateClient(site);
        }
    }


    @Override
    public Page download(Request request,Site site) {

        logger.debug("downloading page {}",request.getUrl());
        CloseableHttpResponse httpResponse = null;

        CloseableHttpClient httpClient = getHttpClient(site);
        HttpClientRequestContext requestContext = httpUriRequestConverter.convert(request, site);
        Page page = Page.fail();
        try {
            httpResponse = httpClient.execute(requestContext.getHttpUriRequest(),requestContext.getHttpClientContext());
            page = handleResponse(request, site.getCharset(), httpResponse);
            logger.debug("downloading page success {}", page);
            return page;
        }catch (Exception e){
            logger.warn("download page {} error", request.getUrl(), e);
            return page;
        }finally {
            if (httpResponse != null) {
                //ensure the connection is released back to pool
                EntityUtils.consumeQuietly(httpResponse.getEntity());
            }
        }
    }


    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse) throws IOException {
        String content = getResponseContent(charset, httpResponse);
        Document document = Jsoup.parse(content);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(request.getUrl());
        page.setDocument(document);
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        return page;
    }

    private String getResponseContent(String charset, HttpResponse httpResponse) throws IOException {
        if (charset == null) {
            byte[] contentBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            String htmlCharset = getHtmlCharset(httpResponse, contentBytes);
            if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
                return new String(contentBytes);
            }
        } else {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }
    }

    private String getHtmlCharset(HttpResponse httpResponse, byte[] contentBytes) throws IOException {
        return CharsetUtils.detectCharset(httpResponse.getEntity().getContentType().getValue(), contentBytes);
    }
}
