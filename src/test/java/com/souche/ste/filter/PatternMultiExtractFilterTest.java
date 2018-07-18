package com.souche.ste.filter;

import junit.framework.Assert;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class PatternMultiExtractFilterTest {

	@Test
	public void testProcess() {
        
        Context context = new Context();
        context.setVariable("key", "你好，我的电话是13666608929");
        
        PatternExtractFilter filter = new PatternExtractFilter();
		filter.addParam("from", "key");
		filter.addParam("to", "to");
		filter.addParam("pattern", "1\\d{10}");
		
		
		filter.process(context, new SteConfig());
		System.out.println(context.getVariable("to"));
		Assert.assertEquals(context.getVariable("to"), "13666608929");
	}

}
