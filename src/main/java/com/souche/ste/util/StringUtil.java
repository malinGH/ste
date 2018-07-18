/*
 *
 * Title: Git Fast Devlopement Platform
 *
 * Copyright: Copyright (c) 2005
 *
 * Company: Git Co., Ltd
 * 
 * All right reserved.
 * 
 * Created on 2005-12-23 by GuoJian Zhang
 * 
 * JDK version used		:1.4.1
 * 
 * Modification history:
 *
 */
package com.souche.ste.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串工具类
 * 
 * @author GuoJian.Zhang
 * @author Taylor
 * @author wenming.hong
 */
public final class StringUtil {

	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	/** 空字符串。 */
	public static final String EMPTY_STRING = "";

	private final static String AS = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int UNICODE_CHAR_NUM = 65536;
    
    public static char[] spaces = new char[UNICODE_CHAR_NUM];
    static {
        for (int i = 0; i < UNICODE_CHAR_NUM; ++i) {
            spaces[i] = 0;
        }
        spaces[' '] = 1;
        spaces['\t'] = 1;
        spaces['\r'] = 1;
        spaces['\n'] = 1;
        spaces['\u3000'] = 1;
    }

    public static char[] crlfs = new char[UNICODE_CHAR_NUM];
    static {
        for (int i = 0; i < UNICODE_CHAR_NUM; ++i) {
            crlfs[i] = 0;
        }
        crlfs['\r'] = 1;
        crlfs['\n'] = 1;
    }

    public static char[] chnPuncs = new char[UNICODE_CHAR_NUM];
    static {
        for (int i = 0; i < UNICODE_CHAR_NUM; ++i) {
            chnPuncs[i] = 0;
        }
        chnPuncs['\uff0c'] = 1;
        chnPuncs['\u3002'] = 1;
        chnPuncs['\uff01'] = 1;
        chnPuncs['\uff1f'] = 1;
        chnPuncs['\u3001'] = 1;
        chnPuncs['\u2026'] = 1;
        chnPuncs['\u201d'] = 1;
        chnPuncs['\u201c'] = 1;
        chnPuncs[','] = 1;
        chnPuncs['!'] = 1;
        chnPuncs['?'] = 1;
    }

    public static boolean isBlank(char c) {
        return spaces[c] == 1;
    }
    
    public static boolean isCrlf(char c) {
        return crlfs[c] == 1;
    }
    
    public static boolean isChnPunc(char c) {
        return chnPuncs[c] == 1;
    }

	public static String convertEncode(String strIn, String encoding, String targetEncoding) {
		String strOut = strIn;
		if (strIn == null)
			return strOut;

		try {
			if (encoding != null && targetEncoding != null) {
				strOut = new String(strIn.getBytes(encoding), targetEncoding);
			} else if (encoding != null) {
				strOut = new String(strIn.getBytes(encoding));
			} else if (targetEncoding != null) {
				strOut = new String(strIn.getBytes(), targetEncoding);
			} else {
				return strOut;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("Unsupported Encoding: " + encoding);
		}
		return strOut;
	}

	/*
	 * ==========================================================================
	 * ==
	 */
	/* 替换字符串（或指定字符）的函数。 */
	/*                                                                              */
	/* 以下方法用来替换一个字串中的子串或指定字符。 */
	/*
	 * ==========================================================================
	 * ==
	 */

	/**
	 * 替换指定的子串，替换所有出现的子串。
	 * 
	 * <p>
	 * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>
	 * ，则返回原字符串。
	 * 
	 * <pre>
	 * StringUtil.replace(null, *, *)        = null
	 * StringUtil.replace(&quot;&quot;, *, *)          = &quot;&quot;
	 * StringUtil.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
	 * StringUtil.replace(&quot;aba&quot;, null, null) = &quot;aba&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, null)  = &quot;aba&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;&quot;)    = &quot;b&quot;
	 * StringUtil.replace(&quot;aba&quot;, &quot;a&quot;, &quot;z&quot;)   = &quot;zbz&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param text 要扫描的字符串
	 * @param repl 要搜索的子串
	 * @param with 替换字符串
	 * 
	 * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	/**
	 * 替换指定的子串，替换指定的次数。
	 * 
	 * <p>
	 * 如果字符串为<code>null</code>则返回<code>null</code>，如果指定子串为<code>null</code>
	 * ，则返回原字符串。
	 * 
	 * <pre>
	 * StringUtil.replace(null, *, *, *)         = null
	 * StringUtil.replace(&quot;&quot;, *, *, *)           = &quot;&quot;
	 * StringUtil.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, null, null, 1) = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, null, 1)  = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;&quot;, 1)    = &quot;baa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 0)   = &quot;abaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 1)   = &quot;zbaa&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, 2)   = &quot;zbza&quot;
	 * StringUtil.replace(&quot;abaa&quot;, &quot;a&quot;, &quot;z&quot;, -1)  = &quot;zbzz&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param text 要扫描的字符串
	 * @param repl 要搜索的子串
	 * @param with 替换字符串
	 * @param max maximum number of values to replace, or <code>-1</code> if no
	 *            maximum
	 * 
	 * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
	 */
	public static String replace(String text, String repl, String with, int max) {
		if ((text == null) || (repl == null) || (with == null) || (repl.length() == 0) || (max == 0)) {
			return text;
		}

		StringBuffer buf = new StringBuffer(text.length());
		int start = 0;
		int end = 0;

		while ((end = text.indexOf(repl, start)) != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();

			if (--max == 0) {
				break;
			}
		}

		buf.append(text.substring(start));
		return buf.toString();
	}

	/*
	 * ==========================================================================
	 * ==
	 */
	/* 去空白（或指定字符）的函数。 */
	/*                                                                              */
	/* 以下方法用来除去一个字串中的空白或指定字符。 */
	/*
	 * ==========================================================================
	 * ==
	 */

	/**
	 * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trim(null)          = null
	 * StringUtil.trim(&quot;&quot;)            = &quot;&quot;
	 * StringUtil.trim(&quot;     &quot;)       = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(String str) {
		return trim(str, null, 0);
	}

	/**
	 * 除去字符串头尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.trim(null, *)          = null
	 * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(String str, String stripChars) {
		return trim(str, stripChars, 0);
	}

	/**
	 * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trimStart(null)         = null
	 * StringUtil.trimStart(&quot;&quot;)           = &quot;&quot;
	 * StringUtil.trimStart(&quot;abc&quot;)        = &quot;abc&quot;
	 * StringUtil.trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
	 * StringUtil.trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * StringUtil.trimStart(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimStart(String str) {
		return trim(str, null, -1);
	}

	/**
	 * 除去字符串头部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.trimStart(null, *)          = null
	 * StringUtil.trimStart(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trimStart(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
	 * StringUtil.trimStart(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trimStart(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trimStart(&quot;abc  &quot;, null)    = &quot;abc  &quot;
	 * StringUtil.trimStart(&quot; abc &quot;, null)    = &quot;abc &quot;
	 * StringUtil.trimStart(&quot;yxabc  &quot;, &quot;xyz&quot;) = &quot;abc  &quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trimStart(String str, String stripChars) {
		return trim(str, stripChars, -1);
	}

	/**
	 * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trimEnd(null)       = null
	 * StringUtil.trimEnd(&quot;&quot;)         = &quot;&quot;
	 * StringUtil.trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
	 * StringUtil.trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * StringUtil.trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
	 * StringUtil.trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimEnd(String str) {
		return trim(str, null, 1);
	}

	/**
	 * 除去字符串尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.trimEnd(null, *)          = null
	 * StringUtil.trimEnd(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trimEnd(&quot;abc&quot;, &quot;&quot;)        = &quot;abc&quot;
	 * StringUtil.trimEnd(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trimEnd(&quot;  abc&quot;, null)    = &quot;  abc&quot;
	 * StringUtil.trimEnd(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trimEnd(&quot; abc &quot;, null)    = &quot; abc&quot;
	 * StringUtil.trimEnd(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trimEnd(String str, String stripChars) {
		return trim(str, stripChars, 1);
	}

	/**
	 * 除去字符串头尾部的空白，如果结果字符串是空字符串<code>""</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trimToNull(null)          = null
	 * StringUtil.trimToNull(&quot;&quot;)            = null
	 * StringUtil.trimToNull(&quot;     &quot;)       = null
	 * StringUtil.trimToNull(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trimToNull(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimToNull(String str) {
		return trimToNull(str, null);
	}

	/**
	 * 除去字符串头尾部的空白，如果结果字符串是空字符串<code>""</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trim(null, *)          = null
	 * StringUtil.trim(&quot;&quot;, *)            = null
	 * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimToNull(String str, String stripChars) {
		String result = trim(str, stripChars);

		if ((result == null) || (result.length() == 0)) {
			return null;
		}

		return result;
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是<code>null</code>，则返回空字符串<code>""</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trimToEmpty(null)          = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;&quot;)            = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;     &quot;)       = &quot;&quot;
	 * StringUtil.trimToEmpty(&quot;abc&quot;)         = &quot;abc&quot;
	 * StringUtil.trimToEmpty(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimToEmpty(String str) {
		return trimToEmpty(str, null);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是<code>null</code>，则返回空字符串<code>""</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>Character.isWhitespace</code>
	 * 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * StringUtil.trim(null, *)          = &quot;&quot;
	 * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回
	 *         <code>null</code>
	 */
	public static String trimToEmpty(String str, String stripChars) {
		String result = trim(str, stripChars);

		if (result == null) {
			return EMPTY_STRING;
		}

		return result;
	}

	/**
	 * 除去字符串头尾部的指定字符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.trim(null, *)          = null
	 * StringUtil.trim(&quot;&quot;, *)            = &quot;&quot;
	 * StringUtil.trim(&quot;abc&quot;, null)      = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abc&quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;abc  &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot; abc &quot;, null)    = &quot;abc&quot;
	 * StringUtil.trim(&quot;  abcyx&quot;, &quot;xyz&quot;) = &quot;  abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * @param stripChars 要除去的字符，如果为<code>null</code>表示除去空白字符
	 * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部，
	 *            <code>1</code>表示trimEnd
	 * 
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	private static String trim(String str, String stripChars, int mode) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		int start = 0;
		int end = length;

		// 扫描字符串头部
		if (mode <= 0) {
			if (stripChars == null) {
				while ((start < end) && (Character.isWhitespace(str.charAt(start)))) {
					start++;
				}
			} else if (stripChars.length() == 0) {
				return str;
			} else {
				while ((start < end) && (stripChars.indexOf(str.charAt(start)) != -1)) {
					start++;
				}
			}
		}

		// 扫描字符串尾部
		if (mode >= 0) {
			if (stripChars == null) {
				while ((start < end) && (Character.isWhitespace(str.charAt(end - 1)))) {
					end--;
				}
			} else if (stripChars.length() == 0) {
				return str;
			} else {
				while ((start < end) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
					end--;
				}
			}
		}

		if ((start > 0) || (end < length)) {
			return str.substring(start, end);
		}

		return str;
	}

	/**
	 * Check if a String has length.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength(&quot;&quot;) = false
	 * StringUtils.hasLength(&quot; &quot;) = true
	 * StringUtils.hasLength(&quot;Hello&quot;) = true
	 * </pre>
	 * 
	 * @param str the String to check, may be <code>null</code>
	 * @return <code>true</code> if the String is not null and has length
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check if a String has text. More specifically, returns <code>true</code>
	 * if the string not <code>null<code>, it's <code>length is > 0</code>, and it has at least one
	 * non-whitespace character.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText(&quot;&quot;) = false
	 * StringUtils.hasText(&quot; &quot;) = false
	 * StringUtils.hasText(&quot;12345&quot;) = true
	 * StringUtils.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str the String to check, may be <code>null</code>
	 * @return <code>true</code> if the String is not null, length > 0, and not
	 *         whitespace only
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if a String has text. More specifically, returns <code>true</code>
	 * if the string not <code>null<code>, it's <code>length is > 0</code>, and it has at least one
	 * non-whitespace character.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText(&quot;&quot;) = false
	 * StringUtils.hasText(&quot; &quot;) = false
	 * StringUtils.hasText(&quot;12345&quot;) = true
	 * StringUtils.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str the String to check, may be <code>null</code>
	 * @return <code>true</code> if the String is not null, length > 0, and not
	 *         whitespace only
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(StringBuffer str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 * 
	 * @param str CSV list
	 * @return an array of Strings, or the empty array if s is null
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still
	 * be considered as single delimiter string, rather than as bunch of
	 * potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 * 
	 * @param str the input String
	 * @param delimiter the delimiter between elements (this is a single
	 *            delimiter, rather than a bunch individual delimiter
	 *            characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}

		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(str.substring(i, i + 1));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(str.substring(pos));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Copy the given Collection into a String array. The Collection must
	 * contain String elements only.
	 * 
	 * @param collection the Collection to copy
	 * @return the String array (<code>null</code> if the Collection was
	 *         <code>null</code> as well)
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	private static final char CA[];

	private static final int IA[];

	private static final BitSet allowed_query;

	static {
		CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
		IA = new int[256];
		Arrays.fill(IA, -1);
		int i = 0;
		for (int iS = CA.length; i < iS; i++)
			IA[CA[i]] = i;

		IA[61] = 0;
		allowed_query = new BitSet(256);
		for (i = 48; i <= 57; i++)
			allowed_query.set(i);

		for (i = 97; i <= 122; i++)
			allowed_query.set(i);

		for (i = 65; i <= 90; i++)
			allowed_query.set(i);

		allowed_query.set(45);
		allowed_query.set(95);
		allowed_query.set(46);
		allowed_query.set(33);
		allowed_query.set(126);
		allowed_query.set(42);
		allowed_query.set(39);
		allowed_query.set(40);
		allowed_query.set(41);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 * @author Taylor
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	/**
	 * 判断字符串是否为不为空
	 * 
	 * @param s
	 * @return
	 * @author Taylor
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && s.trim().length() != 0;
	}

	/**
	 * 过滤所有的HTML标签
	 * 
	 * @param str 含HTML的原始字符串
	 * @return 返回去掉标签之后的文本
	 * @author Taylor
	 */
	public static String filterHtml(String str) {
		if (str == null) {
			return null;
		}

		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(str);
			str = m_html.replaceAll(""); // 过滤html标签

		} catch (Exception e) {
			logger.error("过滤HTML出错", e);
		}

		return str;// 返回文本字符串
	}

	/**
	 * 去掉危险的HTML标签(style,script)
	 * 
	 * @param str
	 * @return
	 * @author Taylor
	 */
	public static String filterDangerHtml(String str) {
		if (str == null) {
			return null;
		}

		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;

		String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式
		String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式

		p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		m_script = p_script.matcher(str);
		str = m_script.replaceAll(""); // 过滤script标签

		p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		m_style = p_style.matcher(str);
		str = m_style.replaceAll(""); // 过滤style标签

		return str;
	}

	/**
	 * 转义所有"<"和">"符号
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str) {
		if (str == null) {
			return "";
		}
		str = str.replace(">", "&gt;");
		str = str.replace("<", "&lt;");
		return str;
	}

	/**
	 * 将字符串截取一定的长度,末尾用...补全
	 * 
	 * @param s
	 * @param byteLength
	 * @return
	 */
	public static String limitString(String s, int byteLength) {
		return limitString(s, byteLength, "...");
	}

	/**
	 * 将字符串截取一定的长度,末尾用omit补全
	 * 
	 * @param s
	 * @param byteLength
	 * @param omit
	 * @return
	 * @author Taylor
	 */
	public static String limitString(String s, int byteLength, String omit) {
		if (s == null) {
			return null;
		}
		if (byteLength <= 0) {
			return "";
		}
		if (s.getBytes().length <= byteLength) {
			return s;
		}
		String r = "";
		for (int i = 0; i < s.length(); i++) {
			String tmp = s.substring(i, i + 1);
			if (r.getBytes().length + tmp.getBytes().length > byteLength) {
				break;
			}
			r += tmp;
		}
		if (omit != null) {
			r += omit;
		}
		return r;
	}

	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			char c = AS.charAt((int) (Math.random() * (AS.length())));
			sb.append(c);
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 将字符串补齐至一定长度,不足部分用omit补齐
	 * 
	 * @param s
	 * @param byteLength
	 * @param omit
	 * @return
	 * @author Taylor
	 */
	public static String toSize(String s, int byteLength, String omit) {
		if (byteLength <= 0) {
			return "";
		}
		if (s == null) {
			s = "";
		}
		if (omit == null) {
			omit = "...";
		}
		int omitSize = omit.getBytes().length;

		if (s.getBytes().length > byteLength) {
			if (byteLength < omitSize) {
				s = limitString(s, byteLength);
			} else {
				s = limitString(s, byteLength - omitSize, omit);
			}
		}
		while (s.getBytes().length + omitSize <= byteLength) {
			s += omit;
		}
		return s;
	}

	/**
	 * 将字符串的首字母大写.
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		String firstLetter = str.substring(0, 1);
		String result = firstLetter.toUpperCase() + str.substring(1);
		return result;
	}

	/**
	 * 判断某字符串是否都在ascii的范围内
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isAsciiStr(String str) {
		if (str == null) {
			return false;
		}

		int length = str.length();

		for (int i = 0; i < length; i++) {
			if (str.charAt(i) > 255) {
				return false;
			}
		}

		return true;
	}

    /**
     * 判断某字符串是否数值型
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

	/**
	 * 将字符串转换成小写。
	 * 
	 * <p>
	 * 如果字符串是<code>null</code>则返回<code>null</code>。
	 * 
	 * <pre>
	 * StringUtil.toLowerCase(null)  = null
	 * StringUtil.toLowerCase(&quot;&quot;)    = &quot;&quot;
	 * StringUtil.toLowerCase(&quot;aBc&quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要转换的字符串
	 * 
	 * @return 大写字符串，如果原字符串为<code>null</code>，则返回<code>null</code>
	 */
	public static String toLowerCase(String str) {
		if (str == null) {
			return null;
		}

		return str.toLowerCase();
	}

	/**
	 * 取得指定子串在字符串中出现的次数。
	 * 
	 * <p>
	 * 如果字符串为<code>null</code>或空，则返回<code>0</code>。
	 * 
	 * <pre>
	 * StringUtil.countMatches(null, *)       = 0
	 * StringUtil.countMatches(&quot;&quot;, *)         = 0
	 * StringUtil.countMatches(&quot;abba&quot;, null)  = 0
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;&quot;)    = 0
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;a&quot;)   = 2
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;ab&quot;)  = 1
	 * StringUtil.countMatches(&quot;abba&quot;, &quot;xxx&quot;) = 0
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param str 要扫描的字符串
	 * @param subStr 子字符串
	 * 
	 * @return 子串在字符串中出现的次数，如果字符串为<code>null</code>或空，则返回<code>0</code>
	 */
	public static int countMatches(String str, String subStr) {
		if ((str == null) || (str.length() == 0) || (subStr == null) || (subStr.length() == 0)) {
			return 0;
		}

		int count = 0;
		int index = 0;

		while ((index = str.indexOf(subStr, index)) != -1) {
			count++;
			index += subStr.length();
		}

		return count;
	}
	
	public static String readLine(InputStreamReader inputReader) throws IOException {
	    StringBuffer sb = new StringBuffer();
	    
        char c;
        int n;
        int num = 0;
        while((n = inputReader.read()) > 0) {
            num ++;
            c = (char) n;
            if(c == '\n' || c == '\r') {
                break;
            }
            sb.append(c);
        }
        if(num == 0) {
            return null;
        }
        
	    return sb.toString();
	}
	
	/**
	 * 校验输入值是否为合法字符或者数字
	 * 
	 * @param param
	 * @return
	 */
	public static boolean checkValidatorString(String param) {
		String character = "([a-zA-Z0-9_-])+";
		return param.matches(character);
	}

    public static String normalize(String s) {
//        s = HtmlDecoder.decode(s);

        int len = s.length();
        StringBuffer sb = new StringBuffer(len);

        boolean blankSkip = true;
        boolean crlfSkip = true;
        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if (StringUtil.isBlank(c)) {
                if (!blankSkip) {
                    sb.append(c);
                    blankSkip = true;
                }
            } else if (StringUtil.isCrlf(c)) {
                if (!crlfSkip) {
                    sb.append(c);
                    crlfSkip = true;
                    blankSkip = true;
                }
            } else {
                sb.append(c);
                blankSkip = false;
                crlfSkip = false;
            }
        }
        return sb.toString().trim();
    }
}