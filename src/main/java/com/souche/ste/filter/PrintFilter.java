package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * extract抽取
 * @author hongwm
 * @since 2013年11月7日
 */
public class PrintFilter extends SteFilter {
	private String spliter = " ";
	private List<String> attrList = 
			new ArrayList<String>();
	
	@Override
	public void process(Context context, SteConfig config) {
		for(String attr : attrList) {
			super.print(attr + ": " + context.getVariable(attr));
		}
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
		}
	}

}
