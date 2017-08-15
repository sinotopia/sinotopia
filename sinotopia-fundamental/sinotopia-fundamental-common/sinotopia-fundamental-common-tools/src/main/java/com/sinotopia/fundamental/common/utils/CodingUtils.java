package com.sinotopia.fundamental.common.utils;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodingUtils {

    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);
    public static final String UTF_8 = "UTF-8";
    public static final String ISO_88591 = "ISO-8859-1";
    public static final String GBK = "GBK";
    public static final String GB2312 = "GB2312";

    public static String encodeURL(String url) {
        return encodeURL(url, UTF_8);
    }

    public static String decodeURL(String url) {
        return decodeURL(url, UTF_8);
    }

    public static String encodeURL(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return url;
    }

    public static String decodeURL(String url, String charset) {
        try {
            return URLDecoder.decode(url, charset);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return url;
    }

    public static String iso88591ToUTF8(String str) {
        if (str == null) {
            return "";
        }
        try {
            return StrUtils.trim(new String(str.getBytes(ISO_88591), UTF_8));
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return str;
    }
}
