package com.souche.ste.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;

/**
 * url链接提取器，从一段文本中提取相应的链接及锚文本
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.HrefExtractFilter
    from:item_info
    url:model_url
    anchor:model
    如item_info中的值为&lt;a href="http://www.sina.com.cn"&gt;新浪网&lt;/a&gt;，执行上面的语句后，url为http://www.sina.com.cn，anchor值为新浪网
    参数说明：
       from: 来源变量
       url: 文本中的链接信息
       anchor: 链接中的锚文本
 * </pre>
 * @author hongwm
 */
public class HrefExtractFilter extends SteFilter {
	private String from;
	private String url;
	private String anchor;
	private int id;
	private boolean revert = false;
	
	@Override
	public void process(Context context, SteConfig config) {
		String content = (String) context.getVariable(from);
		if(content == null) {
			return;
		}
		Page page = new Page(content, "utf-8");
		int num = id;

		Lexer lexer = new Lexer(page);
		Parser parser = new Parser(lexer);
		NodeFilter filter = new TagNameFilter("A");
		try {
			NodeList nodeList = parser.parse(filter);
			NodeIterator itr = nodeList.elements();
			if(revert) {
				num = nodeList.size() - 1 - id;
			}
			
			int i = 0;
			String urlVar = null;
			String anchorVar = null;
			
			while(itr.hasMoreNodes()) {
				Node node = itr.nextNode();
				if(!(node instanceof LinkTag)) {
					continue;
				}
				LinkTag hrefTag = (LinkTag) node;
				if(i++ == num) {
					urlVar = hrefTag.getLink();
					anchorVar = hrefTag.getLinkText();
					break;
				}
			}
			
			if(urlVar != null) {
				context.setVariable(url, urlVar);
				context.setVariable(anchor, anchorVar);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void addParam(String key,String value){
		super.addParam(key, value);
		if("from".equals(key)){
			from = value;
		} else if("url".equals(key)){
			url = value;
		} else if("anchor".equals(key)){
			anchor = value;
		} else if("id".equals(key)) {
			id = CommonUtil.getIntValue(value);
		} else if("revert".equals(key)) {
			if("true".equals(value)) {
				revert = true;
			}
		}
			
	}

}
