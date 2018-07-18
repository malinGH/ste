package com.souche.ste;

import java.io.IOException;

import com.souche.ste.config.SteConfig;
import com.souche.ste.process.SteDocumentProcess;

/**
 * ste模板抽取主程序
 * @author hongwm
 */
public class SteMain {

	public static void main(String[] args) throws IOException {
		if(args.length < 1) {
			printHelp();
			return;
		}
		
		SteConfig steConfig = SteConfig.loadConfigFromFile(args[0]);
		SteDocumentProcess process = steConfig.getDocumentProcess();
		process.process(steConfig);
	}

	public static void printHelp() {
		System.out.println("SteMain propfile");
	}
}
