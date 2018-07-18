package com.souche.ste.filter;

import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class ConditionFilterTest {

	@Test
	public void testProcess() {
		ConditionFilter filter = new ConditionFilter();
		
		Context context = new Context();
		context.setVariable("url", "abcdefghelloabc");
		
		filter.addParam("attr", "url");
		filter.addParam("contains", "hello");
		filter.addParam("match.abc", "match");
		filter.addParam("unmatch.abc", "notmatch");
		
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("abc"));
		Assert.assertEquals("match", context.getVariable("abc"));
		
		context.setVariable("url", "hellab");
		filter.process(context, new SteConfig());
		Assert.assertEquals("notmatch", context.getVariable("abc"));
	}

}
