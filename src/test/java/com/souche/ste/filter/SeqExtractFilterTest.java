package com.souche.ste.filter;

import static org.junit.Assert.*;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class SeqExtractFilterTest {

	@Test
	public void testProcess() {
		SeqExtractFilter filter = new SeqExtractFilter();
		filter.addParam("from", "fromvar");
		filter.addParam("abegin1.begin", "<span>");
        filter.addParam("abegin1.end", "<");
        filter.addParam("aend2.begin", "<span>");
        filter.addParam("aend2.end", "<");
        filter.addParam("to3.begin", "<span>");
        filter.addParam("to3.end", "<");
		
		Context context = new Context();
		context.setVariable("fromvar", "<i></i><span>7.9万公里</span>行驶里程</li><li><i></i><span>2014-08</span>首次上牌</li><li><i></i><span>自动／3L</span>挡位／排量</li><li><i></i><span>北京</span>所在地</li><li><i></i><span>欧V</span>查询准迁地</a></li>");
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("abegin1"));
		System.out.println(context.getVariable("aend2"));
		System.out.println(context.getVariable("to3"));
		assertEquals("7.9万公里", context.getVariable("abegin1"));
		assertEquals("2014-08", context.getVariable("aend2"));
		assertEquals("自动／3L", context.getVariable("to3"));

//		context.setVariable("fromvar", "abdexydef");
//		filter.process(context, new SteConfig());
//		System.out.println(context.getVariable("tovar"));
//		assertEquals("abdexy", context.getVariable("tovar"));
//
//		context.setVariable("fromvar", "abdexyde");
//		filter.process(context, new SteConfig());
//		System.out.println(context.getVariable("tovar"));
//		assertEquals("abdexyde", context.getVariable("tovar"));

	
	}

}
