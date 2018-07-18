package com.souche.ste.filter;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

public class HrefExtractFilterTest {
	
	@Test
	public void testExtract() {
		HrefExtractFilter filter = new HrefExtractFilter();
		
		filter.addParam("from", "from");
		filter.addParam("url", "url");
		filter.addParam("anchor", "anchor");
		filter.addParam("id", "1");
		
		Context context = new Context();
		context.setVariable("from", ProcessUtil.readFromFile("test/href_test"));
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("url"));
		System.out.println(context.getVariable("anchor"));
		
	}
}
