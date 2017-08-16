package com.sinotopia.fundamental.parser.regex;

import com.alibaba.fastjson.util.TypeUtils;
import com.sinotopia.fundamental.common.utils.StrUtils;
import com.sinotopia.fundamental.parser.Parser;
import com.sinotopia.fundamental.parser.define.FieldDefine;
import com.sinotopia.fundamental.parser.define.FieldTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于正则表达式解析文本类型数据的解析器
 * Created by zhoubing on 2016/11/30.
 */
public class RegexParser extends Parser {
    public void parseData(String html, FieldDefine fieldDefine, Map result) {
        parseRegex(html, html, fieldDefine, result);
    }

    private void parseRegex(String html, String childText, FieldDefine fieldDefine, Map result) {
        FieldDefine[] defines = fieldDefine.getDefines();
        if (defines == null || defines.length == 0) {
            return;
        }

        for (FieldDefine define : defines) {
            String type = define.getType();
            String selector = define.getSelector();

            if (StrUtils.isEmpty(selector)) {
                if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                    Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                    parseRegex(html, html, define, child);
                    result.put(define.getName(), child);
                    continue;
                }
                if (define.getProcessor() != null) {
                    Object value = define.getProcessor().process(html);
                    result.put(define.getName(), TypeUtils.cast(value, typeToClass(define.getType()), null));
                    continue;
                }

                throw new IllegalArgumentException("unhandled html parser define type ["+type+"] when selector is empty.");
            }

            List<String> elements = RegexSelector.matcher(html, childText, selector);

            if (elements == null || elements.isEmpty()) {
                continue;
            }

            if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                parseRegex(html, elements.get(0), define, child);
                result.put(define.getName(), child);
                continue;
            }

            if (FieldTypeEnum.Array.isEqual(type) || FieldTypeEnum.List.isEqual(type)) {
                int size = elements.size();
                Object[] array = result.containsKey(define.getName()) ? (Object[]) result.get(define.getName()) : new Object[size];
                int min = Math.min(size, array.length);
                for (int i = 0; i < min; i++) {
                    Map item = array[i] != null ? (Map) array[i] : new HashMap();
                    parseRegex(html, elements.get(i), define.firstDefine(), item);
                    array[i] = item;
                }

                result.put(define.getName(), array);
                continue;
            }

            result.put(define.getName(), getValue(elements, define));
        }
    }
}
