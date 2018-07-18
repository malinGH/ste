package com.souche.ste.filter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.StringUtil;

/**
 * 从文件中循环获取信息
 * @author hongwm
 * @since 2014年4月17日
 */
public class MultiGetFilterFromFile extends SteFilter implements LoopFilter {
	private String to;
	private String file;
	
	private String preLine = null;
	InputStreamReader reader = null;

	@Override
	public void process(Context context, SteConfig config) {
		
		if(preLine == null) {
			preLine = getLine();
		}

		if(preLine == null) {
			return;
		}
		context.setVariable(to, preLine);
		preLine = null;
	}

	protected String getLine() {
		try {
			if(reader == null) {
				InputStream input;
				input = new FileInputStream(CommonUtil.getAbsoluteFilePath(file));
				reader = new InputStreamReader(input, "utf-8");
			}
			return StringUtil.readLine(reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("to".equals(key)) {
			to = value;
		} else if("file".equals(key)) {
			file = value;
		}
	}
	public boolean hasNext() {
		if(preLine == null) {
			preLine = getLine();
		}
		return preLine == null ? false : true;
	}

	public void reset() {
		preLine = null;
		reader = null;
	}
}
