package com.souche.ste.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class MultiGetFilterFromFileTest {

	@Test
	public void testProcess() {
		MultiGetFilterFromFile filter = new MultiGetFilterFromFile();
		filter.addParam("to", "key1");
		filter.addParam("file", "test/line_file");
		Context context = new Context();
		int n = 0;
		while(filter.hasNext()) {
			filter.process(context, new SteConfig());
			System.out.println(context.getVariable("key1"));
			n ++;
		}
		assertEquals(n, 6);
	}

}
