package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * CssExtractFilter的循环实现，循环获取符合css条件的信息，放到to变量中
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.CssParserMultiFilter
    from:item_list
    to:item_info
    css:li
    循环获取item_list中li标签的值，放到item_info变量中
 * </pre>
 * @author hongwm
 */
public class CssParserMultiFilter extends SteFilter implements LoopFilter {
	private Logger logger = LoggerFactory.getLogger(CssParserMultiFilter.class);
	private String from;
	private String to;
	private String css;
	
	private int idx = 0;
	
	private List<String> extractResults = 
			new ArrayList<String>();
	
	public boolean hasNext() {
		return extractResults.size() > idx;
	}

	@Override
	public void process(Context context, SteConfig config) {
		if(CollectionUtils.isEmpty(extractResults)) {
			String content = (String) context.getVariable(from);
			Page page = new Page(content, "utf-8");
			
			Lexer lexer = new Lexer(page);
			Parser parser = new Parser(lexer);
			
			NodeFilter cssFilter = new CssSelectorNodeFilter(css);
			try {
				NodeList cssNodes = parser.parse(cssFilter);
				if(cssNodes != null) {
					NodeIterator itr = cssNodes.elements();
					while(itr.hasMoreNodes()) {
						Node node = itr.nextNode();
						extractResults.add(node.toHtml());
					}
				}
			} catch (ParserException e) {
				logger.warn("", e);
			}
		}
		
		if(extractResults.size() <= idx) {
			return;
		}
		
		String toValue = extractResults.get(idx);
		context.setVariable(to, toValue);
		idx ++;
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

	public void reset() {
		idx = 0;
		extractResults.clear();
				
	}
}
