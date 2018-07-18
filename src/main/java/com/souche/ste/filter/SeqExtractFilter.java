package com.souche.ste.filter;

import java.util.ArrayList;
import java.util.List;

import com.souche.ste.config.SteConfig;
import com.souche.ste.context.Context;

/**
 * 连续信息的extract抽取，从from变量中提取由begin到end的信息到to变量中
 * <pre>
 * 例子：
 * filter.class:com.souche.ste.filter.SeqExtractFilter
    from:item_info1
    begin.to1:&lt;/span&gt;&lt;/div&gt;&lt;div&gt;
    end.to1:&lt;/div
    begin.to2:&lt;/span&gt;&lt;/div&gt;&lt;div&gt;
    end.to2:&lt;/div
    begin.to3:&lt;/span&gt;&lt;/div&gt;&lt;div&gt;
    end.to3:&lt;/div
 * 
 * </pre>
 * @author hongwm
 * @since 2018年7月1日
 */
public class SeqExtractFilter extends SteFilter {
    protected String from;
    private List<ExtractInfo> extractInfos = new ArrayList<ExtractInfo>();
    protected String to;
    protected String begin;
    protected String end;

    @Override
    public void process(Context context, SteConfig config) {
        String fromVariable = (String) context.getVariable(from);

        if(fromVariable != null) {
            int beginIdx = 0;
            int endIdx = fromVariable.length();
            for(ExtractInfo extractInfo : extractInfos) {
                String toattr = extractInfo.getKey();
                String begin = extractInfo.getBegin();
                String end = extractInfo.getEnd();
                
                if(begin != null) {
                    int idx = fromVariable.indexOf(begin, beginIdx);
                    if(idx != -1) {
                        beginIdx = idx + begin.length();
                    }
                } else {
                    beginIdx = 0;
                }
                
                if(end != null) {
                    int idx = fromVariable.indexOf(end, beginIdx);
                    if(idx != -1) {
                        endIdx = idx;
                    } else {
                        endIdx = beginIdx;
                    }
                } else {
                    endIdx = fromVariable.length();
                }
                
                String toval = fromVariable.substring(beginIdx, endIdx);
                context.setVariable(toattr, toval);
                beginIdx = endIdx;
            }

        }
    }

    public void addParam(String key, String value) {
        super.addParam(key, value);
        if("from".equals(key)) {
            from = value;
        } else if(key.endsWith(".begin")) {
            // 以begin
            key = key.replace(".begin", "");
            ExtractInfo lastExtractInfo = getLastAddedExtractInfo();
            if(lastExtractInfo != null && lastExtractInfo.isSameKey(key)) {
                lastExtractInfo.setBegin(value);
            } else {
                ExtractInfo currentExtractInfo = new ExtractInfo(key);
                currentExtractInfo.setBegin(value);
                extractInfos.add(currentExtractInfo);
            }
        } else if(key.endsWith(".end")) {
            // 以end开头
            key = key.replace(".end", "");
            ExtractInfo lastExtractInfo = getLastAddedExtractInfo();
            if(lastExtractInfo != null && lastExtractInfo.isSameKey(key)) {
                lastExtractInfo.setEnd(value);
            } else {
                ExtractInfo currentExtractInfo = new ExtractInfo(key);
                currentExtractInfo.setEnd(value);
                extractInfos.add(currentExtractInfo);
            }
            
        }
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }
    
    // 获取最后一个加进来的抽取信息
    protected ExtractInfo getLastAddedExtractInfo() {
        if(extractInfos.size() == 0) {
            return null;
        }
        return extractInfos.get(extractInfos.size() - 1);
    }

    protected class ExtractInfo {
        private String key;
        private String begin;
        private String end;
        public ExtractInfo(String key) {
            this.key = key;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getBegin() {
            return begin;
        }
        public void setBegin(String begin) {
            this.begin = begin;
        }
        public String getEnd() {
            return end;
        }
        public void setEnd(String end) {
            this.end = end;
        }
        public boolean isSameKey(String okey) {
            if(okey == null || key == null) {
                return false;
            }
            return key.equals(okey);
        }
    }

}
