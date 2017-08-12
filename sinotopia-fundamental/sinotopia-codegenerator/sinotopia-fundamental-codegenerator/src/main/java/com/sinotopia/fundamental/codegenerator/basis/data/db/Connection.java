package com.hkfs.fundamental.codegenerator.basis.data.db;

import com.hkfs.fundamental.codegenerator.enums.DatabaseType;
import com.hkfs.fundamental.codegenerator.utils.DatabaseUtils;

/**
 * 数据库连接
 * @author zhoub 2015年11月4日 下午10:35:52
 */
public class Connection {
	public String driverClassName = DatabaseType.MYSQL.getDriverClassName();
	/**
	 * 数据库完整链接地址如：jdbc:mysql://localhost:3306/db_xxxx
	 */
	public String url;
	/**
	 * 数据库用户名
	 */
	public String username;
	/**
	 * 数据库密码
	 */
	public String password;
	/**
	 * 数据库名称
	 */
	public String databaseName;
	
	public Connection(String ip, int port, String databaseName, String username, String password) {
		this(DatabaseUtils.processMySqlUrl(ip, port, databaseName), databaseName, username, password);
	}
	public Connection(String url, String databaseName, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.databaseName = databaseName;
	}
}
