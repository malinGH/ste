package com.souche.ste.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Map工具类
 * 
 * @author Taylor xuwei840916@hotmail.com
 * @version 2009-1-23 下午09:50:45
 */
public final class CollectionUtils {

    public static <T> T[] toArray(List<T> list) {
        if(isEmpty(list)) {
            return null;
        }
        @SuppressWarnings("unchecked")
        T[] array = ((T[]) new Object[list.size()]);
        list.toArray(array);
        return array;
    }
    
	/**
	 * map是否为空
	 */
	public static boolean isEmpty(Map<? extends Object, ? extends Object> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * map是否不为空
	 */
	public static boolean isNotEmpty(Map<? extends Object, ? extends Object> map) {
		return map != null && !map.isEmpty();
	}

	/**
	 * 将Map转换为List
	 */
	public static List<? extends Object> parseMap2List(Map<? extends Object, ? extends Object> map) {
		List<Object> list = null;
		if (isNotEmpty(map)) {
			list = new ArrayList<Object>();
			for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
				list.add(entry.getValue());
			}
		}
		return list;
	}
	
	public static boolean isEmpty(Collection<? extends Object> list) {
	    if(list == null || list.isEmpty()) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean isNotEmpty(Collection<? extends Object> list) {
	    if(list == null || list.isEmpty()) {
	        return false;
	    }
	    return true;
	}
	
    /**
     * 把list转换成string，中间以combineChar来连接
     * @param lists
     * @param combineChar
     * @return
     */
    public static <T> String combineListToString(List<T> lists, char combineChar) {
        StringBuffer sb = new StringBuffer();
        int i=0;
        for(T l : lists) {
            if(i > 0) {
                sb.append(combineChar);
            }
            i++;
            sb.append(String.valueOf(l));
        }
        
        return sb.toString();
    }

}
