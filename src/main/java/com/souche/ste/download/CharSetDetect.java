package com.souche.ste.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * detech the charset;
 */
public class CharSetDetect {
    private final Logger logger = LoggerFactory.getLogger(CharSetDetect.class);
    private final String defaultEncoding= "gbk";
    private final byte[] markerUTF8 = { (byte) 0xef, (byte) 0xbb, (byte) 0xbf };
    private final byte[] markerUTF16BE = { (byte) 0xfe, (byte) 0xff };
    private final byte[] markerUTF16LE = { (byte) 0xff, (byte) 0xfe };

    private final Pattern metaPattern = 
            Pattern.compile("<meta.+?charset\\s*=\\s*\"?([a-z0-9-_]+)\"?",Pattern.CASE_INSENSITIVE);

    private byte[] body;

    private String httpCharSet =null;
    private String checkedCharSet ;

    private String guessByByteChar;
    private boolean found ;

    private String gbk="gbk";
    private String gb2312="gb2312";

    
    public CharSetDetect() {
    }

    public CharSetDetect(byte[] body) {
        this.body = body;
    }
    
    /**
     * check charset
     */
    public void checkCharSet() {
        if(checkedCharSet != null){
            return;
        }

        if(body == null) {
            return;
        }
        
        String encoding = null;
        //sequece : utf marker,httpheader,meta,xml attr,byte stream 

        // http header
        encoding = guessEncodeFromHttpCharset(httpCharSet);

        if(isGoodEncoding(encoding)) {
            logger.debug("get charset from http header :" + encoding);

            this.checkedCharSet = encoding;
            return;
        }

        encoding = guessEncodeFromUTFMarker();

        if(isGoodEncoding(encoding)) {
            logger.debug("get charset from utfmarker :" + encoding);

            this.checkedCharSet = encoding;
            return;
        }

        // meta information
        encoding = guessEncodeFromMeta();

        if(isGoodEncoding(encoding)) {
            logger.debug("get charset from meta:" + encoding);

            this.checkedCharSet = encoding;
            return;
        }

        // check stream
        encoding = guessEncodeFromByte();

        if(isGoodEncoding(encoding)) {
            logger.debug("get charset from stream :" + encoding);

            this.checkedCharSet = encoding;
            return;
        }

        // html xml marker
        encoding = getXMLEncoding(body);

        if(isGoodEncoding(encoding)) {
            logger.debug("get charset from xml info :" + encoding);

            this.checkedCharSet = encoding;
            return;
        }

        logger.debug("can not guess out encoding, use default encoding: {}", defaultEncoding);
        this.checkedCharSet = defaultEncoding;

    }

    /**
     * guess charset from meta data
     * @return charset guessed
     */
    private String guessEncodeFromMeta() {
        if(body == null) {
            return null;
        }
        String encoding = null;
        try{
            Matcher matcher;
            int maxNum = body.length;
            if(maxNum >= 1000) {
                maxNum = 1000;
            }

            matcher = metaPattern.matcher(new String(body, 0, maxNum, "ASCII"));
            if (matcher.find()) {
                encoding = matcher.group(1);

            }
        }catch(UnsupportedEncodingException e){
            logger.debug("", e);
        }

        return encoding;
    }

    /**
     * guess encode from utf marker
     * @return encode guessed base on utf marker
     */
    private String guessEncodeFromUTFMarker() {
        String encoding = null;
        // utf marker
        if (body != null
                && ArrayUtils.isEquals(markerUTF8, ArrayUtils.subarray(body, 0,
                        3))) {
            logger.debug("UTF-8 marker found");
            encoding = "UTF-8";
        } else if (body != null
                && ArrayUtils.isEquals(markerUTF16BE, ArrayUtils.subarray(body,
                        0, 2))) {
            logger.debug("UTF-16BE marker found");
            encoding = "UTF-16BE";
        } else if (body != null
                && ArrayUtils.isEquals(markerUTF16LE, ArrayUtils.subarray(body,
                        0, 2))) {
            logger.debug("UTF-16LE marker found");
            encoding = "UTF-16LE";
        }        
        return encoding;
    }

    /**
     * check if encoding is good encoding
     * @param encoding
     * @return true if good, false else
     */
    private boolean isGoodEncoding(String encoding) {
        if(encoding == null) {
            return false;
        }
        try{
            if(Charset.isSupported(encoding)){
                return true;
            }
        }catch(Exception e){
            logger.debug("", e);
        }


        return false;
    }

    /**
     * guess encoding from http head charset
     * @param httpCharSet
     * @return charset guessed
     */
    private String guessEncodeFromHttpCharset(String httpCharSet) {
        String encoding = null;
        //            logger.info("httpCharSet: {}", httpCharSet);

        if (isGoodEncoding(httpCharSet)) {
            encoding = httpCharSet;
            try{
                if (!Charset.isSupported(encoding)) {
                    encoding = null;
                    logger.warn("Unsupported charset in http head: " + encoding);
                }
            }catch(Exception e){
                encoding = null;
            }
        }

        return encoding;
    }

    /**
     * get utf8 string of content
     * @return utf8 string
     */
    public String getutf8String() {
        if(checkedCharSet == null) {
            checkCharSet();
        }
        
        if(body == null) {
            return null;
        }

        String text = null;
        try{

            if(this.checkedCharSet  == null 
                    || !Charset.isSupported(this.checkedCharSet)){
                String tmp = new String(body,this.defaultEncoding);
                text = new String(tmp.getBytes("utf-8"),"utf-8");
            }else{
                if(this.checkedCharSet.equalsIgnoreCase("utf-8")){
                    text = new String(body,"utf-8");
                }else{
                    String actChar=this.checkedCharSet;
                    if(actChar.equalsIgnoreCase(this.gb2312)){
                        actChar=this.gbk;
                    }
                    String tmp = new String(body,actChar);
                    text = new String(tmp.getBytes("utf-8"),"utf-8");
                }
            }
        }catch(Exception e){
            logger.error("",e);
        }
        return text;
    }

    /**
     * get encoding guessed
     * @return guessed encoding
     */
    public String getEncoding(){
        if(checkedCharSet == null) {
            checkCharSet();
        }
        return checkedCharSet;
    }

    /**
     * Searches for XML declaration and returns the <tt>encoding</tt> if
     * found, otherwise returns <code>null</code>.
     * @return xml encoding
     */
    private String getXMLEncoding(final byte[] body) {
        String encoding = null;
        final byte[] declarationPrefix = "<?xml ".getBytes();
        if (ArrayUtils.isEquals(declarationPrefix, ArrayUtils.subarray(body, 0, declarationPrefix.length))) {
            final int index = ArrayUtils.indexOf(body, (byte) '?', 2);
            if (index + 1 < body.length && body[index + 1] == '>') {
                final String declaration = new String(body, 0, index + 2);
                int start = declaration.indexOf("encoding");
                if (start != -1) {
                    start += 8;
                    char delimiter;

                    outer:
                        while (true) {
                            switch (declaration.charAt(start)) {
                            case '"':
                            case '\'':
                                delimiter = declaration.charAt(start);
                                start = start + 1;
                                break outer;

                            default:
                                start++;
                            }
                        }
                    final int end = declaration.indexOf(delimiter, start);
                    encoding = declaration.substring(start, end);
                }
            }
        }
        return encoding;
    }

    /**
     * guess encoding from byte using mozilla encoding checker
     * @return encoding guessed
     */
    private String guessEncodeFromByte() {

        String encoding = null ;
        nsDetector det = new nsDetector(nsPSMDetector.ALL);
        det.Init(new nsICharsetDetectionObserver() {
            public void Notify(String charset) {
                found = true;
                guessByByteChar = charset;
            }

        });
        boolean isAscii = true ;

        byte[] buf = new byte[1024] ;
        boolean done = false ;

        int totalLen = body.length;
        int nowIndex = 0;
        while(nowIndex < totalLen){
            for(int i = 0;i<1024 ;i++){
                buf[i] = 0;
            }
            int len = 0; 
            for(int i=0;i<1024 && nowIndex<totalLen;i++,nowIndex++){
                buf[i] = body[nowIndex];
                len ++;
            }
            if (isAscii) {
                isAscii = det.isAscii(buf,len);
            }

            // DoIt if non-ascii and not done yet.
            if (!isAscii && !done) {
                done = det.DoIt(buf,len, false);
            }
        }
        det.DataEnd();

        if (isAscii) {
            encoding = "ASCII";
        }else {
            if(!found){
                String []prob = det.getProbableCharsets() ;
                if(prob.length > 0){
                    if(Charset.isSupported(prob[0])){
                        encoding = prob[0];
                    }
                }else{
                    encoding = null;
                }

            }else{
                logger.debug("found charset :"+guessByByteChar);
                encoding = guessByByteChar;
            }

        }
        return encoding;
    }

    /**
     * set input stream
     * @param in
     * @return false if exception catched, true else
     */
    public boolean setInputStream(InputStream in) {
        ByteArrayOutputStream buffer = null;
        BufferedInputStream imp = null;
        try {
            imp = new BufferedInputStream(in);
            buffer = new ByteArrayOutputStream(10240);
            byte[] buf = new byte[10240] ;
            int len;
            while((len=imp.read(buf, 0 ,buf.length)) != -1) {
                buffer.write(buf, 0, len);
            }
            body =buffer.toByteArray();
            imp.close();

        }
        catch (Exception e) {
            return false;
        }
        finally {
            if (imp != null) {
                try {
                    imp.close();
                }catch (Exception ex) {
                    logger.debug("", ex);
                }
            }
            if (buffer != null) {
                try {
                    buffer.close();
                }catch (Exception ex) {
                    logger.debug("", ex);
                }
            }           
        }

        return true;
    }

    /**
     * set body to be checked
     * @param bodybyte
     */
    public void setBody(byte[] bodybyte){
        this.body = bodybyte;
    }

    /**
     * set http charset
     * @param httpCharset
     */
    public void setHttpHeadCharset(String httpCharset){
        this.httpCharSet = httpCharset;

    }

    /**
     * get bytes
     * @return bytes of content
     */
    public byte[] getBytes() {
        return body;
    }
}
