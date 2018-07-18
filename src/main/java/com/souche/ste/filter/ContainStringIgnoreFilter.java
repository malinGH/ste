package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 包含内容忽略过滤器，如果check对应的值中包含了特定的关键字，则忽略该条结果
 * 
 * <pre>
 * 例子
 * filter.class:com.souche.ste.filter.ContainStringIgnoreFilter
      check:model_url
      contains:click
    上面的filter，会判断model_url中是否含有'click'关键字，如果含有，则忽略该条记录
    
    参数说明：
      check: 要检查的变量
      contains: 要检查包含的信息
 * </pre>
 * @author hongwm
 * @since 2014年4月19日
 */
public class ContainStringIgnoreFilter extends SteFilter implements IgnoreFilter {
	private String check = "url";
	private String contains = "";
	
	private boolean ignore = false;
	
	@Override
	public boolean ignore() {
		return ignore;
	}

	@Override
	public void process(Context context, SteConfig config) {
		ignore = false;
		
		String checkVal = (String) context.getVariable(check);
		String containsVal = (String) context.getVariable(contains);
		if(containsVal == null) {
			containsVal = contains;
		}
		if(checkVal != null && containsVal != null) {
			if(checkVal.indexOf(containsVal) != -1) {
				ignore = true;
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
