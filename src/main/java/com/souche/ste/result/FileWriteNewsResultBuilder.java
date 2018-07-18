package com.souche.ste.result;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.Entry;

public class FileWriteNewsResultBuilder extends ResultBuilder {
	private OutputStream outputStream;
	
	private String dataDir = "/usr/local/ste/datafiles/";
	private int maxNumPerFile = 100000000;
	
	private int fileNum = 0;
	
	private int count = 0;
	
	private String writeFile;
	
	public void write(Context context, SteConfig steConfig) throws IOException {
		if(outputStream == null) {
			String dir = dataDir + steConfig.getProptype() + "/";
			new File(dir).mkdirs();
			writeFile = dir + steConfig.getPropname() + "_" + normalizeTime(new Date());
			outputStream = new DataOutputStream(new FileOutputStream(writeFile));
		}
		
		List<Entry<String, Object>> results = getExtractResults(context, steConfig);
		StringBuffer sb = new StringBuffer();
		for(Entry<String, Object> e : results) {
			if(e.getKey().startsWith("content")){
				sb.append("\n" + e.getValue());
			}else{
				sb.append( "@@" + e.getKey() + ": " + e.getValue() );	
			}
		}
		
		sb.append("\n");
		outputStream.write(sb.toString().getBytes("utf-8"));
		System.out.print(sb.toString());
		count ++;
		if(count >= maxNumPerFile) {
			closeWriter();
		}
	}

	@Override
	public void closeWriter() throws IOException {
		if(outputStream != null) {
			outputStream.close();
			outputStream = null;
			count = 0;
			fileNum ++;

			String completedName = writeFile + "_complete";
			new File(writeFile).renameTo(new File(completedName));
		}
		
	}
	
	/**
	 * 获得可以正常显示的时间
	 */
	public String normalizeTime(Date date) {
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd_HHmm");
		return sm.format(date);
	}

	public void buildConf() {
		super.buildConf();
		int maxNum = CommonUtil.getIntValue(getParam("maxNumPerFile"));
		if(maxNum > 0) {
			maxNumPerFile = maxNum;
		}
	}
}
