package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.StringUtil;

/**
 * 循环抽取处理器
 * @author hongwm
 * @since 2013年11月7日
 */
public class MultiExtract extends ExtractFilter implements LoopFilter {
	/**
	 * 抽取结果列表
	 */
	protected List<String> extractResults = 
			new ArrayList<String>();
	
	private int idx = 0;
	
	private int initIdx = 0;
	
	@Override
	public void process(Context context, SteConfig config) {

		if(extractResults.isEmpty()) {
			String fromVariable = (String) context.getVariable(from);
			if(fromVariable == null) {
				return;
			}
			int start = 0;
			while(start < fromVariable.length()) {
				int beginIdx = start, endIdx = fromVariable.length();
				if(StringUtil.isNotEmpty(begin)) {
					beginIdx = fromVariable.indexOf(begin, start);
					if(beginIdx >= 0) {
						beginIdx += begin.length();
					}
				}
				if(beginIdx == -1) {
					break;
				}
				
				boolean legalEnd = true;
				if(StringUtil.isNotEmpty(end)) {
					endIdx = fromVariable.indexOf(end, beginIdx + 1);
				} else {
					endIdx = fromVariable.indexOf(begin, beginIdx + 1);
					legalEnd = false;
				}
				
				if(endIdx == -1) {
					break;
				}

				if(beginIdx == start && endIdx == fromVariable.length()) {
					break;
				}
				
				String tostr = fromVariable.substring(beginIdx, endIdx);
				extractResults.add(tostr);
				
				if(legalEnd) {
					start = endIdx + 1;
				} else {
					start = endIdx;
				}
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
		if("ignoreIdx".equals(key)) {
			initIdx = Integer.valueOf(value);
		}
	}
	public boolean hasNext() {
		return extractResults.size() > idx;
	}

	public void reset() {
		idx = initIdx;
		extractResults.clear();
	}
}
