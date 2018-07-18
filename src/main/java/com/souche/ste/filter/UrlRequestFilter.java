package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.download.DownloadUtil;
import com.souche.ste.download.PolitenessController;
import com.souche.ste.download.ProxyChecker;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.UrlUtil;

/**
 * url链接请求处理器
 * @author hongwm
 * @since 2013年11月8日
 */
public class UrlRequestFilter extends SteFilter implements IgnoreFilter {
	private final static Logger logger = LoggerFactory.getLogger(UrlRequestFilter.class);
	private boolean ignore = false;
	
	private int minDocLen = 12000;
	
	private int maxRetry = 3;
	
	private List<Header> headers = new ArrayList<Header>();
	
	private Map<String, String> params = new HashMap<String, String>();
	
	private boolean isPost = false;
	
	@Override
	public void process(Context context, SteConfig config) {
		ignore = false;
		String url = getParam("url");
		if(url != null && 
				(url.length() <= 20 || !url.startsWith("http"))) {
			// 如果url长度比较短，或url不是以http开头
			url = (String) context.getVariable(url);
		}
		if(url == null) {
			logger.error("[error]not set url param in UrlRequestFilter");
			ignore = true;
			throw new IllegalAccessError("没有设置UrlRequestFilter的url参数");
		}
		
		HttpHost httpProxy = null;
		
		String host = UrlUtil.getHostname(url);
		PolitenessController.getInstance().checkPoliteNess(config.getPoliteness(), host);
		String content = null;
		int retry = 0;
		
		while((content == null || content.length() <= minDocLen) && retry ++ < maxRetry) {
			httpProxy = null;
			if(retry <= 2 && config != null && config.isUseProxy()) {
				httpProxy = ProxyChecker.nextProxy();
			}

			print("start to fetch " + url + ",  using proxy " + httpProxy);
			content = DownloadUtil.download(url, headers, params, isPost, httpProxy);
			if(content != null) {
				System.out.println("length of " + url + " is " + content.length());
			}
			if(!config.isUseProxy()) {
				break;
			}
		}
		
		
		String to = getParam("to");
		if(to == null) {
			to = "to";
		}
		if(content == null) {
			System.out.println("content of " + url + " is null");
			ignore = true;
		}
		context.setVariable(to, content);
//		DownloadUtil.removeProxy();
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("maxRetry".equals(key)) {
			maxRetry = CommonUtil.getIntValue(value);
			maxRetry = (maxRetry > 0) ? maxRetry : 3;
		} else if("minDocLen".equals(key)) {
			minDocLen = CommonUtil.getIntValue(value);
		}
		
		if(key.startsWith("headers.")) {
		    key = key.replace("headers.", "");
		    headers.add(new BasicHeader(key, value));
		}
		
		if("post".equals(key) 
		        && ("1".equals(value) || "true".equals(value))) {
		    // key为post，且value为1或true
		    isPost = true;
		}
		
		if(key.startsWith("params.")) {
		    key = key.replace("params.", "");
		    params.put(key, value);
		}
	}

	public void reset() {
		ignore = false;
	}

	@Override
	public boolean ignore() {
		return ignore;
	}
	
}
