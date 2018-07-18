package com.souche.ste.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 根据pattern，从from变量中提取相应的信息到to变量中
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.PatternExtractFilter
    from:phone_info
    to:mobile
    pattern:1\\d{10}
    设置phone_info中的值为"你好，我的电话是13666608929"，执行上面的filter以后，mobile中的值为13666608929
    
    参数说明：
        from: 来源变量
        to: 结果变量
        pattern: 要匹配的pattern
 * 
 * </pre>
 * @author hongwm
 *
 */
public class PatternExtractFilter extends SteFilter {

	private String from;
	private String to;
	private String pattern;

	@Override
	public void process(Context context, SteConfig config) {
		String content = (String) context.getVariable(from);
		if (content == null) {
			return;
		}

		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(content);

		if(matcher.find()) {
			String result = matcher.group();
			context.setVariable(to, result);
		} else {
			context.setVariable(to, null);
		}

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

}
