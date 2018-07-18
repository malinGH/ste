package com.souche.ste.filter;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * css抽取器，根据css抽取出对应的文本
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.CssExtractFilter
    from:detail_body
    to:head_info
    css:.path
    假设detail_body中的值为  &lt;body&gt;&lt;div&gt;abc&lt;/div&gt;&lt;div class="path"&gt;hello&lt;/div&gt;&lt;/body&gt;
    执行上面的filter后，header_info中的值为&lt;div class="path"&gt;hello&lt;/div&gt;
 * </pre>
 * @author hongwm
 *
 */
public class CssExtractFilter extends SteFilter {

	private Logger logger = LoggerFactory.getLogger(CssExtractFilter.class);
	private String from;
	private String to;
	private String css;

	@Override
	public void process(Context context, SteConfig config) {
		String content = (String) context.getVariable(from);
		Page page = new Page(content, "utf-8");

		Lexer lexer = new Lexer(page);
		Parser parser = new Parser(lexer);

		NodeFilter cssFilter = new CssSelectorNodeFilter(css);
		try {
			NodeList cssNodes = parser.parse(cssFilter);
			if(cssNodes != null) {
				String toValue = cssNodes.toHtml();
				context.setVariable(to, toValue);
			}
		} catch (ParserException e) {
			logger.warn("", e);
		}

	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if("from".equals(key)) {
			from = value;
		} else if("to".equals(key)) {
			to = value;
		} else if("css".equals(key)) {
			css = value;
		}
	}

}
