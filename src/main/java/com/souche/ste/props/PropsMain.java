package com.souche.ste.props;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.souche.ste.util.StringUtil;

public class PropsMain {
	private static String filename = "src/main/resources/props.config";
	
	public static void main(String[] args) throws IOException {
		if(args.length >= 1) {
			filename = args[0];
		}
		
		String propConfig = filename;
		
		InputStreamReader reader = new InputStreamReader(new FileInputStream(propConfig));
		List<PropsAttrs> propsList = new ArrayList<PropsAttrs>();
		String line = StringUtil.readLine(reader);
		while (line != null) {
			if (line.length() > 0) {
				System.out.println(line);
				PropsAttrs props = PropsAttrs.parseLine(line);
				System.out.println(props);
				if (props != null) {
					propsList.add(props);
				}
			}
			line = StringUtil.readLine(reader);
		}

		for (PropsAttrs prop : propsList) {
			if (prop.isToRun()) {
				prop.run();
			}
		}

		OutputStream outputStream = new FileOutputStream(filename);
		for (PropsAttrs prop : propsList) {
			prop.persist(outputStream);
		}
		outputStream.flush();
		outputStream.close();
	}

}
