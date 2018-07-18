package com.souche.ste.filter;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;
import com.souche.ste.util.Entry;

/**
 * 条件判断filter，判断attr是否包含某个关键字，如果包含，设置match属性，如果不包含，设置unmatch属性
 * <pre>
 * 例子：
 * 假设参数num中的值为0595.123456
 * filter.class:com.souche.ste.filter.ConditionFilter
    attr:num
    contains:.
    match.hasDot:1
    unmatch.hasDot:0
 * 执行上面的filter后，hasDot的值为1
 * 
 * 参数说明：
 *  atrr: 要判断的参数
 *  contains: 要判断是否包含的值
 *  match.param: 如果包含，要设置param参数的信息
 *  unmatch.param:如果不包含，要设置param参数的信息
 * </pre>
 * 
 * @author hongwm
 * @since 2014年3月25日
 */
public class ConditionFilter extends SteFilter {
    private String attr;
    private String contains;
    private Entry<String, String> matchEntry;
    private Entry<String, String> unmatchEntry;

    @Override
    public void process(Context context, SteConfig config) {
        String containsStr = (String) context.getVariable(contains);
        if(containsStr == null) {
            containsStr = contains;
        }

        boolean matchTag = false;
        String attrValue = (String) context.getVariable(attr);
        if(attrValue != null) {
            if(attrValue.contains(containsStr)) {
                matchTag = true;
            }
        }

        Entry<String, String> e = null;
        if(matchTag) {
            e = matchEntry;
        } else {
            e = unmatchEntry;
        }
        if(e != null) {
            String value = (String) context.getVariable(e.getValue());
            if(value == null) {
                value = e.getValue();
            }
            context.setVariable(e.getKey(), value);
        }
    }

    public void addParam(String key, String value) {
        super.addParam(key, value);
        if("attr".equals(key)) {
            attr = value;
        } else if("contains".equals(key)) {
            contains = value;
        } else if(key.startsWith("match")) {
            key = key.replaceAll("match.", "");
            matchEntry = new Entry<String, String>(key, value);
        } else if(key.startsWith("unmatch")) {
            key = key.replaceAll("unmatch.", "");
            unmatchEntry = new Entry<String, String>(key, value);
        }
    }

}
