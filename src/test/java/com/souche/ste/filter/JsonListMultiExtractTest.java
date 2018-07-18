package com.souche.ste.filter;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class JsonListMultiExtractTest {

	@Test
	public void testProcess() {
		JSONArray array = new JSONArray();
		array.put("a");
		array.put("b");
		array.put("c");
		
		Context context = new Context();
		context.setVariable("key", array.toString());
		JsonListMultiExtract extract = new JsonListMultiExtract();
		extract.addParam("from", "key");
		extract.addParam("to", "to");
		
		extract.process(context, new SteConfig());
		Assert.assertEquals("a", context.getVariable("to"));
		Assert.assertTrue(extract.hasNext());
		extract.process(context, new SteConfig());
		Assert.assertEquals("b", context.getVariable("to"));
		Assert.assertTrue(extract.hasNext());
		extract.process(context, new SteConfig());
		Assert.assertEquals("c", context.getVariable("to"));
		Assert.assertFalse(extract.hasNext());
	}

}
