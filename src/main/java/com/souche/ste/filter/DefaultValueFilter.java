package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.Entry;

/**
 * 给变量列表设置默认的值
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.DefaultValueFilter
    source:che168.com
    is_newcar:0
   设置source为'che168.com'，is_newcar变量为0
 * </pre>
 * @author hongwm
 * @since 2013年11月7日
 */
public class DefaultValueFilter extends SteFilter {
	private List<Entry<String, String>> defaultValueList = 
			new ArrayList<Entry<String, String>>();
	
	@Override
	public void process(Context context, SteConfig config) {
		for(Entry<String, String> e : defaultValueList) {
			String key = e.getKey();
			if(context.getVariable(key) == null) {
				context.setVariable(key, e.getValue());
			}
		}
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		defaultValueList.add(new Entry<String, String>(key, value));
	}

}
