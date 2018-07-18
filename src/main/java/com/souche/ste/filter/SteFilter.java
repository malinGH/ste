package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.context.Params;

public abstract class SteFilter extends Params{
	private int lineNum;
	public abstract void process(Context context, SteConfig config);
	
	public void reset() {
	}
	
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	public void print(String str) {
		System.out.println(String.format("line %d: %s", lineNum, str));
	}
	
	public int getLineNum() {
		return lineNum;
	}
}
