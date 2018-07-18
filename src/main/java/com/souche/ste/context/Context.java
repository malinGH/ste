package com.souche.ste.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.souche.ste.util.CollectionUtils;
import com.souche.ste.util.CommonUtil;

/**
 * context上下文信息
 * @author hongwm
 * @since 2013年11月7日
 */
public class Context {
	private Map<String, Object> variables = 
			new HashMap<String, Object>();
	
	public void setVariable(String key, Object value) {
		variables.put(key, value);
	}
	
	
	public Object getVariable(String key) {
		return variables.get(key);
	}
	
	public Map<String, Object> getVariables() {
		return variables;
	}
	
	public Context clone() {
		Context c = new Context();
		c.getVariables().putAll(this.getVariables());
		return c;
	}


	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Entry<String, Object> e : variables.entrySet()) {
			sb.append(e.getKey()).append(": ").append(String.valueOf(e.getValue()).substring(0, 100)).append("\n");
		}
		
		return sb.toString();
	}
    public String toJsonString() {
        if(empty()) {
            return null;
        }
        
        return CommonUtil.toJsonString(variables);
    }
    
    private boolean empty() {
		return CollectionUtils.isEmpty(variables);
	}


	public void fromJsonString(String jsonString) {
        Map<String, Object> objs = CommonUtil.getJsonMap(jsonString);
        if(CollectionUtils.isNotEmpty(objs)) {
        	variables.putAll(objs);
        }
    }
    
	
}
