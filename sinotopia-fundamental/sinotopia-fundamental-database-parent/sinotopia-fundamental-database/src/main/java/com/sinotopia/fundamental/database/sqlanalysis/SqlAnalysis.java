package com.sinotopia.fundamental.database.sqlanalysis;

import com.sinotopia.fundamental.common.assist.file.FileLineReader;

import java.util.*;

/**
 * Sql日志分析
 */
public class SqlAnalysis {

    private String url;
    private String username;
    private String password;
    private ExtractHandler extractHandler;
    private Comparator<SqlReport> sortComparator;

    public static void main(String[] args) {
        SqlAnalysis.Builder builder = new SqlAnalysis.Builder();
        builder.setUrl("jdbc:mysql://192.168.7.43:3306/yunying_v1.2?characterEncoding=UTF-8");
        builder.setUsername("root");
        builder.setPassword("123456");
//        builder.setExtractHandler();
        builder.setSortComparator(new Comparator<SqlReport>() {
            @Override
            public int compare(SqlReport o1, SqlReport o2) {
                return o2.getQueryCount().compareTo(o1.getQueryCount());
            }
        });

        SqlAnalysis sqlAnalysis = builder.build();
        List<SqlReport> reportList = sqlAnalysis.process("F:\\temp\\out.nohup");
        for (SqlReport report : reportList) {
            System.out.println(report);
            System.out.println();
        }
    }

    public SqlAnalysis() {
    }

    public SqlAnalysis(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public List<SqlReport> process(String filePath) {
        Map<String, Map<String, Object>> sqlMap = new LinkedHashMap<String, Map<String, Object>>();
        ExtractHandler handler = extractHandler != null ? extractHandler : new BasicExtractHandler();
        FileLineReader reader = new FileLineReader(filePath, new SqlFileLineProcessor(sqlMap, handler));
        //开始读取处理日志文件
        reader.read();

        List<SqlReport> reportList = new ArrayList<SqlReport>(sqlMap.size());

        SqlExplain sqlExplain = new SqlExplain(url, username, password);

        SqlReport report = null;
        Iterator<Map.Entry<String, Map<String, Object>>> it = sqlMap.entrySet().iterator();
        Map.Entry<String, Map<String, Object>> entry = null;
        Map<String, Object> value = null;
        while (it.hasNext()) {
            entry = it.next();
            value = entry.getValue();
            String templateSql = entry.getKey();
            String executableSql = (String) value.get("executableSql");
            Integer queryCount = (Integer) value.get("queryCount");

            report = new SqlReport();
            report.setExecutableSql(executableSql);
            report.setTemplateSql(templateSql);
            report.setQueryCount(queryCount);

            List<Explain> explainList = sqlExplain.explain(executableSql);
            report.setExplainList(explainList);
            reportList.add(report);
        }

        sqlExplain.destroy();

        if (sortComparator != null) {
            Collections.sort(reportList, sortComparator);
        }
        return reportList;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setExtractHandler(ExtractHandler extractHandler) {
        this.extractHandler = extractHandler;
    }

    public void setSortComparator(Comparator<SqlReport> sortComparator) {
        this.sortComparator = sortComparator;
    }

    public static class Builder {
        private String url;
        private String username;
        private String password;
        private ExtractHandler extractHandler;
        private Comparator<SqlReport> sortComparator;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setExtractHandler(ExtractHandler extractHandler) {
            this.extractHandler = extractHandler;
            return this;
        }

        public Builder setSortComparator(Comparator<SqlReport> sortComparator) {
            this.sortComparator = sortComparator;
            return this;
        }

        public SqlAnalysis build() {
            SqlAnalysis sqlAnalysis = new SqlAnalysis(url, username, password);
            sqlAnalysis.extractHandler = extractHandler;
            sqlAnalysis.sortComparator = sortComparator;
            return sqlAnalysis;
        }
    }
}