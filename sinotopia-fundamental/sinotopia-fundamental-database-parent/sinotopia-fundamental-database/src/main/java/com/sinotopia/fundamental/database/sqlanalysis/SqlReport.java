package com.sinotopia.fundamental.database.sqlanalysis;

import java.util.List;

/**
 * Sql报告
 */
public class SqlReport {

    private String templateSql;
    private String executableSql;
    private Integer queryCount;
    private List<Explain> explainList;

    public String getTemplateSql() {
        return templateSql;
    }

    public void setTemplateSql(String templateSql) {
        this.templateSql = templateSql;
    }

    public String getExecutableSql() {
        return executableSql;
    }

    public void setExecutableSql(String executableSql) {
        this.executableSql = executableSql;
    }

    public Integer getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(Integer queryCount) {
        this.queryCount = queryCount;
    }

    public List<Explain> getExplainList() {
        return explainList;
    }

    public void setExplainList(List<Explain> explainList) {
        this.explainList = explainList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QueryCount:");
        sb.append(queryCount);
        sb.append(" TemplateSql:");
        sb.append(templateSql);
        sb.append(" ExecutableSql:");
        sb.append(executableSql);
        if (explainList != null) {
            for (Explain explain : explainList) {
                sb.append("\nExplain:" + explain);
            }
        }
        return sb.toString();
    }
}
