package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

public class StopJudgeFilter extends SteFilter {

	@Override
	public void process(Context context, SteConfig config) {
		String test = getParam("test");
		if(test == null) {
			return;
		}
		
		String testVar = (String) context.getVariable(test);
		if(testVar != null && !"true".equals(testVar)) {
			ProcessUtil.markStop(context);
		}
	}

}
