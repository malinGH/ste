package com.souche.ste.filter;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class UrlRequestFilterTest {

	@Test
	public void testProcess() {
		UrlRequestFilter filter = new UrlRequestFilter();
		String url = "https://www.guazi.com/";
		filter.addParam("url", url);
		//filter.addParam("headers.Cookie", "isDingDing=false; tracknick=13735854173; _security_token_inc=91530087840940905; _gitlab_session=45c64b14d5a780618018a79ba32b38a2");
		filter.addParam("headers.Cookie", "antipas=949042cx24638358i631i4");
		filter.addParam("headers.User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
		filter.addParam("to", "tovar");
		Context context = new Context();
		
		filter.process(context, new SteConfig());
		filter.process(context, new SteConfig());
		
		System.out.println(context.getVariable("tovar"));
	}

}
