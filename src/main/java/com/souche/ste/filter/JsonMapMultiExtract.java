package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.Entry;

/**
 * 循环抽取处理器，从json map中循环抽取相应的item到key/value变量中
 * @author hongwm
 */
public class JsonMapMultiExtract extends SteFilter implements LoopFilter {
	private String from;
	private String key = "key";
	private String value = "value";
	/**
	 * 抽取结果列表
	 */
	protected List<Entry<String, String>> extractResults = 
			new ArrayList<Entry<String, String>>();
	
	private int idx = 0;
	
	@Override
	public void process(Context context, SteConfig config) {
		if(extractResults.isEmpty()) {
			String fromVariable = (String) context.getVariable(from);
			
			JSONObject jsonObj = new JSONObject(fromVariable);

			@SuppressWarnings("rawtypes")
			Iterator itr = jsonObj.keys();
			while(itr.hasNext()) {
				String key = (String) itr.next();
				extractResults.add(new Entry<String, String>(key, (String) jsonObj.get(key)));
			}
		}

		if(idx >= extractResults.size()) {
			return;
		}
		
		context.setVariable(key, extractResults.get(idx).getKey());
		context.setVariable(value, extractResults.get(idx).getValue());
		idx ++;
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("from".equals(key)) {
			from = value;
		} else if("key".equals(key)) {
			this.key = value;
		} else if("value".equals(key)) {
			this.value = value;
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
