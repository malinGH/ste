package com.souche.ste.props;

import java.io.OutputStream;


public class Main {
//	private static OutputStream outputStream;

	public static void main(String[] args) {
		
		String propFilePath = "/usr/local/ste/props/resources/props.config";
		PropsAttrs props = new PropsAttrs();
		props.setProps(propFilePath);
		
		System.out.println("runned");
		props.isToRun();
		props.run();
//		props.persist(outputStream);
		
		System.out.println("runned");
	}

}
