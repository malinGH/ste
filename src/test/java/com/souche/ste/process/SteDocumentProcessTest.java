package com.souche.ste.process;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.souche.ste.config.SteConfig;
import com.souche.ste.util.CommonUtil;

public class SteDocumentProcessTest {

	@Test
	public void testProcess() throws IOException {
		SteConfig config = SteConfig.loadConfigFromFile(CommonUtil.getAbsoluteFilePath("XcarAll.props"));
		SteDocumentProcess process = config.getDocumentProcess();
		
		process.process(config);
		
		assertTrue(true);
	}

}
