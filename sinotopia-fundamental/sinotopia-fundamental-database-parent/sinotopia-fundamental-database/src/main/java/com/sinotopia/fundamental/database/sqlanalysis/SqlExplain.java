package com.sinotopia.fundamental.database.sqlanalysis;

import com.sinotopia.fundamental.common.utils.NumberUtils;
import com.sinotopia.fundamental.database.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * 对sql语句进行Explain
 */
public class SqlExplain {

    private static Logger logger = LoggerFactory.getLogger(SqlExplain.class);
    private static final String EXPLAIN_KEY = "EXPLAIN ";

    private Connection connection;

    public SqlExplain(String url, String username, String password) {
        this.connection = DatabaseUtils.connect(url, username, password);
    }

    public List<Explain> explain(String sql) {
        if (!sql.toUpperCase().startsWith(EXPLAIN_KEY)) {
            sql = EXPLAIN_KEY + sql;
        }

        Statement statement = null;
        ResultSet rs = null;
        List<Explain> explainList = new LinkedList<Explain>();
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            Explain explain = null;
            while (rs.next()) {
                explain = new Explain();

                explain.setId(NumberUtils.parseLong(rs.getString("id")));
                explain.setSelectType(rs.getString("select_type"));
                explain.setTable(rs.getString("table"));
                explain.setPossibleKeys(rs.getString("possible_keys"));
                explain.setKey(rs.getString("key"));
                explain.setKeyLen(NumberUtils.parseInt(rs.getString("key_len")));
                explain.setRef(rs.getString("ref"));
                explain.setRows(NumberUtils.parseInt(rs.getString("rows")));
                explain.setExtra(rs.getString("Extra"));
                explainList.add(explain);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            DatabaseUtils.close(rs);
            DatabaseUtils.close(statement);
        }

        return explainList;
    }

    public void destroy() {
        DatabaseUtils.close(connection);
    }
}
