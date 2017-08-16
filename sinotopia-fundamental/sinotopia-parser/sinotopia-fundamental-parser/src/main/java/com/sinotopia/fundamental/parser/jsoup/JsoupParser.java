package com.sinotopia.fundamental.parser.jsoup;

import com.alibaba.fastjson.util.TypeUtils;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.parser.Parser;
import com.sinotopia.fundamental.parser.define.FieldDefine;
import com.sinotopia.fundamental.parser.define.FieldTypeEnum;
import com.sinotopia.fundamental.parser.regex.RegexSelector;
import com.sinotopia.fundamental.parser.segment.SegmentSelector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Jsoup或正则表达式解析html类型数据的解析器
 * Created by zhoubing on 2016/11/29.
 */
public class JsoupParser extends Parser {
    public void parseData(String html, FieldDefine fieldDefine, Map result) {
        parseHtml(Jsoup.parse(html), fieldDefine, result);
    }

    public void parseHtml(Document document, FieldDefine fieldDefine, Map result) {
        parseHtml(document, document, fieldDefine, result);
    }

    private void parseHtml(Document document, Element element, FieldDefine fieldDefine, Map result) {
        FieldDefine[] defines = fieldDefine.getDefines();
        if (defines == null || defines.length == 0) {
            return;
        }

        CssSelector cssSelector = new CssSelector();//复用对象
        for (FieldDefine define : defines) {
            String type = define.getType();
            String selector = define.getSelector();

            if (StrUtils.isEmpty(selector)) {
                if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                    Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                    parseHtml(document, document, define, child);
                    result.put(define.getName(), child);
                    continue;
                }
                if (define.getProcessor() != null) {
                    Object value = define.getProcessor().process(document.html());
                    result.put(define.getName(), TypeUtils.cast(value, typeToClass(define.getType()), null));
                    continue;
                }

                throw new IllegalArgumentException("unhandled html parser define type ["+type+"] when selector is empty.");
            }

            if (RegexSelector.isRegexSelector(selector)) {
                List<String> list = RegexSelector.matcher(document.html(), element.html(), selector);
                result.put(define.getName(), getValue(list, define));
                continue;
            }
            if (SegmentSelector.isSegmentSelector(selector)) {
                String value = SegmentSelector.matcher(document.html(), element.html(), selector);
                result.put(define.getName(), castValue(define, value));
                continue;
            }

            cssSelector.parse(selector);
            Elements elements = null;
            if (cssSelector.isRelative()) {
                elements = element.select(cssSelector.getCssQuery());
            }
            else {
                elements = document.select(cssSelector.getCssQuery());
            }

            if (elements == null || elements.isEmpty()) {
                continue;
            }

            if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                parseHtml(document, elements.first(), define, child);
                result.put(define.getName(), child);
                continue;
            }

            if (FieldTypeEnum.Array.isEqual(type) || FieldTypeEnum.List.isEqual(type)) {
                int size = elements.size();
                Object[] array = result.containsKey(define.getName()) ? (Object[]) result.get(define.getName()) : new Object[size];
                int min = Math.min(size, array.length);
                for (int i = 0; i < min; i++) {
                    Map item = array[i] != null ? (Map) array[i] : new HashMap();
                    parseHtml(document, elements.get(i), define.firstDefine(), item);
                    array[i] = item;
                }

                result.put(define.getName(), array);
                continue;
            }

            result.put(define.getName(), getElementValue(elements, cssSelector, define));
        }
    }

    private Object getElementValue(Elements elements, CssSelector cssSelector, FieldDefine define) {
        return castValue(define, getValueText(elements, cssSelector));
    }

    private Object getValueText(Elements elements, CssSelector cssSelector) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        Element element = elements.get(0);

        if (cssSelector.isTextMethod()) {
            return StrUtils.trim(element.text());
        }
        if (cssSelector.isValMethod()) {
            return StrUtils.trim(element.val());
        }
        if (cssSelector.isAttrMethod()) {
            return StrUtils.trim(element.attr(cssSelector.getParameter()));
        }
        if (cssSelector.isOuterHtmlMethod()) {
            return StrUtils.trim(element.outerHtml());
        }
        if (cssSelector.isOwnTextMethod()) {
            return StrUtils.trim(element.ownText());
        }
        if (cssSelector.isHtmlMethod()) {
            return StrUtils.trim(element.html());
        }
        return StrUtils.trim(element.text());
    }
}
