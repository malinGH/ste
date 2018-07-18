package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 如果不包含内容，则忽略该记录的过滤器
 * @author hongwm
 * @since 2014年7月4日
 */
public class NotContainIgnoreFilter extends SteFilter implements IgnoreFilter {
	private String check = "url";
	private String contains = "";
	
	private boolean ignore = false;
	
	@Override
	public boolean ignore() {
		return ignore;
	}

	@Override
	public void process(Context context, SteConfig config) {
		ignore = true;
		
		String checkVal = (String) context.getVariable(check);
		String containsVal = (String) context.getVariable(contains);
		if(containsVal == null) {
			containsVal = contains;
		}
		if(checkVal != null && containsVal != null) {
			if(checkVal.indexOf(containsVal) != -1) {
				ignore = false;
			}
		}

	}

	public void addParam(String key, String value) {
		if("check".equals(key)) {
			check = value;
		} else if("contains".equals(key)) {
			contains = value;
		}
	}
	
	public void reset() {
		ignore = false;
	}
}
