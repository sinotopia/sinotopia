package com.sinotopia.fundamental.parser.jsoup;

import com.sinotopia.fundamental.common.utils.StrUtils;

/**
 * 扩展的css query
 * Created by zhoubing on 2016/11/29.
 */
public class CssSelector {
    private String cssQuery;
    private boolean isRelative = false;
    private String method;
    private String parameter;

    public static final String METHOD_TEXT = ".text()";
    public static final String METHOD_HTML = ".html()";
    public static final String METHOD_OUTER_HTML = ".outerHtml()";
    public static final String METHOD_OWN_TEXT = ".ownText()";
    public static final String METHOD_VAL = ".val()";

    private static final String METHOD_ATTR_PREFIX = ".attr(";
    private static final String METHOD_ATTR_SUFFIX = ")";
    public static final String METHOD_ATTR = METHOD_ATTR_PREFIX+METHOD_ATTR_SUFFIX;

    private static final String RELATIVE_PREFIX = " ";//相对路径的css查询器 前面使用空格作为标志

    public CssSelector reset() {
        //复用对象
        this.cssQuery = null;
        this.isRelative = false;
        this.method = null;
        this.parameter = null;
        return this;
    }



    public void parse(String selector) {
        reset();

        setRelative(selector.startsWith(RELATIVE_PREFIX));
        //.text()
        //.attr("href")
        //.html()
        int index = 0;
        index = selector.indexOf(METHOD_TEXT);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_TEXT);
            return;
        }

        index = selector.indexOf(METHOD_HTML);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_HTML);
            return;
        }

        index = selector.indexOf(METHOD_ATTR_PREFIX);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_ATTR);
            setParameter(getParameterFromSelector(selector));
            return;
        }

        index = selector.indexOf(METHOD_OUTER_HTML);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_OUTER_HTML);
            return;
        }

        index = selector.indexOf(METHOD_OWN_TEXT);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_OWN_TEXT);
            return;
        }

        index = selector.indexOf(METHOD_VAL);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_VAL);
            return;
        }

        setCssQuery(selector);
    }

    private static String getParameterFromSelector(String text) {
        return StrUtils.trimQuotation(StrUtils.getMiddleText(text, METHOD_ATTR_PREFIX, METHOD_ATTR_SUFFIX));
    }

    public boolean isTextMethod() {
        return METHOD_TEXT.equals(this.method);
    }
    public boolean isHtmlMethod() {
        return METHOD_HTML.equals(this.method);
    }
    public boolean isAttrMethod() {
        return METHOD_ATTR.equals(this.method);
    }
    public boolean isOuterHtmlMethod() {
        return METHOD_OUTER_HTML.equals(this.method);
    }
    public boolean isOwnTextMethod() {
        return METHOD_OWN_TEXT.equals(this.method);
    }
    public boolean isValMethod() {
        return METHOD_VAL.equals(this.method);
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public void setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
    }

    public boolean isRelative() {
        return isRelative;
    }

    public void setRelative(boolean isRelative) {
        this.isRelative = isRelative;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
