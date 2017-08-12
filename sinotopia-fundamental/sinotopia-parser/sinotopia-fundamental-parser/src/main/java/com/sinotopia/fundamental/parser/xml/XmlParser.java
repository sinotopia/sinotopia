package com.hkfs.fundamental.parser.xml;

import com.alibaba.fastjson.util.TypeUtils;
import com.hkfs.fundamental.common.utils.StrUtils;
import com.hkfs.fundamental.parser.Parser;
import com.hkfs.fundamental.parser.define.FieldDefine;
import com.hkfs.fundamental.parser.define.FieldTypeEnum;
import com.hkfs.fundamental.parser.regex.RegexSelector;
import com.hkfs.fundamental.parser.segment.SegmentSelector;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于xpath解析xml类型数据的解析器
 * Created by zhoubing on 2016/11/28.
 */
public class XmlParser extends Parser {
    public void parseData(String xml, FieldDefine fieldDefine, Map result) {
        parseXml(parseDocument(xml), fieldDefine, result);
    }

    public void parseXml(Document document, FieldDefine fieldDefine, Map result) {
        parseXml(document, document, fieldDefine, result);
    }
    
    public Document parseDocument(String xml) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }
        return document;
    }

    private void parseXml(Document document, Node node, FieldDefine fieldDefine, Map result) {
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
                    parseXml(document, document, define, child);
                    result.put(define.getName(), child);
                    continue;
                }
                if (define.getProcessor() != null) {
                    Object value = define.getProcessor().process(document.asXML());
                    result.put(define.getName(), TypeUtils.cast(value, typeToClass(define.getType()), null));
                    continue;
                }

                throw new IllegalArgumentException("unhandled xml parser define type ["+type+"] when selector is empty.");
            }

            if (RegexSelector.isRegexSelector(selector)) {
                List<String> list = RegexSelector.matcher(document.asXML(), node.asXML(), selector);
                result.put(define.getName(), getValue(list, define));
                continue;
            }
            if (SegmentSelector.isSegmentSelector(selector)) {
                String value = SegmentSelector.matcher(document.asXML(), node.asXML(), selector);
                result.put(define.getName(), castValue(define, value));
                continue;
            }

            List nodes = null;
            if (isRelativeSelector(selector)) {
                nodes = node.selectNodes(selector);
            }
            else if (isAbsoluteSelector(selector)) {
                nodes = document.selectNodes(selector);
            }
            else {
                throw new IllegalArgumentException("unsupported xml selector ["+selector+"].");
            }

            if (nodes == null || nodes.isEmpty()) {
                continue;
            }

            if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                parseXml(document, (Node) nodes.get(0), define, child);
                result.put(define.getName(), child);
                continue;
            }

            if (FieldTypeEnum.Array.isEqual(type) || FieldTypeEnum.List.isEqual(type)) {
                int size = nodes.size();
                Object[] array = result.containsKey(define.getName()) ? (Object[]) result.get(define.getName()) : new Object[size];
                int min = Math.min(size, array.length);
                for (int i = 0; i < min; i++) {
                    Map item = array[i] != null ? (Map) array[i] : new HashMap();
                    parseXml(document, (Node) nodes.get(i), define.firstDefine(), item);
                    array[i] = item;
                }

                result.put(define.getName(), array);
                continue;
            }

            result.put(define.getName(), getXmlValue(nodes, define));
        }
    }

    private Object getXmlValue(List nodes, FieldDefine define) {
        return castValue(define, getXmlValueText(nodes));
    }

    private Object getXmlValueText(List nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }
        Object node = nodes.get(0);
        if (node instanceof DefaultText) {
            return StrUtils.trim(((DefaultText) node).getText());
        }
        if (node instanceof DefaultAttribute) {
            return StrUtils.trim(((DefaultAttribute) node).getText());
        }
        if (node instanceof DefaultElement) {
            return StrUtils.trim(((DefaultElement) node).getText());
        }

        throw new IllegalArgumentException("unsupported node type ["+node+"].");
    }

    private boolean isAbsoluteSelector(String selector) {
        return selector != null && !isRelativeSelector(selector) && selector.startsWith("/");
    }
    private boolean isRelativeSelector(String selector) {
        return selector != null && selector.startsWith(".");
    }
}
