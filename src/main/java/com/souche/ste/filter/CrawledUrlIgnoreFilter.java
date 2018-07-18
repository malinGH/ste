package com.souche.ste.filter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.HashUtil;
import com.souche.ste.util.StringUtil;

/**
 * 已抓取链接忽略过滤器，如果该链接已经抓过，则跳出该次循环(抓取的链接默认放在/usr/local/ste/crawled/目录中)
 * @author hongwm
 * @since 2014年4月19日
 */
public class CrawledUrlIgnoreFilter extends SteFilter implements IgnoreFilter {
	private String crawledDir = "/usr/local/ste/crawled/";
	private Set<Long> hashSet = new HashSet<Long>();
	private DataOutputStream outputStream;
	
	private boolean ignore = false;
	
	private String check = "url";
	
	@Override
	public boolean ignore() {
		return ignore;
	}

	@Override
	public void process(Context context, SteConfig config) {
		ignore = false;
		if(outputStream == null) {
			String dir = crawledDir + config.getProptype() + "/";
			new File(dir).mkdirs();
			String file = dir + config.getPropname();
			loadCrawledHash(file);
			try {
				outputStream = new DataOutputStream(new FileOutputStream(file, true));
			} catch (Exception e) {
			}
		}
		
		String checkVal = (String) context.getVariable(check);
		if(checkVal != null) {
			long hash = HashUtil.getFPHash(checkVal);
			if(hashSet.contains(hash)) {
				System.out.println(checkVal + " ignored");
				ignore = true;
			} else {
				if(outputStream != null) {
					try {
						outputStream.write((checkVal + "\n").getBytes());
						outputStream.flush();
					} catch (IOException e) {
					}
				}
			}
		}

	}

	protected void loadCrawledHash(String file) {
		try {
			if(new File(file).exists()) {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
				String line = StringUtil.readLine(reader);
				while(line != null) {
					hashSet.add(HashUtil.getFPHash(line));
					line = StringUtil.readLine(reader);
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addParam(String key, String value) {
		if("check".equals(key)) {
			check = value;
		}
	}
	
	public void close() {
		if(outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}
	
	public void reset() {
		ignore = false;
	}
}
