package com.souche.ste.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.souche.ste.context.Context;

public class RemoveHtmlTagFilterTest {

	@Test
	public void testProcess() {
		RemoveHtmlTagFilter filter = new RemoveHtmlTagFilter();
		filter.addParam("attrs", "key");
		
		Context context = new Context();
		context.setVariable("key", "<a href=url>abcd</a>");
		filter.process(context, null);
		
		System.out.println(context.getVariable("key"));
		assertEquals(context.getVariable("key"), "abcd");

		context.setVariable("key", "<a href=url>abcd</a");
		filter.process(context, null);
		
		System.out.println(context.getVariable("key"));
		assertEquals(context.getVariable("key"), "abcd</a");

		context.setVariable("key", "aaa<a href=url>abcd</a>");
		filter.process(context, null);
		
		System.out.println(context.getVariable("key"));
		assertEquals(context.getVariable("key"), "aaaabcd");

		assertTrue(true);
	}

}
