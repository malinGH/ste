package com.souche.ste.download;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpHost;
import org.springframework.util.CollectionUtils;

import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.ProcessUtil;
import com.souche.ste.util.StringUtil;

public class ProxyChecker {
	private static Map<String, CheckStatus> proxyCheckedMap = 
			new ConcurrentHashMap<String, CheckStatus>();

	private static List<HttpHost> proxys;
	static {
		loadProxys();
	}

	/**
	 * 加载代理文件
	 */
	protected static void loadProxys() {
		proxys = new ArrayList<HttpHost>();
		try {
			InputStream input = ProcessUtil.getAbsoluteFilePathStream("proxy.list");
			InputStreamReader reader = new InputStreamReader(input);

			BufferedReader r = new BufferedReader(reader);
			String line = null;
			while((line = r.readLine()) != null) {
				if(StringUtil.isEmpty(line)) {
					continue;
				}
				if(!Character.isDigit(line.charAt(0))) {
					continue;
				}

				String[] h = line.split(":");
				if(h.length != 2) {
					continue;
				}

				proxys.add(new HttpHost(h[0], CommonUtil.getIntValue(h[1])));
			}
			r.close();
		} catch (IOException e) {
		}

	}

	/**
	 * 获取下一个代理的地址
	 * @return
	 */
	public static HttpHost nextProxy() {
		if(CollectionUtils.isEmpty(proxys)) {
			return null;
		}

		int retry = 0;

		while(retry <= 10) {
			int randId = ProcessUtil.randomNum(proxys.size());
			HttpHost proxy = proxys.get(randId);
			if(proxy != null && isGoodProxy(proxy, null)) {
				return proxy;
			}
			retry ++;
		}

		return null;
	}

	public static boolean isGoodProxy(HttpHost proxyHost, List<String> checkUrls) {
		if(CollectionUtils.isEmpty(checkUrls)) {
			checkUrls = new ArrayList<String>();
			checkUrls.add("http://www.baidu.com/");
			checkUrls.add("http://www.sina.com.cn/");
			checkUrls.add("http://www.taobao.com/");
//			checkUrls.add("http://quanguo.58.com/ershouche/0/");
//						checkUrls.add("http://www.sina.com.cn/");
		}

		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost.getHostName(), proxyHost.getPort()));

		String address = proxy.address().toString();
		CheckStatus checkedStatus = proxyCheckedMap.get(address);
		if(checkedStatus != null && checkedStatus.isLegalCheck()) {
			return checkedStatus.getStatus();
		}

		boolean isGoodProxy = true;
		for(String url : checkUrls) {
			try {
				URL u = new URL(url);
				URLConnection connection = u.openConnection(proxy);
				connection.setConnectTimeout(600);
				connection.setReadTimeout(1200);
				connection.connect();
				if(CollectionUtils.isEmpty(connection.getHeaderFields())) {
					isGoodProxy = false;
					break;
				}

				boolean success = false;
				for(List<String> vs : connection.getHeaderFields().values()) {
					for(String value : vs) {
						if(success || value.toUpperCase().indexOf("200 OK") != -1) {
							success = true;
							break;
						}
					}
				}

				connection.getContent();

				if(!success) {
					isGoodProxy = false;
					break;
				}
			} catch (IOException e) {
				isGoodProxy = false;
				break;
			}
		}

		proxyCheckedMap.put(address, new CheckStatus(isGoodProxy));

		return isGoodProxy;

	}

	private static class CheckStatus {
		private long checkTime;
		private boolean status;

		public CheckStatus(boolean status) {
			this.checkTime = System.currentTimeMillis();
			this.status = status;
		}

		public boolean getStatus() {
			return status;
		}

		public boolean isLegalCheck() {
			// 2分钟内的检查有效
			return System.currentTimeMillis() - checkTime < 120 * 1000;
		}
	}

	public static void main(String[] args) throws IOException {

		//		for(int i=0; i<30; i++) {
		//			System.out.println(ProxyChecker.nextProxy());
		//		}
		check();
	}

	protected static void check() throws IOException {


		Map<String, Integer> m = 
				new HashMap<String, Integer>();
		String path = CommonUtil.getAbsoluteFilePath("proxy.list");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(path));
		String line = null;
		while((line = StringUtil.readLine(reader)) != null) {
			String[] s = line.split(":");
			if(s.length != 2) {
				continue;
			}
			HttpHost proxy = new HttpHost(s[0], Integer.parseInt(s[1]));
			if(ProxyChecker.isGoodProxy(proxy, null)) {
				System.out.println(line);
			} else {
//				System.out.println(line + " false");
			}
//			System.out.println(line + "\t" + ProxyChecker.isGoodProxy(proxy, null));

		}
	}
}
