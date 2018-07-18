package com.souche.ste.download;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadUtil {
	private static Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

	public static String download(String url) {
		return download(url, null, null, false, null);
	}

	public static String download(String url, List<Header> headers, Map<String, String> params, boolean isPost, HttpHost proxy) {
		if(proxy != null && !ProxyChecker.isGoodProxy(proxy, null)) {
			proxy = null;
		}

		try {

		    if(headers == null) {
		        headers = new ArrayList<Header>();
		    }
			headers.add(new BasicHeader("Content-Type", "text/plain"));
			headers.add(new BasicHeader("Content-Encoding", "text"));
			
			byte[] bytes = null;
			if(!isPost) {
			    bytes = HttpClientUtil.getBytes(url, headers, proxy);
			} else {
			    bytes = HttpClientUtil.postBytes(url, headers, params, proxy);
			}
			
			return new CharSetDetect(bytes).getutf8String();
			
		} catch (Exception e) {
		    return null;
		}
	}


	public static void main(String[] args) {
		HttpHost proxy = new HttpHost("114.228.155.80", 809);
		String content = DownloadUtil.download("http://www.youdaili.net/Daili/guonei/3450_4.html", null, null, false, proxy);
		System.out.println(content);
//		DownloadUtil.removeProxy();
//		content = DownloadUtil.download("http://www.baidu.com/");
//		System.out.println(content.length());
	}
}
