package com.souche.ste.download;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.log4j.Logger;

@SuppressWarnings("deprecation")
public class HttpClientUtil {
    private static Logger logger = 
            Logger.getLogger(HttpClientUtil.class);

    private static HttpClient httpClient;

    private static Map<InputStream, HttpEntity> entityMap =
            new ConcurrentHashMap<InputStream, HttpEntity>();
    
    
    private static RequestConfig defaultConfig;

    static {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy(){
                public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException{
                    return true;
                }
            }).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        defaultConfig = RequestConfig.custom().setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setMaxRedirects(4)
                .setSocketTimeout(10000)
                .setRedirectsEnabled(true)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_ppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36");
        builder.setMaxConnPerRoute(600);
        builder.setMaxConnTotal(1000);
        builder.setDefaultRequestConfig(defaultConfig);
        builder.setSslcontext(sslContext);
//        builder.setRedirectStrategy(new XcrawlerRedirectStrategy());

//        Cookies
        httpClient = builder.build();
    }

    public static HttpClient getHttpClientInstance() {
        return httpClient;
    }

    public static InputStream getInputStream(String url) {
        HttpUriRequest uriRequest =
                new HttpGet(url);

        return getInputStream(uriRequest);
    }

    public static InputStream getInputStream(HttpUriRequest uriRequest) {
        HttpResponse response = null;
        try {
            response = httpClient.execute(uriRequest);

            if(response == null || 
                    response.getStatusLine() == null) {
                return null;
            }

            int httpStatus = response.getStatusLine().getStatusCode();
            if(httpStatus != 200) {
                throw new IOException("获取链接失败, response status: " + httpStatus);
            }

            InputStream inputStream = response.getEntity().getContent();
            if(hasGzip(response)) {
                inputStream = new GZIPInputStream(inputStream);
            }
            entityMap.put(inputStream, 
                    response.getEntity());
            return inputStream;
        } catch (Exception e) {
            logger.warn("", e);
            if(response != null && response.getEntity() != null) {
                try {
                    response.getEntity().consumeContent();
                } catch (Exception e1) {
                    logger.warn("", e1);
                }
            }
            return null;
        }
    }

    private static boolean hasGzip(HttpResponse response) {
        Header[] headers = response.getHeaders("Content-Encoding");
        if(headers != null && headers.length > 0) {
            for(Header h : headers) {
                if("GZIP".equalsIgnoreCase(h.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void releaseInputStream(InputStream inputStream) {
        if(inputStream == null) {
            return;
        }

        HttpEntity entity = entityMap.get(inputStream);
        if(entity != null) {
            entityMap.remove(inputStream);
            try {
                entity.consumeContent();
                inputStream.close();
            } catch (IOException e) {
                logger.warn("", e);
            }
        }
    }

    public static byte[] getBytes(String url) {
        return getBytes(url, null);
    }

    public static byte[] postBytes(String url, List<Header> headers, Map<String, String> kvs, HttpHost proxy) throws IOException {
        HttpPost postMethod = new HttpPost(url);
        if(proxy != null) {
            RequestConfig config = RequestConfig.copy(defaultConfig).setProxy(proxy).build();
            postMethod.setConfig(config);
        }
        
        if(headers != null) {
            for(Header header : headers) {
                postMethod.addHeader(header);
            }
        }
        
        if(kvs != null && kvs.size() > 0) {
            List<NameValuePair> nvs = new ArrayList<NameValuePair>();
            for(Entry<String, String> entry : kvs.entrySet()) {
                nvs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            HttpEntity entity = new UrlEncodedFormEntity(nvs, "UTF-8");
            postMethod.setEntity(entity);
        }
        
        return getBytes(postMethod);
    }
    
    public static byte[] getBytes(String url, List<Header> headers) {
        return getBytes(url, headers, null);
    }

    public static byte[] getBytes(String url, List<Header> headers, HttpHost proxy) {
        HttpGet uriRequest =
                new HttpGet(url);

        if(headers !=  null) {
            for(Header header : headers) {
                uriRequest.addHeader(header);
            }
        }

        if(proxy != null) {
            RequestConfig config = RequestConfig.copy(defaultConfig).setProxy(proxy).build();
            uriRequest.setConfig(config);
        }

        return getBytes(uriRequest);
    }


    protected static byte[] getBytes(HttpUriRequest uriRequest) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        InputStream input = getInputStream(uriRequest);
        try {
            if(input == null) {
                return null;
            }
            byte[] buffer = new byte[1024];
            try {
                int len = 0;
                while((len = input.read(buffer)) > 0) {
                    byteStream.write(buffer, 0, len);
                }
            } catch (IOException e) {
                logger.warn("", e);
            }
            return byteStream.toByteArray();

        } finally {
            releaseInputStream(input);
        }
    }
    
    public static byte[] getBytes(String url, boolean isPost,HttpParams params) {

        if(!isPost){
            return getBytes(url, null);
        }
        
        HttpUriRequest uriRequest = new HttpPost(url);
        if(params!=null){
            uriRequest.setParams(params);
        }
        
        return getBytes(uriRequest);

    }


}
