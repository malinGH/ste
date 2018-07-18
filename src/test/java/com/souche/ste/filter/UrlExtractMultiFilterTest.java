package com.souche.ste.filter;

import org.junit.Test;
import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

public class UrlExtractMultiFilterTest {

	@Test
	public void testProcess() {
		UrlExtractMultiFilter multiFilter = new UrlExtractMultiFilter();
		multiFilter.addParam("from", "fromVariable");
		multiFilter.addParam("anchor", "anchor");
		multiFilter.addParam("url", "url");
		
        String str = ProcessUtil.readFromFile("test/url_test_file");
		
		Context context = new Context();
		context.setVariable("fromVariable", str);
	
		multiFilter.process(context, new SteConfig());
		System.out.println(context.getVariable("url"));
		System.out.println(context.getVariable("anchor"));
		while(multiFilter.hasNext()) {
			multiFilter.process(context, new SteConfig());
			System.out.println(context.getVariable("url"));
			System.out.println(context.getVariable("anchor"));
		}
	}
}
