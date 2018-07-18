package com.souche.ste.download;

import java.util.HashMap;
import java.util.Map;

public class PolitenessController {
	private static PolitenessController instance;
	private PolitenessController() {
	}

	public static PolitenessController getInstance() {
		if(instance == null) {
			instance = new PolitenessController();
		}
		
		return instance;
	}
	
	private Map<String, Long> lastCheckMap = 
			new HashMap<String, Long>();
	
	/**
	 * 检查host的politeness，如果不满足，直接在内部做sleep动作
	 * @param politeness
	 * @param domain
	 */
	public synchronized boolean checkPoliteNess(int politeness, String host) {
		try {
			Long lastTime = lastCheckMap.get(host);
			long now = System.currentTimeMillis();
			if(lastTime != null) {
				long eclapse = now - lastTime;
				if(eclapse < politeness * 1000) {
					try {
						Thread.sleep(politeness * 1000 - eclapse);
					} catch (InterruptedException e) {
					}
				}
			}
		} finally {
			lastCheckMap.put(host, System.currentTimeMillis());
		}
		
		return true;
	}

	
}
