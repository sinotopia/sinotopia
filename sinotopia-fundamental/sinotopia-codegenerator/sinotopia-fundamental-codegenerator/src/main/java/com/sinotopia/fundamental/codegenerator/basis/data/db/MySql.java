package com.sinotopia.fundamental.codegenerator.basis.data.db;

import com.sinotopia.fundamental.codegenerator.utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MySql {
	private String databaseName;
	private Connection connection;
	/**
	 * 指定包含的表名
	 */
	private HashSet<String> includeSet = new HashSet<String>();
	/**
	 * 指定排除的表名
	 */
	private HashSet<String> excludeSet = new HashSet<String>();
	
	private MySql(com.sinotopia.fundamental.codegenerator.basis.data.db.Connection connection) {
		this.databaseName = connection.databaseName;
		this.connection = DatabaseUtils.connect(connection.url, connection.username, connection.password);
	}
	
	public MySql include(String...tableNames) {
		if (tableNames != null) {
			for (String tableName : tableNames) {
				includeSet.add(tableName);
			}
		}
		return this;
	}
	
	public MySql exclude(String...tableNames) {
		if (tableNames != null) {
			for (String tableName : tableNames) {
				excludeSet.add(tableName);
			}
		}
		return this;
	}
	
	public static MySql newInstance(com.sinotopia.fundamental.codegenerator.basis.data.db.Connection connection) {
		return new MySql(connection);
	}
	
	public Table[] getTables() {
		try {
			Table[] tables = selectTables();
			if (tables != null && tables.length > 0) {
				HashMap<String, String> tableCommentsMap = getTableCommentsMap(tables, databaseName);
				HashMap<String, String> columnCommentsMap = getColumnCommentsMap(tables, databaseName);
				for (Table table : tables) {
					table.comment = tableCommentsMap.get(DatabaseUtils.processTableCommentKey(table.name));
					Column[] columns = table.columns;
					if (columns != null) {
						for (Column column : columns) {
							column.comment = columnCommentsMap.get(DatabaseUtils.processColumnCommentKey(table.name, column.name));
						}
					}
				}
			}
			return tables;
		}
		finally {
			DatabaseUtils.close(connection);
		}
	}
	
	private Table[] selectTables() {
		List<Table> tableList = new ArrayList<Table>();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData = connection.getMetaData();
			rs = metaData.getTables(connection.getCatalog(),null,null,new String[]{"TABLE"});
			Table table = null;
			String tableName = null;
			Column[] columns = null;
			while(rs.next()) {
				tableName = rs.getString("TABLE_NAME");
				if (isExcludeTable(tableName)) {
					continue;
				}
				
				columns = selectColumns(tableName);
				
				setColumnStructure(tableName, columns);
				
				table = new Table(tableName, columns, null);
				tableList.add(table);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DatabaseUtils.close(rs);
		}
		return tableList.size() > 0 ? tableList.toArray(new Table[tableList.size()]) : new Table[0];
	}
	
	private boolean isExcludeTable(String tableName) {
		if (includeSet != null && !includeSet.isEmpty() && !includeSet.contains(tableName)) {
			return true;
		}
		if (excludeSet != null && !excludeSet.isEmpty() && excludeSet.contains(tableName)) {
			return true;
		}
		return false;
	}
	
	private void setColumnStructure(String tableName, Column[] columns) {
		HashMap<String, Structure> map = getStructureMap(tableName);
		for (Column column : columns) {
			column.setStructure(map.get(column.name));
		}
	}
	
	private Column[] selectColumns(String tableName) {
		List<Column> columnList = new ArrayList<Column>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM "+tableName +" LIMIT 1";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			Column column = null;
			int count = rsmd.getColumnCount();
			String name = null;
			String type = null;
			for(int i=1;i<=count;i++) {
				name = rsmd.getColumnName(i);
				type = rsmd.getColumnTypeName(i);
				column = new Column(name, type, null);
				columnList.add(column);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseUtils.close(rs);
			DatabaseUtils.close(stmt);
		}
		return columnList.size() > 0 ? columnList.toArray(new Column[columnList.size()]) : null;
	}
	
	/**
	 * 获取数据库表的字段的注释
	 * @param tables 数据库表数组
	 * @param databaseName 数据库名称
	 * @return 数据库表字段对应的注释的map，key为表名+"/"+字段名
	 */
	private HashMap<String, String> getColumnCommentsMap(Table[] tables, String databaseName) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			stmt = connection.createStatement();
			String sql = DatabaseUtils.getColumnCommentsSql(tables, databaseName);
			rs = stmt.executeQuery(sql);
			String tableName = null;
			String columnName = null;
			String comment = null;
			while (rs.next()) {
				tableName = rs.getString(1);
				columnName = rs.getString(2);
				comment = rs.getString(3);
				map.put(DatabaseUtils.processColumnCommentKey(tableName, columnName), comment);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DatabaseUtils.close(rs);
			DatabaseUtils.close(stmt);
		}
		return map;
	}
	
	/**
	 * 获取数据库表的注释
	 * @param tables 数据库表数组
	 * @param databaseName 数据库名称
	 * @return 数据库表对应的注释的map，key为表名
	 */
	private HashMap<String, String> getTableCommentsMap(Table[] tables, String databaseName) {
		Statement stmt = null;
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			stmt = connection.createStatement();
			String sql = DatabaseUtils.getTableCommentsSql(tables, databaseName);
			rs = stmt.executeQuery(sql);
			String tableName = null;
			String comment = null;
			while (rs.next()) {
				tableName = rs.getString(1);
				comment = rs.getString(2);
				map.put(DatabaseUtils.processTableCommentKey(tableName), comment);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DatabaseUtils.close(rs);
			DatabaseUtils.close(stmt);
		}
		return map;
	}
	
	private HashMap<String, Structure> getStructureMap(String tableName) {
		HashMap<String, Structure> map = new HashMap<String, Structure>();
		Structure[] structures = selectStructures(tableName);
		if (structures != null && structures.length > 0) {
			for (Structure structure : structures) {
				map.put(structure.field, structure);
			}
		}
		return map;
	}
	
	private Structure[] selectStructures(String tableName) {
		List<Structure> structureList = new ArrayList<Structure>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "DESC "+tableName;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			Structure structure = null;
			while (rs.next()) {
				structure = new Structure();
				structure.field = rs.getString(1);
				structure.type = rs.getString(2);
				structure.permitsNull = rs.getString(3);
				structure.key = rs.getString(4);
				structure.defaultValue = rs.getString(5);
				structure.extra = rs.getString(6);
				structureList.add(structure);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			DatabaseUtils.close(rs);
			DatabaseUtils.close(stmt);
		}
		return structureList.size() > 0 ? structureList.toArray(new Structure[structureList.size()]) : null;
	}
}
