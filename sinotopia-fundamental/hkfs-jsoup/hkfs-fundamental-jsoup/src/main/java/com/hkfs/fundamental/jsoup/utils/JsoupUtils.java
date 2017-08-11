package com.hkfs.fundamental.jsoup.utils;

import com.hkfs.fundamental.common.utils.StrUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;


/**
 * Jsoup操作工具类
 * Created by zhoubing on 2016/5/7.
 */
public class JsoupUtils {
    public static final String FORM_ACTION = "form_action";
    /**
     * 从jsoup节点中获取表单参数map
     * @param element
     * @return
     */
    public static Map<String, String> getParamsMap(Element element){
        return getParamsMap(element, "form");
    }

    /**
     * 从jsoup节点中获取表单参数map
     * @param element
     * @param formCssQuery form表单的选择器
     * @return
     */
    public static Map<String, String> getParamsMap(Element element, String formCssQuery) {
        return getParamsMap(element, formCssQuery, null);
    }

    /**
     * 从jsoup节点中获取表单参数map
     * @param html
     * @param formCssQuery form表单的选择器
     * @param baseUri 网站URL
     * @return
     */
    public static Map<String, String> getParamsMap(String html, String formCssQuery, String baseUri) {
        return getParamsMap(Jsoup.parse(html), formCssQuery, baseUri);
    }

    /**
     * 从jsoup节点中获取表单参数map
     * @param element
     * @param formCssQuery form表单的选择器
     * @param baseUri 网站URL
     * @return
     */
    public static Map<String, String> getParamsMap(Element element, String formCssQuery, String baseUri) {
        Element form = element.select(formCssQuery).first();
        if(null == form) return null;
        Elements elements = form.getElementsByTag("input");
        Map<String, String> params = new HashMap<String, String>();
        for(Element eachElement : elements){
            if(StrUtils.notEqualIgnoreCase(eachElement.attr("type"), "button")
                    && StrUtils.notEqualIgnoreCase(eachElement.attr(eachElement.attr("type")), "submit")) {
                params.put(eachElement.attr("name"), eachElement.attr("value"));
            }
        }

        if (StrUtils.notEmpty(baseUri)) {
            form.setBaseUri(baseUri);
        }
        String action = form.absUrl("action");
        if (StrUtils.notEmpty(action)) {
            params.put(FORM_ACTION, action);
        }
        return params;
    }



    /**
     * 从HTML中获取表单参数map
     * @param html 网页数据
     * @param formCssQuery form表单的选择器
     * @return
     */
    public static Map<String, String> getParamsMap(String html, String formCssQuery) {
        return JsoupUtils.getParamsMap(Jsoup.parse(html), formCssQuery);
    }

    /**
     * 获取指定cssQuery的节点的text()
     * @param element
     * @param cssQuery
     * @return
     */
    public static String getText(Element element, String cssQuery) {
        Element target = getElement(element, cssQuery);
        if (target != null) {
            return StrUtils.trim(target.text());
        }
        return null;
    }


    /**
     * 获取某元素中的数组中某个元素的text
     * @param element
     * @param index
     * @return
     */
    public static String getText(Element element, String cssQuery, int index) {
        Elements target =  null;
        if (element != null && cssQuery != null) {
            target = element.select(cssQuery);
        }
        if (target != null) {
            return getText(target, index);
        }
        return null;
    }


    /**
     * 获取指定cssQuery的节点的text()
     * @param text
     * @param cssQuery
     * @return
     */
    public static String getText(String text, String cssQuery) {
        return getText(Jsoup.parse(text), cssQuery);
    }

    /**
     * 获取数组中某个元素的text
     * @param elements
     * @param index
     * @return
     */
    public static String getText(Elements elements, int index) {
        if (elements != null && index < elements.size()) {
            return StrUtils.trim(elements.get(index).text());
        }
        return null;
    }

    /**
     * 获取指定选择器的节点
     * @param element
     * @param cssQuery
     * @return
     */
    public static Element getElement(Element element, String cssQuery) {
        if (element != null && cssQuery != null) {
            return element.select(cssQuery).first();
        }
        return null;
    }

    /**
     * 获取指定选择器的节点
     * @param element
     * @param cssQuery
     * @return
     */
    public static Elements getElements(Element element, String cssQuery) {
        if (element != null && cssQuery != null) {
            return element.select(cssQuery);
        }
        return null;
    }

    /**
     * 获取指定选择器的节点
     * @param html
     * @param cssQuery
     * @return
     */
    public static Element getElement(String html, String cssQuery) {
        if (html != null && cssQuery != null) {
            return Jsoup.parse(html).select(cssQuery).first();
        }
        return null;
    }

    /**
     * 获取指定选择器的节点
     * @param html
     * @param cssQuery
     * @return
     */
    public static Elements getElements(String html, String cssQuery) {
        if (html != null && cssQuery != null) {
            return Jsoup.parse(html).select(cssQuery);
        }
        return null;
    }

    /**
     * 获取指定选择器的节点的val
     * @param element
     * @param cssQuery
     * @return
     */
    public static String getVal(Element element, String cssQuery) {
        Element target = getElement(element, cssQuery);
        if (target != null) {
            return StrUtils.trim(target.val());
        }
        return null;
    }

    /**
     * 获取指定选择器的节点的指定属性的attr
     * @param element
     * @param cssQuery
     * @param attrName
     * @return
     */
    public static String getAttr(Element element, String cssQuery, String attrName) {
        Element target = getElement(element, cssQuery);
        if (target != null) {
            return StrUtils.trim(target.attr(attrName));
        }
        return null;
    }

    /**
     * 获取指定选择器的节点的指定属性的绝对URL
     * @param element
     * @param cssQuery
     * @param attrName
     * @param baseUri
     * @return
     */
    public static String getAbsUrl(Element element, String cssQuery, String attrName, String baseUri) {
        Element target = getElement(element, cssQuery);
        if (target != null) {
            if (StrUtils.notEmpty(baseUri)) {
                target.setBaseUri(baseUri);
            }
            return StrUtils.trim(target.absUrl(attrName));
        }
        return null;
    }

    /**
     * 获取指定选择器的节点的指定属性的绝对URL
     * @param element
     * @param cssQuery
     * @param attrName
     * @return
     */
    public static String getAbsUrl(Element element, String cssQuery, String attrName) {
        return getAbsUrl(element, cssQuery, attrName, null);
    }

    /**
     * 获取指定选择器的节点的html
     * @param element
     * @param cssQuery
     * @return
     */
    public static String getHtml(Element element, String cssQuery) {
        Element target = getElement(element, cssQuery);
        if (target != null) {
            return StrUtils.trim(target.html());
        }
        return null;
    }

    /**
     * 判断元素是否是disabled
     * @param element
     * @return
     */
    public static boolean isDisabled(Element element) {
        String html = element.toString();
        if (!html.contains("disabled")) {
            return false;
        }
        return true;
    }

    /**
     * 替换掉字符串中所有的&nbsp;
     * @param text
     * @return
     */
    public static String replaceNbsp(String text) {
        return text != null ? text.replace("&nbsp;", "") : text;
    }

    /**
     * 替换字符串中所有的空格
     * @param text
     * @return
     */
    public static String replaceWhiteSpace(String text) {
        return text != null ? text.replace(" ", "").replace("\\u00A0", "").replace(" ", "") : text;
    }
    /**
     * 替换字符串中所有的特殊空格
     * @param text
     * @return
     */
    public static String replaceWhiteSpaceU00A0(String text) {
        return text != null ? text.replace("\\u00A0", "").replace(" ", "") : text;//这里的空格是\u00A0的空格 不是一般的空格
    }

    /**
     * 替换字符串中的\n
     * @param text
     * @return
     */
    public static String replaceLineText(String text) {
        return text != null ? text.replace("\\n", "") : text;
    }

    /**
     * 去除字符串两端的空格跟引号，将&nbsp;替换
     * @param text
     * @return
     */
    public static String formatAll(String text) {
        return StrUtils.trimQuotation(replaceNbsp(text));
    }

    /**
     * 获取table下的tr中包含关键字的某个td的text
     * @param tableElement table的element
     * @param keyword tr包含关键字
     * @param tdIndex td的索引
     * @return
     */
    public static String getTableTdText(Element tableElement, String keyword, int tdIndex) {
        Elements elements = JsoupUtils.getElements(tableElement, "tr:contains("+keyword+") td");
        return JsoupUtils.getText(elements, tdIndex);
    }

}
