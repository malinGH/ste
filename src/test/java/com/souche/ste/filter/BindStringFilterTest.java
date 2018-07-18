package com.souche.ste.filter;

import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class BindStringFilterTest {

	@Test
	public void testProcess() {
		BindStringFilter filter = new BindStringFilter();
		Context context = new Context();
		context.setVariable("url", "123");
		
		filter.addParam("to", "key");
		filter.addParam("attrs", "\"http://www.test.com/\" url");
		
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("key"));
		Assert.assertEquals("http://www.test.com/123", context.getVariable("key"));;
		context.setVariable("url", "124");
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("key"));
		Assert.assertEquals("http://www.test.com/124", context.getVariable("key"));
		
		filter.addParam("spliter", ",");
		filter.addParam("attrs", "http://www.test.com/,url");
		Assert.assertEquals("http://www.test.com/124", context.getVariable("key"));

	}
//	http://api.car.bitauto.com/CarInfo/getlefttreejson.ashx?tagtype=baojia&pagetype=serial&objid=2921&
}
