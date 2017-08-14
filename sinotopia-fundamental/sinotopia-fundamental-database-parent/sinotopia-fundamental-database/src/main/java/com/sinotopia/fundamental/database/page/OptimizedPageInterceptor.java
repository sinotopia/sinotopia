package com.sinotopia.fundamental.database.page;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对查询进行优化的分页拦截器
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class OptimizedPageInterceptor extends PageInterceptor {
    //用于判断sql语句中是否包含union的SQL关键字的正则
    private static final String UNION_REGEX = "[\\s|\t|\r|\n]+union[\\s|\t\r\n]+";
    //用于替换获取记录总数的正则
    private static final String COUNT_REGEX = "(select)([\\s\\S]+?)(from)([\\s\\S]+)";
    //用于替换排序的正则
    private static final String ORDER_BY_REGEX = "order[\\s\\S]+by([\\s\\S]+)";

    protected Pattern unionPattern = Pattern.compile(UNION_REGEX, Pattern.CASE_INSENSITIVE);
    protected Pattern countPattern = Pattern.compile(COUNT_REGEX, Pattern.CASE_INSENSITIVE);
    protected Pattern orderByPattern = Pattern.compile(ORDER_BY_REGEX, Pattern.CASE_INSENSITIVE);

    @Override
    protected String buildPageSql(String sql, int requestOffset, int requestCount) {
        if (!isUnionSql(sql)) {
            String newSql = generatePageSql(sql, requestOffset, requestCount);
            if (newSql != null) {
                return newSql;
            }
        }
        return super.buildPageSql(sql, requestOffset, requestCount);
    }

    public static void main(String[] args) {
//        String regex = "[\\?\\s,\\d]+";
//        System.out.println("?, ?".matches(regex));
//        System.out.println("?    ".matches(regex));
//        System.out.println("    ".matches(regex));
//        System.out.println("0, 10".matches(regex));
//        System.out.println("10 ".matches(regex));
//        System.out.println("0, ?".matches(regex));
//        System.out.println("?,10".matches(regex));

        String sql1 = " SELECT id from user";
        String sql2 = " SELECT temp.* from (SELECT id from user) unioN\t\n temp order BY id desc";
        String sql3 = "select\n" +
                "        a.id,a. name,if (a.menuId, 1, 2) permissionFlag,a.parentId from\n" +
                "        (select t1.id,t1. name,t2.menuId,t1.parentId from tb_admin_menu t1\n" +
                "        left join (\n" +
                "        select menuId from tb_admin_menu_role where roleCode =  ? \n" +
                "        ) t2 on t2.menuId = t1.id and t1.status = 0\n" +
                "        ) a order \nby a.parentId \nasc ,a.id asc";

        String sql = sql3;

        System.out.println(sql);
        System.out.println(new OptimizedPageInterceptor().generateCountPageSql(sql));
    }

    //处理分页SQL
    protected String generatePageSql(String sql, int requestOffset, int requestCount) {
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append(sql);
        sb.append(" LIMIT ");
        sb.append(requestOffset);
        sb.append(",");
        sb.append(requestCount);
        return sb.toString();
    }

    //判断sql语句中是否包含union的SQL关键字
    protected boolean isUnionSql(String sql) {
        return unionPattern.matcher(sql).find();
    }

    //处理获取记录数的SQL
    protected String generateCountPageSql(String sql) {
        Matcher countMatcher = countPattern.matcher(sql);
        if (countMatcher.find()) {
            StringBuilder sb = new StringBuilder();
            sb.append(countMatcher.group(1));
            sb.append(" COUNT(1) ");
            sb.append(countMatcher.group(3));
            sb.append(countMatcher.group(4));

            //这里只是简单的去除最后的order by语句，如果语句中有多个order by将不适用
            Matcher orderByMatcher = orderByPattern.matcher(sb.toString());
            return orderByMatcher.replaceAll("");
        }
        return null;
    }

    @Override
    protected String buildCountPageSql(String sql) {
        if (!isUnionSql(sql)) {
            String newSql = generateCountPageSql(sql);
            if (newSql != null) {
                return newSql;
            }
        }
        return super.buildCountPageSql(sql);
    }
}
