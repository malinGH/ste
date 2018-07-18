package com.souche.ste.filter;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class JsonMapMultiExtractTest {

	@Test
	public void testProcess() {
		JSONObject array = new JSONObject();
		array.put("a", "abcd");
		array.put("b", "bcd");
		array.put("c", "cde");
		
		Context context = new Context();
		context.setVariable("from", array.toString());
		JsonMapMultiExtract extract = new JsonMapMultiExtract();
		extract.addParam("from", "from");
		extract.addParam("key", "key");
		extract.addParam("value", "value");
		
		extract.process(context, new SteConfig());
		System.out.println(context.getVariable("key"));
		System.out.println(context.getVariable("value"));
//		Assert.assertEquals("a", context.getVariable("key"));
		Assert.assertTrue(extract.hasNext());
		extract.process(context, new SteConfig());
		System.out.println(context.getVariable("key"));
		System.out.println(context.getVariable("value"));
//		Assert.assertEquals("b", context.getVariable("key"));
		Assert.assertTrue(extract.hasNext());
		extract.process(context, new SteConfig());
		System.out.println(context.getVariable("key"));
		System.out.println(context.getVariable("value"));
//		Assert.assertEquals("c", context.getVariable("key"));
		Assert.assertFalse(extract.hasNext());
	}

}
