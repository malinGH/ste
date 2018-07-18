package com.souche.ste.context;

public class Params {
	private Context context =
			new Context();
	
	public void addParam(String key, String value) {
		context.setVariable(key, value);
	}
	
	public String getParam(String key) {
		return (String) context.getVariable(key);
	}
}
