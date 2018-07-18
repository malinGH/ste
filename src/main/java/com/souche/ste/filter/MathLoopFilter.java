package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.CommonUtil;
import com.souche.ste.util.ProcessUtil;
import com.souche.ste.util.StringUtil;

/**
 * for循环迭代器，每过一次该过滤器，to值增加1，当to值达到规定的maxValue值后，退出大的循环
 * 
 * @author hongwm
 * @since 2013年11月7日
 */
public class MathLoopFilter extends SteFilter implements IgnoreFilter {
	private int init = 1;
	private int maxValue = 0;
	private int inc = 1;
	
	private String to;
	private String maxAttr;

	private int nowVal = 0;
	private boolean ignore = false;

	@Override
	public void process(Context context, SteConfig config) {
		if (to == null) {
			return;
		}

		if (maxAttr != null) {
			maxValue = CommonUtil.getIntValue(context.getVariable(maxAttr));
		}
		
		if(nowVal >= maxValue) {
		    // 已经到达最大值了
			ignore = true;
			ProcessUtil.markStop(context);
		} else {

		    //还没到达最大值，nowValue自增
			nowVal += inc;

			context.setVariable(to, String.valueOf(nowVal));

			if (nowVal >= maxValue) {
				ProcessUtil.markStop(context);
			}
		}

	}

	public void addParam(String key, String value) {
		super.addParam(key, value);
		if ("init".equals(key)) {
			init = CommonUtil.getIntValue(value);
			nowVal = init;
		} else if ("maxValue".equals(key)) {
			if (StringUtil.isNumeric(value)) {
				maxValue = CommonUtil.getIntValue(value);
			} else {
				maxAttr = value;
			}

		} else if ("to".equals(key)) {
			to = value;
		} else if ("inc".equals(key)) {
			inc = CommonUtil.getIntValue(value);
		}
	}

	public void reset() {
		nowVal = init;
		ignore = false;
	}

	@Override
	public boolean ignore() {
		// TODO Auto-generated method stub
		return ignore;
	}
}
