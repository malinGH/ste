package com.souche.ste.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class MultiExtractTest {

	@Test
	public void testProcess() {
		MultiExtract extract = new MultiExtract();
		
		extract.from = "fromvar";
		extract.to = "tovar";
		extract.begin = "ab";
		extract.end = "cd";
		
		List<String> results = new ArrayList<String>();
		results.add("123");
		results.add("456");
		results.add("124");
		results.add("45");
		Context context = new Context();
		context.setVariable("fromvar", "ab123cd32ab456cd32ab124cdab45cda");
		
		for(int i=0; i<3; i++) {
			extract.process(context, new SteConfig());
			System.out.println(context.getVariable("tovar"));
			Assert.assertEquals(results.get(i), context.getVariable("tovar"));
			assertTrue(extract.hasNext());
		}
		
		extract.process(context, new SteConfig());
		Assert.assertEquals(results.get(3), context.getVariable("tovar"));
		assertFalse(extract.hasNext());

		extract.reset();
		extract.begin = null;
		extract.end = "4";
		results = new ArrayList<String>();
		results.add("ab123cd32ab");
		results.add("56cd32ab12");
		results.add("cdab");
		context.setVariable("fromvar", "ab123cd32ab456cd32ab124cdab45");
		for(int i=0; i<2; i++) {
			extract.process(context, new SteConfig());
			System.out.println(context.getVariable("tovar"));
			Assert.assertEquals(results.get(i), context.getVariable("tovar"));
			assertTrue(extract.hasNext());
		}
		
		extract.process(context, new SteConfig());
		Assert.assertEquals(results.get(2), context.getVariable("tovar"));
		assertFalse(extract.hasNext());

		
		extract.reset();
		extract.begin = "a";
		extract.end = null;
		results = new ArrayList<String>();
		results.add("b123cd32");
		results.add("b456cd32");
		results.add("b124cd");
		results.add("b45");
		context.setVariable("fromvar", "ab123cd32ab456cd32ab124cdab45ac");
		for(int i=0; i<3; i++) {
			extract.process(context, new SteConfig());
			System.out.println(context.getVariable("tovar"));
			Assert.assertEquals(results.get(i), context.getVariable("tovar"));
			assertTrue(extract.hasNext());
		}
		
		extract.process(context, new SteConfig());
		Assert.assertEquals(results.get(3), context.getVariable("tovar"));
		assertFalse(extract.hasNext());


	}

}
