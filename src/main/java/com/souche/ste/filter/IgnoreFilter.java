package com.souche.ste.filter;


/**
 * 忽略判断filter
 * @author hongwm
 * @since 2014年4月19日
 */
public interface IgnoreFilter {
	/**
	 * 判断是否要忽略该条记录
	 * @param context
	 * @return
	 */
	public boolean ignore();
}
