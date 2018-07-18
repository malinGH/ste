package com.souche.ste.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class ExtractFilterTest {

	@Test
	public void testProcess() {
		ExtractFilter filter = new ExtractFilter();
		filter.addParam("begin", "abc");
		filter.addParam("end", "def");
		filter.addParam("from", "fromvar");
		filter.addParam("to", "tovar");
		
		Context context = new Context();
		context.setVariable("fromvar", "abcxydef");
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("tovar"));
		assertEquals("xy", context.getVariable("tovar"));

		context.setVariable("fromvar", "abdexydef");
		filter.process(context, new SteConfig());
		System.out.println(context.getVariable("tovar"));
		assertEquals("abdexy", context.getVariable("tovar"));

		context.setVariable("fromvar", "abdexyde");
		filter.process(context, new SteConfig());
		System.out.println(context.getVariable("tovar"));
		assertEquals("abdexyde", context.getVariable("tovar"));

	
	}

}
