package com.souche.ste.source;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;

import com.souche.ste.config.SteConfig;
import com.souche.ste.config.SteConfig.ConfigLine;
import com.souche.ste.context.Context;
import com.souche.ste.download.DownloadUtil;
import com.souche.ste.download.ProxyChecker;
import com.souche.ste.process.AbstractBuilder;

/**
 * 来源处理器
 * @author hongwm
 * @since 2013年11月7日
 */
public class SourceBuilder extends AbstractBuilder {
	String url = null;
	List<Header> headers = new ArrayList<Header>();
	private int minDocLen = 6000;
	public void process(SteConfig config, Context context) {
		String url = getParam("url");
		
		HttpHost httpProxy = null;
		
		String content = null;
		int retry = 0;
		while((content == null || content.length() <= minDocLen) && retry ++ < 3) {
			httpProxy = null;
			if(retry < 2 && config != null && config.isUseProxy()) {
				httpProxy = ProxyChecker.nextProxy();
			}

			System.out.println("start to fetch " + url + ",  using proxy " + httpProxy);
			content = DownloadUtil.download(url, headers, null, false, httpProxy);
			int len = 0;
			if(content != null) {
				len = content.length();
			}
			System.out.println("body length of " + url + ": " + len);
		}

		context.setVariable("body", content);
	}

	@Override
	public void buildConf() {
		List<ConfigLine> confLines = getConfLines();
		for(ConfigLine confLine : confLines) {
			String conf = confLine.getLine();
			String[] kv = conf.split(":", 2);
			if(kv.length != 2) {
				continue;
			}
			
			String key = kv[0];
			key = key.replaceAll(".*parameters.", "");
			addParam(key, kv[1]);
		}
	}
	
	public void addParam(String key, String value) {
		if("url".equals(key)) {
			url = value;
		}
		
		if(key.contains(".headers.")) {
			key = key.replaceAll(".*headers.", "");
			headers.add(new BasicHeader(key, value));
		}
		
		super.addParam(key, value);
	}
}
