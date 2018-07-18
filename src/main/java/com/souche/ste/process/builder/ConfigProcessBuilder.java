package com.souche.ste.process.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.filter.IgnoreFilter;
import com.souche.ste.filter.LoopFilter;
import com.souche.ste.filter.MultiExtract;
import com.souche.ste.filter.SteFilter;

/**
 * @author hongwm
 * @since 2013年11月7日
 */
public class ConfigProcessBuilder extends AbstractProcessBuilder {
	private Logger logger = LoggerFactory.getLogger(ConfigProcessBuilder.class);
	private boolean ignore = false;
	/**
	 * 需要循环的过滤器id列表
	 */
	private Stack<Integer> loopIds =
			new Stack<Integer>();
	
	private List<String> initAttrs = 
//			new UniqueList<String>();
			new ArrayList<String>();
	
	public void process(Context context, SteConfig config) {
		setupInitAttrs(context);
		List<SteFilter> filters = getFilters();
		int id = 0;
		if(!loopIds.isEmpty()) {
			id = loopIds.pop();
		}
		
		boolean firstFilter = true;
		ignore = false;
		for(int i=id; i<filters.size(); i++) {
			SteFilter filter = filters.get(i);
			
			if(!firstFilter && (filter instanceof MultiExtract)) {
				// 非第一个filter，且为MultiExtract，重置该处理器
				filter.reset();
			}
			
			try {
//				System.out.println(filter.getClass().toString());
				filter.process(context, config);
			} catch (Exception e) {
				logger.warn("", e);
			}
			firstFilter = false;
			
			if(filter instanceof LoopFilter) {
				if(((LoopFilter) filter).hasNext()) {
					// 是循环处理器，且还有下一个节点，加入循环处理id列表
					loopIds.push(i);
				}
			}
			
			if(filter instanceof IgnoreFilter) {
				if(((IgnoreFilter) filter).ignore()) {
					System.out.println("ret of filter " + filter.getClass().toString() + " is ignore");
					ignore = true;
					break;
				}
			}
			
		}
		
	}
	
	/**
	 * 设定builder初始的保留变量
	 */
	public void setupInitAttrs(Context context) {
		if(initAttrs.isEmpty()) {
			initAttrs.addAll(getAttrs());
			initAttrs.addAll(context.getVariables().keySet());
		}
	}
	
	public void reset(Context context) {
		List<SteFilter> filters = getFilters();
		for(SteFilter filter : filters) {
			filter.reset();
		}
		loopIds.clear();
		if(!initAttrs.isEmpty()) {
			// 清除context中非初始变量的值
			Iterator<Entry<String, Object>> itr = context.getVariables().entrySet().iterator();
			while(itr.hasNext()) {
				String key = itr.next().getKey();
				if(!initAttrs.contains(key)) {
					itr.remove();
				}
			}
		}
		ignore = false;
	}

	/* (non-Javadoc)
	 * @see com.souche.ste.process.builder.AbstractProcessBuilder#nextLoop()
	 */
	@Override
	public boolean nextLoop() {
		return !loopIds.isEmpty();
	}

	@Override
	public boolean ignore() {
		return ignore;
	}
}
