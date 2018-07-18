package com.souche.ste.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.process.builder.AbstractProcessBuilder;
import com.souche.ste.result.ResultBuilder;
import com.souche.ste.source.SourceBuilder;

/**
 * souche template extract文档处理器
 * @author hongwm
 * @since 2013年11月7日
 */
public class SteDocumentProcess {
	private Logger logger = LoggerFactory.getLogger(SteDocumentProcess.class);
	private ResultBuilder resultBuilder;
	private SourceBuilder sourceBuilder;
	private List<AbstractProcessBuilder> builders = 
			new ArrayList<AbstractProcessBuilder>();
	
	private Stack<Integer> loopIds = 
			new Stack<Integer>();
//	private Queue<Integer> loopIds = 
//			new ArrayDeque<Integer>();
			
	
	private volatile boolean stop = false;
	
	public void process(SteConfig config) throws IOException {
		Context context = new Context();
		
		sourceBuilder.buildConf();
		resultBuilder.buildConf();
		for(AbstractProcessBuilder builder : builders) {
			builder.buildConf();
		}
		
		sourceBuilder.process(config, context);
		int startProcessId = 0;
		while(!stop) {
			boolean firstProcess = true;
			boolean ignore = false;
			for(int i=startProcessId; i<builders.size(); i++) {
				AbstractProcessBuilder builder = builders.get(i);
				if(!firstProcess) {
					// 为保证循环处理能够再次进行，如果不是第一个builder，需要进行重置
					builder.reset(context);
				}
				
				try {
					builder.process(context, config);
				} catch (Exception e) {
					logger.warn("", e);
				}
				if(builder.nextLoop()) {
					//  如果该builder后面还需要循环，加入到循环队列
					loopIds.push(i);
				}
				firstProcess = false;
				if(builder.ignore()) {
					ignore = true;
					break;
				}
			}
			
			if(!ignore) {
				resultBuilder.write(context, config);
			}
			if(loopIds.isEmpty()) {
				break;
			}
			startProcessId = loopIds.pop();
		}
		
		resultBuilder.closeWriter();
	}
	
	public ResultBuilder getResultBuilder() {
		return resultBuilder;
	}
	public void setResultBuilder(ResultBuilder resultBuilder) {
		this.resultBuilder = resultBuilder;
	}
	public SourceBuilder getSourceBuilder() {
		return sourceBuilder;
	}
	public void setSourceBuilder(SourceBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}
	public List<AbstractProcessBuilder> getBuilders() {
		return builders;
	}
	public void setBuilders(List<AbstractProcessBuilder> builders) {
		this.builders = builders;
	}
	
	public void addBuilder(AbstractBuilder builder) {
		if(builder instanceof SourceBuilder) {
			sourceBuilder = (SourceBuilder) builder;
		} else if(builder instanceof ResultBuilder) {
			resultBuilder = (ResultBuilder) builder;
		} else {
			builders.add((AbstractProcessBuilder) builder);
		}
	}

	public boolean isStop() {
		return stop;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	
}
