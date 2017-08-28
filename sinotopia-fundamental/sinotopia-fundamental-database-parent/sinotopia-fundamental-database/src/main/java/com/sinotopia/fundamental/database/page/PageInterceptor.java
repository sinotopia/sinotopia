package com.sinotopia.fundamental.database.page;

import com.sinotopia.fundamental.api.data.PageDataObjectBase;
import com.sinotopia.fundamental.database.utils.DatabaseUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 分页拦截器
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class PageInterceptor implements Interceptor {

    private static final Log logger = LogFactory.getLog(PageInterceptor.class);

    protected static final ThreadLocal<Integer> localPage = new ThreadLocal<Integer>();//用于缓存总记录数
    protected static final Map<String, Boolean> pageMap = new HashMap<String, Boolean>(32);//用于缓存需要和不需要自动分页的方法

    static {
        //默认基类中的方法的分页配置
        pageMap.put("add", false);
        pageMap.put("update", false);
        pageMap.put("get", false);
        pageMap.put("getById", false);
        pageMap.put("query", false);
        pageMap.put("delete", false);
        pageMap.put("count", false);
        pageMap.put("pageQuery", true);
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        if (target instanceof StatementHandler) {
            MetaObject metaStatementHandler = getMetaObject(target);
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                return invocation.proceed();
            }

            Integer requestOffset = null;
            Integer requestCount = null;
            if (parameterObject instanceof PageDataObjectBase) {
                PageDataObjectBase page = (PageDataObjectBase) parameterObject;
                requestOffset = page.getRequestOffset();
                requestCount = page.getRequestCount();
            } else if (parameterObject instanceof Map) {
                Map paramMap = (Map) parameterObject;
                if (paramMap.containsKey("requestOffset")) {
                    requestOffset = (Integer) paramMap.get("requestOffset");
                }
                if (paramMap.containsKey("requestCount")) {
                    requestCount = (Integer) paramMap.get("requestCount");
                }
            }

            if (requestOffset == null || requestCount == null) {
                return invocation.proceed();
            }

            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            if (!isPageStatement(mappedStatement, parameterObject)) {
                return invocation.proceed();
            }

            // 分页参数作为参数对象parameterObject的一个属性
            String sql = boundSql.getSql();
            // 重写sql
            String pageSql = buildPageSql(sql, requestOffset, requestCount);
            // 重写分页sql
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
            // 获取连接参数
            Connection connection = (Connection) invocation.getArgs()[0];
            // 获取总记录数
            int totalCount = getTotalCount(sql, connection, mappedStatement, boundSql);
            // 缓存分页总数
            localPage.set(totalCount);
            // 将执行权交给下一个拦截器
            return invocation.proceed();
        } else if (target instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            //结果非列表，直接返回
            if (!(result instanceof List)) {
                return result;
            }

            //没有获取到前面一步的总记录数，直接返回
            if (localPage.get() == null) {
                return result;
            }

            MappedStatement mappedStatement = (MappedStatement) getMetaObject(target).getValue("mappedStatement");
            if (!isPageStatement(mappedStatement, null)) {
                return result;
            }

            PageList pageList = new PageList();
            pageList.addAll((List) result);
            pageList.setTotalCount(localPage.get());
            localPage.remove();
            return pageList;
        }

        return invocation.proceed();
    }

    protected boolean isPageStatement(MappedStatement mappedStatement, Object parameterObject) throws NoSuchMethodException, ClassNotFoundException {
        String mappedStatementId = mappedStatement.getId();
        String methodName = getMappedMethodName(mappedStatementId);

        //先判断是否是DaoBase或PageDaoBase里面的方法
        Boolean value = pageMap.get(methodName);
        if (value != null) {
            return value;
        }

        //再判断子类的方法
        value = pageMap.get(mappedStatementId);
        if (value == null) {
            if (parameterObject == null) {
                return false;
            }

            String className = getMappedClassName(mappedStatementId);
            Class<?> clazz = Class.forName(className);
            Method[] methods = clazz.getDeclaredMethods();//只获取子类Dao中的方法，如果要全部获取使用getMethods()
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    value = (method.getReturnType() == PageList.class);
                    break;
                }
            }

            value = value != null ? value : false;
            pageMap.put(mappedStatementId, value);
        }
        return value;
    }

    protected String getMappedClassName(String mappedStatementId) {
        return mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
    }

    protected String getMappedMethodName(String mappedStatementId) {
        return mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
    }

    protected MetaObject getMetaObject(Object target) {
        MetaObject metaObject = SystemMetaObject.forObject(target);
        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
        // 可以分离出最原始的的目标类)
        while (metaObject.hasGetter("h")) {
            Object object = metaObject.getValue("h");
            metaObject = SystemMetaObject.forObject(object);
        }
        // 分离最后一个代理对象的目标类
        while (metaObject.hasGetter("target")) {
            Object object = metaObject.getValue("target");
            metaObject = SystemMetaObject.forObject(object);
        }
        return metaObject;
    }

    /**
     * 获取总记录数
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     */
    protected int getTotalCount(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql) {
        // 记录总记录数
        String countSql = buildCountPageSql(sql);
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        int totalCount = 0;
        try {
            countStmt = connection.prepareStatement(countSql);

            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            //需要将metaParameters赋值过去..
            MetaObject countBsObject = SystemMetaObject.forObject(countBS);
            MetaObject boundSqlObject = SystemMetaObject.forObject(boundSql);
            countBsObject.setValue("metaParameters", boundSqlObject.getValue("metaParameters"));

            //处理参数绑定
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
                    boundSql.getParameterObject(), boundSql);
            parameterHandler.setParameters(countStmt);

            //执行查询
            rs = countStmt.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            DatabaseUtils.close(rs);
            DatabaseUtils.close(countStmt);
        }

        return totalCount;
    }

    /**
     * 修改原SQL为分页SQL
     *
     * @param sql
     * @param requestOffset
     * @param requestCount
     * @return
     */
    protected String buildPageSql(String sql, int requestOffset, int requestCount) {
        StringBuilder pageSql = new StringBuilder(2000);
        pageSql.append("SELECT temp.* FROM (");
        pageSql.append(sql);
        pageSql.append(") temp LIMIT ");
        pageSql.append(requestOffset);
        pageSql.append(",");
        pageSql.append(requestCount);
        return pageSql.toString();
    }

    /**
     * 通过原SQL构造获取数量的SQL
     *
     * @param sql
     * @return
     */
    protected String buildCountPageSql(String sql) {
        StringBuilder countPageSql = new StringBuilder();
        countPageSql.append("SELECT COUNT(1) FROM (");
        countPageSql.append(sql);
        countPageSql.append(") temp");
        return countPageSql.toString();
    }

    /**
     * 只拦截这两种类型的
     * <br>StatementHandler
     * <br>ResultSetHandler
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
