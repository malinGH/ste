package com.souche.ste.context;

import org.junit.Assert;
import org.junit.Test;

public class ContextTest {

	@Test
	public void testClone() {
		Context context = new Context();
		
		context.setVariable("key", 123);
		context.setVariable("key1", 123);
		
		Context context1 = context.clone();
		System.out.println(context1.getVariable("key"));
		Assert.assertEquals(context.getVariable("key"), context1.getVariable("key"));
		Assert.assertEquals(context.getVariable("key1"), context1.getVariable("key1"));
	}

}
