package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.springframework.util.CollectionUtils;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;

public class UrlExtractMultiFilter extends SteFilter implements LoopFilter {

	private String from;
	private String url;
	private String anchor;
	private int id = 0;

	private List<LinkTag> extractResults = new ArrayList<LinkTag>();

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
			Page page = new Page(content, "utf-8");

			Lexer lexer = new Lexer(page);
			Parser parser = new Parser(lexer);
			NodeFilter filter = new TagNameFilter("A");
			try {
				NodeList nodeList = parser.parse(filter);
				NodeIterator itr = nodeList.elements();
				while (itr.hasMoreNodes()) {
					Node node = itr.nextNode();
					if (!(node instanceof LinkTag)) {
						continue;
					}
					extractResults.add((LinkTag) node);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (id >= extractResults.size()) {
			return;
		}
		LinkTag node = extractResults.get(id);
		String urlVar = null;
		String anchorVar = null;
		urlVar = node.getLink();
		anchorVar = node.getLinkText();
		context.setVariable(url, urlVar);
		context.setVariable(anchor, anchorVar);
		id++;
	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if ("from".equals(key)) {
			from = value;
		} else if ("url".equals(key)) {
			url = value;
		} else if ("anchor".equals(key)) {
			anchor = value;
		} else if ("id".equals(key)) {
			id = CommonUtil.getIntValue(value);
		}

	}

	public void reset() {
		id = 0;
		extractResults.clear();
	}

}
