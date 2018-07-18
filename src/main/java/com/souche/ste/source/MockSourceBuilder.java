package com.souche.ste.source;

import com.souche.ste.context.Context;

/**
 * 来源处理器
 * @author hongwm
 * @since 2013年11月7日
 */
public class MockSourceBuilder extends SourceBuilder {
	public void process(Context context) {
		context.setVariable("body", context.getVariable("test"));
	}

}
