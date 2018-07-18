package com.souche.ste.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.DataFormatException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xcrawler util class
 * @author wenming.hong
 * @since 2013-2-20
 */
public class CommonUtil {
    private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    private static final Map<String, Integer> monthMap = 
            new HashMap<String, Integer>();
    static {
        String[] monthArray = new String[] { "Jan", "Feb", "Mar", "Apr", "May",
                "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        for (int i = 0; i < monthArray.length; ++i) {
            monthMap.put(monthArray[i], i);
        }
    }

    
    /**
     * sleep some time, in mil second
     */
    public static void sleep(long milsec) {
        try {
            Thread.sleep(milsec);
        } catch (InterruptedException e) {
        }
    }
    
    /**
     * Extract time from the last-Modified, Date or Expires HTTP headers, and
     * translate the date to time stamp For example, when get Last-Modified
     * header: "Thu, 27 May 2010 02:20:46 GMT", extract date: 2010 4 27 2 20 46
     * and then translate to time stamp
     * 
     * @param headerStr
     *            value of last-Modified header
     * @return
     * @throws DataFormatException
     */
    public static long headerDateStrToLong(String headerStr)
            throws DataFormatException {
        String[] tokens = headerStr.split(" ");
        if (tokens.length != 6) { // 
            throw new DataFormatException("The date string is not illegal");
        }
        // parse last-Modified header
        int year = Integer.valueOf(tokens[3]).intValue();
        int mon = monthMap.get(tokens[2]).intValue();
        int day = Integer.valueOf(tokens[1]).intValue();
        int hour = Integer.valueOf(tokens[4].split(":")[0]).intValue();
        int min = Integer.valueOf(tokens[4].split(":")[1]).intValue();
        int sec = Integer.valueOf(tokens[4].split(":")[2]).intValue();
        String timeZone = tokens[5];

        GregorianCalendar cal = new GregorianCalendar();
        cal.clear(); // clear defaulting setting
        cal.setTimeZone(TimeZone.getTimeZone(timeZone)); // set time zone
        cal.set(year, mon, day, hour, min, sec);

        return cal.getTimeInMillis(); // return time stamp
    }

    /**
     * ������object�����㈡����������������扮��string涓�
     */
    public static String objectToString(Object obj, char splitChar) {
        if(obj == null) {
            return null;
        }
        
        StringBuffer querySB = new StringBuffer();
        Class<? extends Object> cls = obj.getClass();
        Class<? extends Object> superCls = cls.getSuperclass();
        Field[] fields = cls.getDeclaredFields();
        Field[] superFields = superCls.getDeclaredFields();
        int count = 0;
        for(Field field : fields) {
            try {
                field.setAccessible(true);
                if(field.get(obj) == null || field.getName().equals("serialVersionUID")) {
                    continue;
                }
                
                if(Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                if(count++ > 0)  {
                    querySB.append(splitChar);
                }

                querySB.append(field.getName())
                       .append(": ")
                       .append(String.valueOf(field.get(obj)));
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
        
        for(Field field : superFields) {
            try {
                field.setAccessible(true);
                if(field.get(obj) == null || field.getName().equals("serialVersionUID")) {
                    continue;
                }
                
                if(Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                if(count++ > 0)  {
                    querySB.append(splitChar);
                }

                querySB.append(field.getName())
                       .append(": ")
                       .append(String.valueOf(field.get(obj)));
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
        
        return querySB.toString();
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

    /**
     * ������object�����㈡����������������扮��string涓�
     */
    public static String objectToString(Object obj) {
        return objectToString(obj, '\t');
    }
    
    public static String getAbsoluteFilePath(String file) {
        if(new File(file).exists()) {
            return file;
        }
        
        URL u = CommonUtil.class.getResource(file);
        if(u == null) {
        	u = CommonUtil.class.getResource("/" + file);
        	if(u == null) {
        		return file;
        	}
        }
        return u.getPath();
    }
    
    /**
     * 寰����int���
     */
    public static int getIntValue(Object obj) {
        try {
            if(obj == null) {
                return 0;
            }

            if(obj instanceof Integer) {
                return (Integer) obj;
            }

            if(obj instanceof Long) {
                return ((Long)obj).intValue();
            }

            if(obj instanceof String) {
                if(StringUtil.isEmpty((String) obj)) {
                    return 0;
                }
                return Integer.parseInt(StringUtil.trim((String) obj));
            }
        } catch (Exception e) {
//            logger.warn("", e);
        }
        return 0;
    }
    
    public static long getLongValue(Object obj) {
        try {
            if(obj == null) {
                return 0;
            }

            if(obj instanceof Integer) {
                return ((Integer) obj).longValue();
            }

            if(obj instanceof Long) {
                return ((Long)obj).longValue();
            }

            if(obj instanceof String) {
                return Long.parseLong((String) obj);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
        return 0;
    }
    
    public static float getFloatValue(Object obj) {
        try {
            if(obj == null) {
                return 0;
            }

            if(obj instanceof Integer) {
                return ((Integer) obj).floatValue();
            }

            if(obj instanceof Long) {
                return ((Long)obj).floatValue();
            }

            if(obj instanceof String) {
                return Float.parseFloat((String) obj);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
        return 0;
    }
    
    /**
     * ��峰�����浠ユ�ｅ父��剧ず�����堕��
     * @param time
     * @return
     */
    public static String normalizeTime(long time) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return sm.format(date);
    }
    
    public static String normalizeTime(Date date) {
        return normalizeTime(date.getTime());
    }

	public static String toJsonString(Map<String, Object> variables) {
		return new JSONObject(variables).toString();
	}

	public static Map<String, Object> getJsonMap(String jsonString) {
		
		return new JSONObject(jsonString).toMap();
	}

}
