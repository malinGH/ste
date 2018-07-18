package com.souche.ste.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;
import com.souche.ste.util.StringUtil;

/**
 * 通过配置文件，加载对应的数据
 * @author hongwm
 * @since 2014年3月25日
 */
public class ConfigValueSetFilter extends SteFilter {
	private String config;
	private String key = "key";
	private String to = "to";
	
	private Map<String, String> kvMap = 
			new HashMap<String, String>();
	
	@Override
	public void process(Context context, SteConfig config) {
		String keystr = (String) context.getVariable(key);
		if(keystr != null) {
			String toStr = kvMap.get(keystr);
			if(toStr != null) {
				context.setVariable(to, toStr);
			}
		}
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("config".equals(key)) {
			config = value;
			try {
				InputStream input = ProcessUtil.getAbsoluteFilePathStream(config);
				InputStreamReader reader = new InputStreamReader(input);
				String line = StringUtil.readLine(reader);
				while(line != null) {
					String[] kv = line.split("\\s+", 2);
					if(kv.length == 2) {
						kvMap.put(kv[0], kv[1]);
					}
					line = StringUtil.readLine(reader);
				}
				
//				System.out.println(kvMap.size() + " phones loaded");
				input.close();
			} catch (IOException e) {
			}
		} else if("key".equals(key)) {
			this.key = value;
		} else if("to".equals(key)) {
			to = value;
		}
	}

}
