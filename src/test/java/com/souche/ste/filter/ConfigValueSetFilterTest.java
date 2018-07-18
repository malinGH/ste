package com.souche.ste.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class ConfigValueSetFilterTest {

	@Test
	public void testProcess() {
		ConfigValueSetFilter filter = new ConfigValueSetFilter();
		
		Context context = new Context();
		context.setVariable("seller", "http://www.hx2car.com/profile/126150");
		
		filter.addParam("config", "sellphone");
		filter.addParam("key", "seller");
		filter.addParam("to", "phone");
		
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("phone"));
		assertEquals("13521173192", context.getVariable("phone"));
		
	}

}
