package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;

/**
 * 最大值限制过滤器，限制attr中的值不能超过设定值
 * 
 * @author hongwm
 *
 */
public class MaxValueLimitFilter extends SteFilter {
	private String attr;
	private int maxValue = 100;

	@Override
	public void process(Context context, SteConfig config) {
		String value = (String) context.getVariable(attr);
		if(maxValue < CommonUtil.getIntValue(value)) {
			value = String.valueOf(maxValue);
		}
		
		context.setVariable(attr, value);

	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("attr".equals(key)) {
			attr = value;
		} else if("maxValue".equals(key)) {
			maxValue = CommonUtil.getIntValue(value);
		}
	}

}
