package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * extract抽取，从from变量中提取由begin到end的信息到to变量中
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.ExtractFilter
    from:item_info1
    to:update_time
    begin:&lt;/span&gt;&lt;/div&gt;&lt;div&gt;
    end:&lt;/div
 * 
 * </pre>
 * @author hongwm
 * @since 2013年11月7日
 */
public class ExtractFilter extends SteFilter {
	protected String from;
	protected String to;
	protected String begin;
	protected String end;
	protected boolean ensureMatch = false;
	
	@Override
	public void process(Context context, SteConfig config) {
		String fromVariable = (String) context.getVariable(from);
		
		if(fromVariable != null) {
			int beginIdx = -1;
			int endIdx = fromVariable.length();
			
			if(begin != null) {
				beginIdx = fromVariable.indexOf(begin);
				if(beginIdx != -1) {
					beginIdx += begin.length();
				} else {
					if(ensureMatch) {
						context.setVariable(to, null);
						return;
					}
					beginIdx = 0;
				}
			} else {
				beginIdx = 0;
			}
			
			if(end != null) {
				endIdx = fromVariable.indexOf(end, beginIdx);
				if(endIdx == -1) {
					if(ensureMatch) {
						context.setVariable(to, null);
						return;
					}
					endIdx = fromVariable.length();
				}
			}
			
			String toVariable = fromVariable.substring(beginIdx, endIdx);
			context.setVariable(to, toVariable);
		}
	}
	
	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("from".equals(key)) {
			from = value;
		} else if("to".equals(key)) {
			to = value;
		} else if("begin".equals(key)) {
			begin = value;
		} else if("end".equals(key)) {
			end = value;
		} else if("ensureMatch".equals(key)) {
			ensureMatch = value.equals("true") ? true : false;
		}
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getBegin() {
		return begin;
	}

	public String getEnd() {
		return end;
	}

}
