package com.hkfs.fundamental.database.sqlanalysis;

import com.hkfs.fundamental.common.assist.file.BasicFileLineProcessor;
import com.hkfs.fundamental.common.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志文件处理器
 * Created by zhoubing on 2016/12/15.
 */
public class SqlFileLineProcessor extends BasicFileLineProcessor {
    private Map<String, Map<String, Object>> sqlMap;
    private ExtractHandler extractHandler;

    public SqlFileLineProcessor(Map<String, Map<String, Object>> sqlMap, ExtractHandler extractHandler) {
        this.sqlMap = sqlMap;
        this.extractHandler = extractHandler;
    }

    private String lastSql = null;

    @Override
    public boolean process(String line) {
        String sql = extractHandler.getPreparing(line);
        if (StrUtils.notEmpty(sql)) {
            Map<String, Object> content = sqlMap.get(sql);
            if (content == null) {
                content = new HashMap<String, Object>(2);
                content.put("queryCount", 1);
                lastSql = sql;
                sqlMap.put(sql, content);
            } else {
                Integer count = (Integer) content.get("queryCount");
                content.put("queryCount", count + 1);
            }
            return true;
        }

        if (lastSql != null) {
            Map<String, Object> content = sqlMap.get(lastSql);
            if (!lastSql.contains("?")) {
                content.put("executableSql", lastSql);
                lastSql = null;
                return true;
            }

            String parameterText = extractHandler.getParameters(line);
            if (StrUtils.isEmpty(parameterText)) {
                return true;
            }
            List<Parameter> parameters = processParameters(parameterText);
            int parameterCount = StrUtils.getChildTextCount(lastSql, "\\?");
            if (parameters == null || parameters.size() != parameterCount) {
                //参数与sql不匹配
                return true;
            }

            String newSql = processSql(lastSql, parameters);
            content.put("executableSql", newSql);
            lastSql = null;
        }

        return true;
    }

    private String processSql(String sql, List<Parameter> parameters) {
        for (Parameter parameter : parameters) {
            sql = sql.replaceFirst("\\?", parameter.getSqlValue());
        }
        return sql;
    }

    private List<Parameter> processParameters(String parameterText) {
        if (!parameterText.contains("(") || !parameterText.contains(")")) {
            return null;
        }

        String[] array = parameterText.split(", ");
        List<Parameter> list = new ArrayList<Parameter>(array.length);
        for (String str : array) {
            list.add(processParameter(str));
        }
        return list;
    }

    private Parameter processParameter(String str) {
        String type = StrUtils.getMiddleText(str, "(", ")", false, true);
        String value = StrUtils.getMiddleText(str, null, "(", true, false);
        return new Parameter(type, value);
    }
}