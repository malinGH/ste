package com.souche.ste.process.builder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.souche.ste.config.SteConfig;
import com.souche.ste.config.SteConfig.ConfigLine;
import com.souche.ste.context.Context;
import com.souche.ste.filter.SteFilter;
import com.souche.ste.process.AbstractBuilder;
import com.souche.ste.util.StringUtil;

/**
 * 模板抽取过程处理器
 * @author hongwm
 * @since 2013年11月7日
 */
public abstract class AbstractProcessBuilder extends AbstractBuilder {
	private final static Logger logger = LoggerFactory.getLogger(AbstractProcessBuilder.class);
	
	private List<String> attrs = new ArrayList<String>();
	
	/**
	 * 处理过滤器
	 */
	private List<SteFilter> filters = 
			new ArrayList<SteFilter>();
	
	public abstract void process(Context context, SteConfig config);
	
	/**
	 * 根据读取的配置信息，创建内部的处理器列表
	 */
	public void buildConf() {
		filters.clear();

		SteFilter lastFilter = null;
		List<ConfigLine> confLines = super.getConfLines();
		for(ConfigLine configLine : confLines) {
			String line = configLine.getLine();
			if(line.startsWith("process.parameters")) {
				line = line.replace("process.parameters.", "");
				String[] kv = line.split(":", 2);
				if(kv.length == 2) {
					if(kv[0].equals("attrs")) {
						String[] array = kv[1].split(" ");
						for(String s : array) {
							attrs.add(s);
						}
					}
				}
			} else {

				if(StringUtil.isEmpty(line)) {
					continue;
				}
				String[] kv = line.split(":", 2);
				if(kv.length != 2) {
					continue;
				}

				if(kv[0].equals("filter.class")) {
					// 如果是一个新的filter，新构建filter，同时把filter加入列表中
					try {
						lastFilter = (SteFilter) Class.forName(kv[1]).newInstance();
						lastFilter.setLineNum(configLine.getLineNum());
						filters.add(lastFilter);
					} catch (Exception e) {
						logger.warn("", e);
						lastFilter = null;
					}
				} else {
					if(lastFilter != null) {
						String key = kv[0].replace("filter.parameters.", "");
						String value = kv[1];
						// 去除头尾的空格
						key = StringUtil.trim(key);
						lastFilter.addParam(key, value);	// 参数信息加入到filter中
					}
				}
			}
		}
	}
	
	/**
	 * 判断该builder是否还需要后续循环调用
	 */
	public abstract boolean nextLoop();
	
	public abstract void reset(Context context);
	
	/**
	 * 判断该builder内部是否有忽略结果的行为
	 */
	public abstract boolean ignore();
	public List<String> getAttrs() {
		return attrs;
	}
	
	public List<SteFilter> getFilters() {
		return filters;
	}
}
