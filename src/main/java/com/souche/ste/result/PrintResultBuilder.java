package com.souche.ste.result;

import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.Entry;

public class PrintResultBuilder extends ResultBuilder {

	public void write(Context context, SteConfig steConfig) {
		List<Entry<String, Object>> results = getExtractResults(context, steConfig);
		
		StringBuffer sb = new StringBuffer();
		for(Entry<String, Object> e : results) {
			if(sb.length() > 0) {
				sb.append("\t");
			}
			sb.append(e.getKey() + ": " + e.getValue());
		}
		System.out.println(sb.toString());
	}

	@Override
	public void closeWriter() {
		
	}
}
