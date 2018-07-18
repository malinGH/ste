package com.souche.ste.config;

import java.io.IOException;

import org.junit.Test;

import com.souche.ste.util.CommonUtil;

public class SteConfigTest {

	@Test
	public void testLoadConfigFromFile() throws IOException {
		String propsFile = CommonUtil.getAbsoluteFilePath("testBitauto.props");
		SteConfig config = SteConfig.loadConfigFromFile(propsFile);
		System.out.println(config.getPoliteness());
		System.out.println(config.getInterval());
		System.out.println(config.getSchemas());
		
	}

}
