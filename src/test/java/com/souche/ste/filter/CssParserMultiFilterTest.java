package com.souche.ste.filter;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.StringUtil;

public class CssParserMultiFilterTest {

	@Test
	public void testProcess1() {
		CssParserMultiFilter multiFilter = new CssParserMultiFilter();
		multiFilter.addParam("from", "fromVariable");
		multiFilter.addParam("to", "to");
		multiFilter.addParam("css", ".ctit");
		
		String str = readFromFile("test/css_test_file");
		
		Context context = new Context();
		context.setVariable("fromVariable", str);
		multiFilter.process(context, new SteConfig());
		System.out.println(context.getVariable("to"));
		while(multiFilter.hasNext()) {
			multiFilter.process(context, new SteConfig());
			System.out.println(context.getVariable("to"));
			
		}
	}

	@Test
	public void testProcess2() {
		CssParserMultiFilter multiFilter = new CssParserMultiFilter();
		multiFilter.addParam("from", "fromVariable");
		multiFilter.addParam("to", "to");
		multiFilter.addParam("css", ".listul");
		
		String str = readFromFile("test/css_test_file");
		
		str = str.replaceAll(" bgf5", "");
		Context context = new Context();
		context.setVariable("fromVariable", str);
		multiFilter.process(context, new SteConfig());
		System.out.println(context.getVariable("to"));
		while(multiFilter.hasNext()) {
			multiFilter.process(context, new SteConfig());
			System.out.println(context.getVariable("to"));
			
		}
	}

	
	protected String readFromFile(String file) {
		String absoluteFile = CommonUtil.getAbsoluteFilePath(file);
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(absoluteFile), "utf-8");
			String line = StringUtil.readLine(reader);
			while(line != null) {
				sb.append(line);
//				sb.append('\n');
				line = StringUtil.readLine(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
