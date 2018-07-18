package com.souche.ste.props;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.StringUtil;

public class PropsAttrs {
	private String props;
	private long lastStartTime = 0;
	private int interval;

	private final static String STE_MAIN = "/usr/local/ste/ste.sh crawl %s > %s 2>&1";

	public static PropsAttrs parseLine(String line) {
		String[] str = line.split(";");
		if (str.length < 1) {
			return null;
		}

		PropsAttrs prop = new PropsAttrs();
		prop.setProps(str[0]);
		Map<String, String> kvs = new HashMap<String, String>();
		for (int i = 1; i < str.length; i++) {
			String k = str[i];
			String[] kv = k.split("=");
			if (kv.length != 2) {
				continue;
			}
			kvs.put(StringUtil.trim(kv[0]), StringUtil.trim(kv[1]));
		}

		prop.setLastStartTime(CommonUtil.getLongValue(kvs.get("lastStartTime")));
		prop.setInterval(CommonUtil.getIntValue(kvs.get("interval")));
		return prop;
	}

	public void persist(OutputStream outputStream) {
		String line = this.getProps() + ";interval = " + this.getInterval() 
				+ ";lastStartTime = " + this.getLastStartTime();
		writeToLine(outputStream, line.trim());
	}

	private void writeToLine(OutputStream outputStream, String line) {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				outputStream));
		try {
			bw.write(line);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isToRun() {
		long now = System.currentTimeMillis();
		boolean flag = false;
		if (((now - lastStartTime) / 1000) > interval) {
			flag = true;
		}
		return flag;
	}

	public void run() {
		String propFilePath = props;

		String exec = String.format(STE_MAIN, propFilePath, "/tmp/log.crawl");
		try {
			System.out.println("start to run " + exec);
			Runtime.getRuntime().exec(exec);
		} catch (IOException e) {
			e.printStackTrace();
		}
		lastStartTime = System.currentTimeMillis();
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}

	public long getLastStartTime() {
		return lastStartTime;
	}

	public void setLastStartTime(long lastStartTime) {
		this.lastStartTime = lastStartTime;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "PropsAttrs [props = " + props + "; interval = " + interval + "; lastStartTime = " + lastStartTime + "]";
	}



}
