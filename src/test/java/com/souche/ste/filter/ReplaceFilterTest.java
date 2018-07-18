package com.souche.ste.filter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class ReplaceFilterTest {

	@Test
	public void testProcess() {
		ReplaceFilter filter = new ReplaceFilter();
		filter.addParam("spliter", ",");
		filter.addParam("key1", "&nbsp;, ");
		filter.addParam("key1", " ,");
		filter.addParam("key2", "&nbsp;, ");
		filter.addParam("key2", "1,");
		
		
		Context context = new Context();
		context.setVariable("key1", "abc&nbsp;de");
		context.setVariable("key2", "abc&nbsp;de12");
		
		filter.process(context, new SteConfig());
		System.out.println(context.getVariable("key1"));
		System.out.println(context.getVariable("key2"));
		assertEquals("abcde", context.getVariable("key1"));
		assertEquals("abc de2", context.getVariable("key2"));
	}

}

