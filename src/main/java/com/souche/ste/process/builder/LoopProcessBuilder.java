package com.souche.ste.process.builder;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

/**
 * 循环处理的process builder
 * @author hongwm
 * @since 2013年11月7日
 */
public class LoopProcessBuilder extends ConfigProcessBuilder {
	private boolean runed = false;
	private boolean stoploop = false;
	public void process(Context context, SteConfig config) {
		if(!runed) {
			runed = true;
			return;
		}
		
		super.process(context, config);
		if(ProcessUtil.isStoped(context)) {
			stoploop = true;
			ProcessUtil.clearStop(context);
		}
	}

	public void reset(Context context) {
		runed = false;
		stoploop = false;
		super.reset(context);
	}
	
	@Override
	public boolean nextLoop() {
		if(stoploop) {
			return false;
		}
		return true;
	}

}
