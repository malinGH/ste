package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;

/**
 * extract抽取
 * @author hongwm
 * @since 2013年11月7日
 */
public class SleepFilter extends SteFilter {
	private int sleep = 1000;
	
	@Override
	public void process(Context context, SteConfig config) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
		}
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if(key.equals("sleep")) {
			sleep = CommonUtil.getIntValue(value);
			if(sleep < 100) {
				sleep = 1000;
			}
		}
	}

}
