package com.hkfs.fundamental.codegenerator.utils;

import com.hkfs.fundamental.codegenerator.basis.data.db.Table;
import com.hkfs.fundamental.codegenerator.enums.DatabaseType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库操作辅助类
 * @author brucezee Jan 20, 2013 10:24:12 AM
 */
public class DatabaseUtils {
	/**
	 * jdbc数据库链接字符串
	 */
	public static final String CONNECTION_URL = "jdbc:{type}://{ip}:{port}/{schema}";
	/**
	 * 连接数据库
	 * @param url 连接地址
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 成功返回数据库连接，失败返回null。
	 */
	public static Connection connect(String url, String username, String password) {
		Connection connection = null;
		try {
			Class.forName(DatabaseType.MYSQL.getDriverClassName()).newInstance();
			connection = DriverManager.getConnection(url, username, password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 连接数据库
	 * @param ip ip地址
	 * @param port 端口号
	 * @param schema 数据库
	 * @param username 用户名
	 * @param password 密码
	 * @return 成功返回数据库连接，失败返回null。
	 */
	public static Connection connect(String ip, int port, String schema, String username, String password) {
		return connect(DatabaseType.MYSQL.getType(), DatabaseType.MYSQL.getDriverClassName(), ip, port, schema, username, password);
	}

	/**
	 * 连接数据库
	 * @param databaseType 数据库类型
	 * @param ip ip地址
	 * @param port 端口号
	 * @param schema 数据库
	 * @param username 用户名
	 * @param password 密码
	 * @return 成功返回数据库连接，失败返回null。
	 */
	public static Connection connect(DatabaseType databaseType, String ip, int port, String schema, String username, String password) {
		return connect(databaseType.getType(), databaseType.getDriverClassName(), ip, port, schema, username, password);
	}
	/**
	 * 连接数据库
	 * @param type 数据库类型 mysql postgresql
	 * @param driverClassName 数据库驱动类
	 * @param ip ip地址
	 * @param port 端口号
	 * @param schema 数据库
	 * @param username 用户名
	 * @param password 密码
	 * @return 成功返回数据库连接，失败返回null。
	 */
	public static Connection connect(String type, String driverClassName, String ip, int port, String schema, String username, String password) {
		Connection connection = null;
		try {
			String url = processConnectionUrl(type, ip, port, schema);
			Class.forName(driverClassName).newInstance();
			connection = DriverManager.getConnection(url, username, password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * 关闭连接
	 * @param connection 数据库连接
	 */
	public static void close(Connection connection) {
		closeSafe(connection);
	}
	
	public static void close(ResultSet rs) {
		closeSafe(rs);
	}
	
	public static void close(Statement stmt) {
		closeSafe(stmt);
	}
	
	public static void close(PreparedStatement pstmt) {
		closeSafe(pstmt);
	}
	
	private static void closeSafe(Object object) {
		if (object != null) {
			try {
				if (object instanceof PreparedStatement) {
					((PreparedStatement)object).close();
				}
				else if (object instanceof Statement) {
					((Statement)object).close();
				}
				else if (object instanceof ResultSet) {
					((ResultSet)object).close();
				}
				else if (object instanceof Connection) {
					((Connection)object).close();
				}
			}
			catch (Exception e) {}
		}
	}
	
	public static Long getLastInsertId(Connection connection) {
		Long uid = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			String sqlLastInsertId = "SELECT LAST_INSERT_ID() AS uid";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sqlLastInsertId);
			if (rs.next()) {
				uid = rs.getLong(1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			close(stmt);
			close(rs);
		}
		return uid;
	}

	public static String processMySqlUrl(String ip, int port, String databaseName) {
		return processConnectionUrl(DatabaseType.MYSQL, ip, port, databaseName);
	}
	public static String processConnectionUrl(DatabaseType databaseType, String ip, int port, String schema) {
		return processConnectionUrl(databaseType.getType(), ip, port, schema);
	}
	public static String processConnectionUrl(String type, String ip, int port, String schema) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("ip", ip);
		params.put("port", port);
		params.put("schema", schema);
		return StrUtils.replaceText(CONNECTION_URL, params);
	}

	public static String getColumnCommentsSql(Table[] tables, String databaseName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT table_name, COLUMN_NAME, COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema = '"
				+databaseName+"' AND table_name IN (");
		StringBuilder s = new StringBuilder();
		for (Table table : tables) {
			s.append("'").append(table.name).append("',");
		}
		sb.append(s.substring(0, s.length()-1));
		sb.append(")");
		return sb.toString();
	}

	public static String getTableCommentsSql(Table[] tables, String databaseName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT table_name, TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '"
				+databaseName+"' AND table_name IN (");
		StringBuilder s = new StringBuilder();
		for (Table table : tables) {
			s.append("'").append(table.name).append("',");
		}
		sb.append(s.substring(0, s.length()-1));
		sb.append(")");
		return sb.toString();
	}

	public static String processColumnCommentKey(String tableName, String columnName) {
		return tableName+"/"+columnName;
	}
	public static String processTableCommentKey(String tableName) {
		return tableName;
	}
}
