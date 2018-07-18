package com.souche.ste.process;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig.ConfigLine;
import com.souche.ste.context.Params;

public abstract class AbstractBuilder extends Params{
	/**
	 * 配置行列表
	 */
	private List<ConfigLine> confLines = 
			new ArrayList<ConfigLine>();
	
	/**
	 * 加入配置行
	 * @param line
	 * @param lineNum 
	 */
	public void addConfLine(ConfigLine configLine) {
		confLines.add(configLine);
	}
	
	public void addConfLine(String line) {
		confLines.add(new ConfigLine(line, 0));
	}
	
	public List<ConfigLine> getConfLines() {
		return confLines;
	}
	
	public abstract void buildConf();
}
