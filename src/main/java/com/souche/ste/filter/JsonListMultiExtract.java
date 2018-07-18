package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 循环抽取处理器，从json array中循环提取每一个item的项到to变量中
 * @author hongwm
 */
public class JsonListMultiExtract extends SteFilter implements LoopFilter {
	private String from;
	private String to;
	/**
	 * 抽取结果列表
	 */
	protected List<String> extractResults = 
			new ArrayList<String>();
	
	private int idx = 0;
	
	@Override
	public void process(Context context, SteConfig config) {
		if(extractResults.isEmpty()) {
			String fromVariable = (String) context.getVariable(from);
			
			JSONArray jsonArray = new JSONArray(fromVariable);
			for(int i=0; i<jsonArray.length(); i++) {
				Object obj = jsonArray.get(i);
				extractResults.add(obj.toString());
			}
		}

		if(idx >= extractResults.size()) {
			return;
		}
		
		String toVariable = extractResults.get(idx);
		context.setVariable(to, toVariable);
		idx ++;
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("from".equals(key)) {
			from = value;
		} else if("to".equals(key)) {
			to = value;
		}
	}


	public boolean hasNext() {
		return extractResults.size() > idx;
	}

	public void reset() {
		idx = 0;
		extractResults.clear();
	}
}
