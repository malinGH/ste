package com.souche.ste.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wenming.hong
 * @since 2012-7-12
 */
public class UrlUtil {
    private final static Logger logger = LoggerFactory.getLogger(UrlUtil.class);
    private static DomainMap domainMap;
    static {
        domainMap = new DomainMap();
    }

    /**
     *  get domain names from a host
     *  @return domain
     */
    public static String getDomainFromUrl(String url){
        if(url == null) {
            return null;
        }

        int start = url.indexOf("//");
        if(start >= 0) {
            url = url.substring(start + 2);
        }

        int end = url.indexOf("/");
        if(end >= 0) {
            url = url.substring(0, end);
        }

        end = url.indexOf(":");
        if(end >= 0) {
            url = url.substring(0, end);
        }

        if(isIpAddr(url)) {
            return url;
        }

        int domainIndex = -1;
        int tailIndex = url.indexOf("."); 
        String domainTail = url.substring(tailIndex + 1);
        while(!domainMap.isDomainTail(domainTail)) {

            int nextTailIndex = url.indexOf(".", tailIndex + 1);
            if(nextTailIndex == -1) {
                break;
            } else {
                domainIndex = tailIndex;
                tailIndex = nextTailIndex;
            }

            domainTail = url.substring(tailIndex + 1);
        }

        return url.substring(domainIndex + 1);
    }

    public static boolean isIpAddr(String host) {
        if(host==null || host.length() == 0) {
            return false;
        }
        
        int dotNum = 0;
        for(int i=0; i<host.length(); i++) {
            char c = host.charAt(i);
            if(c == '.') {
                dotNum ++;
                continue;
            }
            if(dotNum > 3) {
                return false;
            }
        
            if(c > '9' || c < '0') {
                return false;
            }
        }
        return true;
    }

    public static boolean isLegalUrl(String url) {
        try {
            if(!url.startsWith("http")) {
                url = "http://" + url;
            }
            new URL(url).getHost();
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    public static String getUrlSuffix(String url) {
        if (url == null) {
            return null;
        }
        String path = url;
        try {
            URL uri = new URL(url);
            path = uri.getPath();
        } catch (Exception e) {
        }

        int lastDotIndex = path.lastIndexOf(".");
        if (lastDotIndex < 0) {
            return null;
        }
        return path.substring(lastDotIndex + 1);
    }

    public static String getUrlHead(String url) {
        if (url == null) {
            return null;
        }
        try {
            int lastDotIndex = url.lastIndexOf(".");
            if (lastDotIndex < 0) {
                return url;
            }
            return url.substring(0, lastDotIndex);
        } catch (Exception e) {
            logger.warn("", e);
        }
        return url;
    }
    
    /**
     * get absolute url
     * @param parentUrl
     * @param url
     * @return absolute url
     */
    public static String getAbsoluteUrl(URL parentUrl, String url){
        if(StringUtil.isEmpty(url)) {
            return url;
        }

        if(url.indexOf("//") > 0) {
            return url;
        }

        if(parentUrl == null) {
            return url;
        }

        String path;
        boolean modified;
        boolean absolute;
        int index;
        String base = null;
        URL u;

        if(parentUrl != null) {
            base = parentUrl.toExternalForm();
        }

        if (('?' == url.charAt (0)))
        {   // remove query part of base if any
            index = base.lastIndexOf ('?');
            if (-1 != index) {
                base = base.substring (0, index);
            }

            return base + url;
        }
        try {

            u = new URL (parentUrl, url);

            path = u.getFile ();
            modified = false;
            absolute = url.startsWith ("/");
            if (!absolute)
            {   // we prefer to fix incorrect relative links
                // this doesn't fix them all, just the ones at the start
                while (path.startsWith ("/."))
                {
                    if (path.startsWith ("/../"))
                    {
                        path = path.substring (3);
                        modified = true;
                    }
                    else if (path.startsWith ("/./") || path.startsWith("/."))
                    {
                        path = path.substring (2);
                        modified = true;
                    }
                    else {
                        break;
                    }
                }
            }
            // fix backslashes
            while (-1 != (index = path.indexOf ("/\\")))
            {
                path = path.substring (0, index + 1) + path.substring (index + 2);
                modified = true;
            }

            if (modified) {
                u = new URL (u, path);
            }

            return u.toExternalForm();
        }
        catch (MalformedURLException e) {
            return url;
        }
    }
    
    /**
     * 获取url的path
     * @param url
     * @return
     */
    public static String getUrlPath(String url) {
        if(StringUtil.isEmpty(url)) {
            return "";
        }
        
        if(!url.startsWith("http")) {
            return url;
        }
        
        try {
            URL u = new URL(url);
            return u.getPath();
        } catch (Exception e) {
            return url;
        }
    }

    public static String getHostname(String url) {
        if(url == null) {
            return null;
        }

        int start = url.indexOf("//");
        if(start >= 0) {
            url = url.substring(start + 2);
        }

        int end = url.indexOf("/");
        if(end >= 0) {
            url = url.substring(0, end);
        }

        end = url.indexOf(":");
        if(end >= 0) {
            url = url.substring(0, end);
        }
        
        return url;
    }
}
