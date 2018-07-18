package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

public class PatternMultiExtractFilter extends SteFilter implements LoopFilter {

	private String from;
	private String to;
	private String pattern;
	private int id = 0;

	private List<String> extractResults = new ArrayList<String>();

	public boolean hasNext() {
		return extractResults.size() > id;
	}

	@Override
	public void process(Context context, SteConfig config) {
		if (CollectionUtils.isEmpty(extractResults)) {
			String content = (String) context.getVariable(from);
			if (content == null) {
				return;
			}
			
			Pattern p = Pattern.compile(pattern);
			Matcher matcher = p.matcher(content);
			
			while(matcher.find()) {
				extractResults.add(matcher.group());
			}
		}

		if (id >= extractResults.size()) {
			return;
		}
		
		context.setVariable(to, extractResults.get(id));
		id++;
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if ("from".equals(key)) {
			from = value;
		} else if ("to".equals(key)) {
			to = value;
		} else if ("pattern".equals(key)) {
			pattern = value;
		}

	}

	public void reset() {
		id = 0;
		extractResults.clear();
	}

}
