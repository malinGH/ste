package com.souche.ste.result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.config.SteConfig.ConfigLine;
import com.souche.ste.context.Context;
import com.souche.ste.process.AbstractBuilder;
import com.souche.ste.util.Entry;

/**
 * 写入处理结果器
 * @author hongwm
 * @since 2013年11月7日
 */
public abstract class ResultBuilder extends AbstractBuilder{
	/**
	 * 结果写入的方法
	 * @param context
	 * @param steConfig
	 * @throws IOException
	 */
	public abstract void write(Context context, SteConfig steConfig) throws IOException;
	
	/**
	 * 关闭writer
	 * @throws IOException
	 */
	public abstract void closeWriter() throws IOException;
	
	/**
	 * 获取要提取信息的结果
	 * @param context
	 * @param steConfig
	 * @return 返回提取信息的列表
	 */
	public List<Entry<String, Object>> getExtractResults(Context context, SteConfig steConfig) {
		List<Entry<String, Object>> results = 
				new ArrayList<Entry<String, Object>>();
		List<String> schemas = steConfig.getSchemaList();
		for(String schema : schemas) {
			Object value = context.getVariable(schema);
			if(value == null) {
				value = "";
			}
			if(value instanceof String) {
				value = ((String)value).replaceAll("\n|\r|\t", " ");
			}
			Entry<String, Object> e = new Entry<String, Object>(schema, value);
			results.add(e);
		}
	
		return results;
	}
	
	@Override
	public void buildConf() {
		List<ConfigLine> confLines = getConfLines();
		for(ConfigLine confLine : confLines) {
			String conf = confLine.getLine();
			String[] kv = conf.split(":", 2);
			if(kv.length != 2) {
				continue;
			}
			
			String key = kv[0];
			key = key.replaceAll(".*parameters.", "");
			addParam(key, kv[1]);
		}
	}
}
