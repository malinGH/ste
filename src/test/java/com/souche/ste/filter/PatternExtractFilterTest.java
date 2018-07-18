package com.souche.ste.filter;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;

public class PatternExtractFilterTest {

	@Test
	public void testProcess() {
        String str = ProcessUtil.readFromFile("test/che168");
        
        Context context = new Context();
        context.setVariable("key1", str);
        RemoveHtmlTagFilter m = new RemoveHtmlTagFilter();
        m.addParam("attrs", "key1");
        m.process(context, new SteConfig());
        
        PatternMultiExtractFilter filter = new PatternMultiExtractFilter();
		filter.addParam("from", "key1");
		filter.addParam("to", "to");
		filter.addParam("pattern", "1\\d{10}");

		
		
		int n = 0;
		filter.process(context, new SteConfig());
		System.out.println(context.getVariable("to"));
		while(filter.hasNext()) {
			filter.process(context, new SteConfig());
			System.out.println(context.getVariable("to"));
			n ++;
		}
	}

}
