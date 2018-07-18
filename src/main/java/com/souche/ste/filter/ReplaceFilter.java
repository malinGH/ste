package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.Entry;

/**
 * Replace过滤器，用于值替换
 * @author hongwm
 * @since 2013年11月7日
 */
public class ReplaceFilter extends SteFilter {
	private String spliter = ",";
	private List<Entry<String, String>> replaceList = 
			new ArrayList<Entry<String, String>>();
	
	@Override
	public void process(Context context, SteConfig config) {
		for(Entry<String, String> e : replaceList) {
			String key = e.getKey();
			String replaceStr = e.getValue();
			String[] v = replaceStr.split(spliter, 2);
			if(v.length != 2) {
				continue;
			}
			
			String keyVal = String.valueOf(context.getVariable(key));
			if(keyVal == null) {
				continue;
			}
			
			keyVal = keyVal.replaceAll(v[0], v[1]);
			context.setVariable(key, keyVal);
		}
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if(key.equals("spliter")) {
			spliter = value;
		} else {
			replaceList.add(new Entry<String, String>(key, value));
		}
	}

}
