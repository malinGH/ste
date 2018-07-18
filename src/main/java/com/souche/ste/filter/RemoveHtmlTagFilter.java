package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.StringUtil;

/**
 * 去除html标签过滤器
 * @author hongwm
 * @since 2013年11月7日
 */
public class RemoveHtmlTagFilter extends SteFilter {
	
	private List<String> attrs = 
			new ArrayList<String>();
	
	@Override
	public void process(Context context, SteConfig config) {
		for(String attr : attrs) {
			String value = (String) context.getVariable(attr);
			if(value != null) {
				int startIdx = value.indexOf('<');
				while(startIdx != -1) {
					int endIdx = value.indexOf(">", startIdx);
					if(endIdx == -1) {
						break;
					}
					value = value.substring(0, startIdx) + 
							value.substring(endIdx+1, value.length());
					startIdx = value.indexOf('<');
				}
				value = StringUtil.trim(value);
				context.setVariable(attr, value);
			}
		}
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if(key.equals("attrs")) {
			String[] attrs = value.split(" ");
			for(String attr : attrs) {
				this.attrs.add(attr);
			}
		}
	}

}
