package com.souche.ste.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.StringUtil;

/**
 * 图片链接抽取器，从一段文本中提取图片的地址列表
 * 
 * @author hongwm
 *
 */
public class ImgExtractFilter extends SteFilter {
	private String from;
	private String url;
	private String tag = null;


	@Override
	public void process(Context context, SteConfig config) {
		String content = (String) context.getVariable(from);

		if(content == null) {
			return;
		}

		Page page = new Page(content, "utf-8");

		Lexer lexer = new Lexer(page);
		Parser parser = new Parser(lexer);
		NodeFilter filter = new TagNameFilter("IMG");
		try {
			NodeList nodeList = parser.parse(filter);
			NodeIterator itr = nodeList.elements();
			StringBuffer sb = new StringBuffer();

			while(itr.hasMoreNodes()) {
				Node node = itr.nextNode();
				if(!(node instanceof ImageTag)) {
					continue;
				}

				ImageTag imgTag = (ImageTag) node;
				String imgUrl = imgTag.getImageURL();
				if(tag != null) {
					imgUrl = imgTag.getAttribute(tag);
				}
				if(StringUtil.isEmpty(imgUrl)) {
					continue;
				}
				//	sb.append(" ");
				sb.append(imgUrl);		
				sb.append(" ");

			}
			context.setVariable(url, sb.toString());
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
		} else if("tag".equals(key)) {
			tag = value;
		}

	}

}
