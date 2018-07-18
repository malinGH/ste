package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 把attrs中的变量进行拼接，把结果赋给to对应的变量
 * <pre>
 * 例子：
 * 假设num中的值为1，num2中的值为2
 * filter.class:com.souche.ste.filter.BindStringFilter
 * 	attrs:"abc" num num2
 *  to:param
 * 最终param对应的值为abc12
 * 
 * 变量说明：
 *  attrs： 要合并的变量或值
 *  to: 要拼接到哪个变量中
 * <pre>
 * 
 * @author hongwm
 * @since 2013年11月7日
 */
public class BindStringFilter extends SteFilter {
	private String spliter = " ";
	private List<String> attrList = 
			new ArrayList<String>();
	
	private String to = "to";
	
	@Override
	public void process(Context context, SteConfig config) {
		StringBuffer sb = new StringBuffer();
		for(String attr : attrList) {
			String value = null;
			if(attr.length() >= 2 && attr.startsWith("\"") && attr.endsWith("\"")) {
				value = attr.substring(1, attr.length() - 1);
			} else {
				value = (String) context.getVariable(attr);
			}
			
			if(value == null) {
				value = attr;
			} else {
			}
			sb.append(value);
		}
		context.setVariable(to, sb.toString());
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if(key.equals("spliter")) {
			spliter = value;
		} else if(key.equals("attrs")){
			attrList.clear();
			String[] attrs = value.split(spliter);
			
			for(String attr : attrs) {
				attrList.add(attr);
			}
		} else if(key.equals("to")) {
			to = value;
		}
	}

}
