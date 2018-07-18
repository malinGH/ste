package com.souche.ste.filter;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

public class ImgExtractFilterTest {
	
	@Test
	public void testExtract() {
		ImgExtractFilter filter = new ImgExtractFilter();
		
		filter.addParam("from", "from");
		filter.addParam("url", "url");		
		
		Context context = new Context();
		context.setVariable("from", ProcessUtil.readFromFile("test/img_test"));
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("url"));
		
	}
}
