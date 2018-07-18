package com.souche.ste.config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.souche.ste.context.Params;
import com.souche.ste.process.AbstractBuilder;
import com.souche.ste.process.SteDocumentProcess;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.StringUtil;

/**
 * 定义ste配置项
 * @author hongwm
 * @since 2013年11月7日
 */
public class SteConfig extends Params {
	/**
	 * 抓取字段定义
	 */
	private String schemas;
	
	/**
	 * 定义的抓取间隔
	 */
	private int interval = 3600;
	/**
	 * 网站politeness定义
	 */
	private int politeness = 2;
	
	/**
	 * 模板名
	 */
	private String propname;
	/**
	 * 模板类型
	 */
	private String proptype;
	private boolean useProxy;
	/**
	 * 文档处理
	 */
	private SteDocumentProcess documentProcess;
	
	public List<String> getSchemaList() {
		List<String> list = new ArrayList<String>();
		String[] strs = schemas.split(",");
		for(String str : strs) {
			list.add(StringUtil.trim(str));
		}
		return list;
	}
	
	public String getSchemas() {
		return schemas;
	}
	public void setSchemas(String schemas) {
		this.schemas = schemas;
	}
	
	public int getInterval() {
		return interval;
	}
	public void setInterval(int interval) {
		this.interval = interval;
	}
	
	public int getPoliteness() {
		return politeness;
	}
	public void setPoliteness(int politeness) {
		this.politeness = politeness;
	}
	
	public String getPropname() {
		return propname;
	}
	public void setPropname(String propname) {
		this.propname = propname;
	}
	
	public String getProptype() {
		return proptype;
	}
	public void setProptype(String proptype) {
		this.proptype = proptype;
	}
	
	public SteDocumentProcess getDocumentProcess() {
		return documentProcess;
	}
	public void setDocumentProcess(SteDocumentProcess documentProcess) {
		this.documentProcess = documentProcess;
	}
	public boolean isUseProxy() {
		return useProxy;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public void addParam(String key, String value) {
		if("politeness".equals(key)) {
			politeness = CommonUtil.getIntValue(value);
		} else if("interval".equals(key)) {
			interval = CommonUtil.getIntValue(value);
		} else if("schema".equals(key)) {
			schemas = value;
		} else if("proptype".equals(key)) {
			proptype = value;
		} else if("proxy".equals(key)) {
			if("true".equals(value)) {
				useProxy = true;
			}
		}
	}

	public static String getPropName(String propFile) {
		int startpos = propFile.lastIndexOf("/");
		int endpos = propFile.lastIndexOf(".");
		
		if(startpos == -1) {
			startpos = propFile.lastIndexOf('\\');
			if(startpos == -1) {
				startpos = 0;
			}
		}
		
		if(endpos == -1) {
			endpos = propFile.length();
		}
		
		return propFile.substring(startpos, endpos);
	}
	
	/**
	 * 根据文件名，解释ste的模板配置文件
	 * @param propFile
	 * @return
	 * @throws IOException
	 */
	public static SteConfig loadConfigFromFile(String propFile) throws IOException {
		propFile = CommonUtil.getAbsoluteFilePath(propFile);
		
		SteConfig steConfig = new SteConfig();
		SteDocumentProcess documentProcess = new SteDocumentProcess();
		steConfig.setDocumentProcess(documentProcess);
		steConfig.setPropname(getPropName(propFile));
		
		InputStream input = new FileInputStream(propFile);
		InputStreamReader reader = new InputStreamReader(input, "utf-8");

		BufferedReader r = new BufferedReader(reader);
		String line = null;
		AbstractBuilder builder = null;
		
		int lineNum = 0;
		while((line = r.readLine() )!= null) {
			lineNum ++;
			// 读取文件的一行信息
			if(line.isEmpty() || line.startsWith("#")) {
				// 如果是注释信息，忽略读取
				continue;
			}
			
			if(builder == null) {
				// 还没出现Builder，读取通用的配置信息
				String[] kv = line.split(":", 2);
				if(kv.length != 2) {
					continue;
				}
				if(!kv[0].endsWith("class")) {
					steConfig.addParam(kv[0], kv[1]);
				} else {
					try {
						builder = (AbstractBuilder) Class.forName(
								StringUtil.trim(kv[1])).newInstance();
						documentProcess.addBuilder(builder);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(builder != null) {
				String[] kv = line.split(":", 2);
				if(kv.length == 2 
						&& (kv[0].startsWith("source") 
								|| kv[0].startsWith("process") 
								|| kv[0].startsWith("result"))
						&& kv[0].endsWith("class")) {
					// 如果是新的构建器
					try {
						builder = (AbstractBuilder) Class.forName(
								StringUtil.trim(kv[1])).newInstance();
						documentProcess.addBuilder(builder);
					} catch (Exception e) {
						builder = null;
						e.printStackTrace();
					}
				} else {
					// builder里面的一个配置
					builder.addConfLine(new ConfigLine(line, lineNum));
				}
			}
		}
		
		input.close();
		return steConfig;
	}
	
	/**
	 * 配置文件中的行信息
	 */
	public static class ConfigLine {
		/**
		 * 配置文件中一行的内容
		 */
		private String line;
		/**
		 * 第几行配置
		 */
		private int lineNum;
		
		public ConfigLine(String line, int lineNum) {
			this.line = line;
			this.lineNum = lineNum;
		}
		
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}
		public int getLineNum() {
			return lineNum;
		}
		public void setLineNum(int lineNum) {
			this.lineNum = lineNum;
		}
	}
}
