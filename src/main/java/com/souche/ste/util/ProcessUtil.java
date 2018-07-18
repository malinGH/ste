/**
 * 
 */
package com.souche.ste.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.springframework.util.CollectionUtils;

import com.souche.ste.context.Context;

/**
 * @author hongwm
 * @since 2013年11月8日
 */
public class ProcessUtil {
    private static Random rand = new Random();

	public static void markStop(Context context) {
		context.setVariable("stopped", true);
	}
	
	public static void clearStop(Context context) {
		context.getVariables().remove("stopped");
	}
	
	public static boolean isStoped(Context context) {
		Boolean variable = (Boolean) context.getVariable("stopped");
		if(variable != null && variable) {
			return true;
		}
		return false;
	}
	
	public static String readFromFile(String file) {
		String absoluteFile = CommonUtil.getAbsoluteFilePath(file);
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(absoluteFile), "utf-8");
			String line = StringUtil.readLine(reader);
			while(line != null) {
				sb.append(line);
//				sb.append('\n');
				line = StringUtil.readLine(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
    public static InputStream getAbsoluteFilePathStream(String file) throws IOException {
        if(new File(file).exists()) {
            return new FileInputStream(file);
        }
        
        URL u = CommonUtil.class.getResource(file);
        if(u == null) {
            u = CommonUtil.class.getResource("/" + file);
            if(u == null) {
                return null;
            }
        }
        return u.openConnection().getInputStream();
    }

	public static String join(List<String> list, String spliter) {
		StringBuffer sb = new StringBuffer();
		if(CollectionUtils.isEmpty(list)) {
			return sb.toString();
		}
		
		if(spliter == null) {
			spliter = " ";
		}
		
		for(String l : list) {
			if(sb.length() > 0) {
				sb.append(spliter);
			}
			sb.append(l);
		}
		return sb.toString();
	}

    /**
     * 生成n以内的随机数
     */
    public static int randomNum(int n) {
    	if(n <= 0) {
    		return 0;
    	}
        return Math.abs(rand.nextInt(n) % n);
    }
}
