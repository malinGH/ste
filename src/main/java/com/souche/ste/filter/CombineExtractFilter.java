package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.ProcessUtil;
import com.souche.ste.util.StringUtil;

/**
 * 循环抽取，然后组合在一起
 */
public class CombineExtractFilter extends ExtractFilter {
	
	@Override
	public void process(Context context, SteConfig config) {
		List<String> extractResults = 
				new ArrayList<String>();
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
		
		String toVariable = ProcessUtil.join(extractResults, " ");

		context.setVariable(to, toVariable);
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
	}
}
