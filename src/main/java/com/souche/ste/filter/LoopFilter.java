package com.souche.ste.filter;

/**
 * multi过滤器类型
 * @author hongwm
 * @since 2013年11月7日
 */
public interface LoopFilter {
	/**
	 * 该过滤器是否还需要下一次处理
	 * @return
	 */
	public boolean hasNext();
}
