package com.souche.ste.process.builder;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.souche.ste.filter.ExtractFilter;
import com.souche.ste.filter.SteFilter;

public class ProcessBuilderTest {

	@Test
	public void testBuildFilters() {
		AbstractProcessBuilder builder = new ConfigProcessBuilder();
		
		builder.addConfLine("filter.class:com.souche.ste.filter.ExtractFilter");
		builder.addConfLine("filter.parameters.from:body");
		builder.addConfLine("filter.parameters.to:to_field");
		builder.addConfLine("filter.parameters.begin:begin");
		builder.addConfLine("filter.parameters.end:end");
		
		builder.addConfLine("filter.class:com.souche.ste.filter.ExtractFilter");
		builder.addConfLine("filter.parameters.from:to_field");
		builder.addConfLine("filter.parameters.to:to_field");
		builder.addConfLine("filter.parameters.begin:begin1");
		builder.addConfLine("filter.parameters.end:end1");
		
		
		builder.buildConf();
		List<SteFilter> filters =  builder.getFilters();
		Assert.assertEquals(filters.size(), 2);
		
		ExtractFilter filter = (ExtractFilter) filters.get(0);
		Assert.assertEquals(filter.getBegin(), "begin");
		Assert.assertEquals(filter.getEnd(), "end");
		Assert.assertEquals(filter.getFrom(), "body");
		Assert.assertEquals(filter.getTo(), "to_field");
		
		ExtractFilter filter1 = (ExtractFilter) filters.get(1);
		Assert.assertEquals(filter1.getFrom(), "to_field");
		Assert.assertEquals(filter1.getTo(), "to_field");
		Assert.assertEquals(filter1.getBegin(), "begin1");
		Assert.assertEquals(filter1.getEnd(), "end1");
		
	}

}
