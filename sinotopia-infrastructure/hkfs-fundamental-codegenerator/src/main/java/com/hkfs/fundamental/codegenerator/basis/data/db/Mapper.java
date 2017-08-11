package com.hkfs.fundamental.codegenerator.basis.data.db;

import com.hkfs.fundamental.codegenerator.basis.data.Class;
import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.translator.ColumnNameTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.ColumnTypeTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.TableToClassTranslator;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.io.Outputable;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

public class Mapper extends AbsFormat implements Outputable {
	public String packageName;
	public Table table;
	public Class pojoClass;
	public ColumnNameTranslator columnNameTranslator;
	public ColumnTypeTranslator columnTypeTranslator;
	public String primaryKeyColumnName = "id";
	public String content;
	
	public Mapper(String packageName, Table table, TableToClassTranslator tableToClassTranslator) {
		this.packageName = packageName;
		this.table = table;
		this.pojoClass = tableToClassTranslator.translate(table);
		this.columnNameTranslator = tableToClassTranslator.getColumnNameTranslator();
		this.columnTypeTranslator = tableToClassTranslator.getColumnTypeTranslator();
	}
	
	public Mapper setExistingMapper(String content) {
		this.content = content;
		return this;
	}
	
	@Override
	public String toString() {
		if (StrUtils.isEmpty(content)) {
			StringBuilder sb = new StringBuilder();
			sb.append(processHeader());
			sb.append(processNamespaceHeader());
			sb.append(processRequestAllFieldsSQL());
			sb.append(processWhereClauseSQL());
			sb.append(processInsertSQL());
			sb.append(processUpdateSQL());
			sb.append(processGetListSQL());
			sb.append(processGetOneSQL());
			sb.append(processGetByIdSQL());
			sb.append(processCountSQL());
			sb.append(processExtendedUpdateSQL());
			sb.append(processExtendedWhereClauseSQL());
			sb.append(processExtendedOrderByClauseSQL());
			sb.append(processExtendedSQL());
			sb.append(processFooter());
			return sb.toString();
		}
		else {
			String c = content;
			c = StrUtils.replaceMiddleText(c,
					processRequestAllFieldsSQLPrefix(),
					processRequestAllFieldsSQLSuffix(),
					processRequestAllFieldsSQLContent());
			c = StrUtils.replaceMiddleText(c,
					processWhereClauseSQLPrefix(),
					processWhereClauseSQLSuffix(),
					processWhereClauseSQLContent());
			c = StrUtils.replaceMiddleText(c,
					processInsertSQLPrefix(),
					processInsertSQLSuffix(),
					processInsertSQLContent());
			c = StrUtils.replaceMiddleText(c,
					processUpdateSQLPrefix(),
					processUpdateSQLSuffix(),
					processUpdateSQLContent());
			return c;
		}
	}
	
	
	
	protected String processHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+line());
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">"+line(2));
		return sb.toString();
	}
	
	protected String processNamespaceHeader() {
		return "<mapper namespace=\""+processNamespace()+"\">"+line()+tab()+line();
	}
	
	public String processNamespace() {
		return packageName+"."+processPojoClassName()+"Dao";
	}
	
	protected String processExtendedSQL() {
		return "";
	}
	
	protected String processFooter() {
		return "</mapper>";
	}
	
	public String processMapperName() {
		return processPojoClassName()+"Mapper";
	}
	
	protected String processPojoClassName() {
		return CodeUtils.getClassNameFromFullClassName(pojoClass.fullClassName);
	}
	
	protected String processRequestAllFieldsSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		sb.append(processRequestAllFieldsSQLPrefix());
		sb.append(processRequestAllFieldsSQLContent());
		sb.append(processRequestAllFieldsSQLSuffix());
		sb.append(line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processRequestAllFieldsSQLContent() {
		StringBuilder sb = new StringBuilder();
		sb.append(line());
		sb.append(processRequestAllFieldsItemsSQL());
		sb.append(tab());
		return sb.toString();
	}
	protected String processRequestAllFieldsItemsSQL() {
		StringBuilder sb = new StringBuilder();
		Column[] columns = table.columns;
		if (columns != null && columns.length > 0) {
			for (int i=0;i<columns.length-1;i++) {
				sb.append(processSelectItemSQL(columns[i], false));
			}
			sb.append(processSelectItemSQL(columns[columns.length-1], true));
		}
		return sb.toString();
	}
	
	protected String processRequestAllFieldsSQLPrefix() {
		return "<sql id=\""+processRequestAllFieldsSQLId()+"\">"+line()+tab(2)+"<![CDATA[";
	}
	protected String processRequestAllFieldsSQLSuffix() {
		return tab()+"]]>"+line()+tab()+"</sql>";
	}
	
	protected String processSelectItemSQL(Column column, boolean isLastItem) {
		StringBuilder sb = new StringBuilder();
		sb.append(tab(3));
		String parameterName = columnNameTranslator.translate(column.name);
		if (parameterName.equals(column.name)) {
			sb.append(parameterName);
		}
		else {
			sb.append(column.name).append(" AS ").append(parameterName);
		}
		if (!isLastItem) {
			sb.append(",");
		}
		sb.append(line());
		return sb.toString();
	}

	protected String processRequestAllFieldsSQLId() {
		return "requestAllFields";
	}
	
	protected String processWhereClauseSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		sb.append(processWhereClauseSQLPrefix());
		sb.append(processWhereClauseSQLContent());
		sb.append(processWhereClauseSQLSuffix());
		sb.append(line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processWhereClauseSQLContent() {
		StringBuilder sb = new StringBuilder();
		sb.append(line()+tab(2)+"<where>"+line());
		sb.append(processWhereClauseItemsSQL());
		sb.append(processIncludeExtendedWhereClauseSQL());
		sb.append(tab(2)+"</where>"+line()+tab());
		return sb.toString();
	}
	protected String processWhereClauseItemsSQL() {
		StringBuilder sb = new StringBuilder();
		Column[] columns = table.columns;
		if (columns != null && columns.length > 0) {
			for (Column column : columns) {
				sb.append(processWhereClauseItem(column));
			}
		}
		return sb.toString();
	}
	protected String processWhereClauseSQLPrefix() {
		return "<sql id=\""+processWhereClauseSQLId()+"\">";
	}
	protected String processWhereClauseSQLSuffix() {
		return "</sql>";
	}
	
	protected String processIncludeExtendedWhereClauseSQL() {
		return tab(3)+"<include refid=\""+processExtendedWhereClauseSQLId()+"\" />"+line();
	}
	
	protected String processExtendedWhereClauseSQLId() {
		return "extendedWhereClause";
	}
	
	protected String processWhereClauseItem(Column column) {
		String columnType = column.type;
		//日期不使用where =
		if (columnType.contains("DATE") || columnType.contains("TIMESTAMP")) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String parameterName = columnNameTranslator.translate(column.name);
		sb.append(tab(3) + "<if test=\"null!=" + parameterName);
		if (isStringParameter(column)) {
			sb.append(" and ''!=" + parameterName);
		}
		sb.append("\">AND " + column.name + " = #{" + parameterName + "}</if>" + line());
		return sb.toString();
	}

	protected String processWhereClauseSQLId() {
		return "whereClause";
	}
	
	protected String processInsertSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		sb.append(processInsertSQLPrefix());
		sb.append(processInsertSQLContent());
		sb.append(processInsertSQLSuffix());
		sb.append(line());
		sb.append(tab()+line());
		return sb.toString();
	}

	private boolean isStringParameter(Column column) {
		String type = columnTypeTranslator.translate(column);
		return "String".equalsIgnoreCase(type);
	}

	protected String processInsertSQLPrefix() {
		return "<insert id=\""+processInsertSQLId()+"\"";
	}
	protected String processInsertSQLSuffix() {
		return "</insert>";
	}
	protected String processInsertSQLContent() {
		StringBuilder sb = new StringBuilder();
		if (table.hasAutoIncrementField()) {
			sb.append(" useGeneratedKeys=\"true\"");
		}
		sb.append(" parameterType=\""+processParameterType()+"\" keyProperty=\""+primaryKeyColumnName+"\">"+line());
		sb.append(tab(2)+"INSERT INTO "+table.name+line());
		sb.append(tab(2)+"<trim prefix=\"(\" suffix=\")\" prefixOverrides=\",\">"+line());
		sb.append(processInsertParamsItemsSQL());
		sb.append(tab(2)+"</trim>"+line());
		sb.append(tab(2)+"VALUES"+line());
		sb.append(tab(2)+"<trim prefix=\"(\" suffix=\")\" prefixOverrides=\",\">"+line());
		sb.append(processInsertValuesItemsSQL());
		sb.append(tab(2)+"</trim>"+line()+tab());
		return sb.toString();
	}

	protected String processInsertParamsItemsSQL() {
		StringBuilder sb = new StringBuilder();
		Column[] columns = table.columns;
		if (columns != null && columns.length > 0) {
			for (Column column : columns) {
				sb.append(processInsertDefineItem(column));
			}
		}
		return sb.toString();
	}

	protected String processInsertValuesItemsSQL() {
		StringBuilder sb = new StringBuilder();
		Column[] columns = table.columns;
		if (columns != null && columns.length > 0) {
			for (Column column : columns) {
				sb.append(processInsertValueItem(column));
			}
		}
		return sb.toString();
	}
	
	protected String processInsertSQLId() {
		return "add";
	}
	
	protected String processInsertDefineItem(Column column) {
		if (column.name.equals(primaryKeyColumnName)) {
			return "";
		}
		String parameterName = columnNameTranslator.translate(column.name);
		if (isStringParameter(column)) {
			return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">," + column.name + "</if>" + line();
		}
		return tab(3) + "<if test=\"null!=" + parameterName + "\">," + column.name + "</if>" + line();
	}
	
	protected String processInsertValueItem(Column column) {
		if (column.name.equals(primaryKeyColumnName)) {
			return "";
		}
		String parameterName = columnNameTranslator.translate(column.name);
		if (isStringParameter(column)) {
			return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">,#{" + parameterName + "}</if>" + line();
		}
		return tab(3) + "<if test=\"null!=" + parameterName + "\">,#{" + parameterName + "}</if>" + line();
	}
	
	public Mapper setPrimaryKeyColumnName(String primaryKeyColumnName) {
		this.primaryKeyColumnName = primaryKeyColumnName;
		return this;
	}
	
	protected String processUpdateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		sb.append(processUpdateSQLPrefix());
		sb.append(processUpdateSQLContent());
		sb.append(processUpdateSQLSuffix());
		sb.append(line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processUpdateSQLPrefix() {
		return "<update id=\""+processUpdateSQLId();
	}
	protected String processUpdateSQLSuffix() {
		return "</update>";
	}
	protected String processUpdateSQLContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("\" parameterType=\""+processParameterType()+"\">"+line());
		sb.append(tab(2)+"UPDATE "+table.name+line());
		sb.append(tab(2)+"<trim prefix=\"SET\" prefixOverrides=\",\">"+line());
		sb.append(processUpdateSetterItemsSQL());
		sb.append(processIncludeExtendedUpdateSQL());
		sb.append(tab(2)+"</trim>"+line());
		sb.append(tab(2)+"WHERE "+primaryKeyColumnName+" = #{"+columnNameTranslator.translate(primaryKeyColumnName)+"}"+line()+tab());
		return sb.toString();
	}

	protected String processUpdateSetterItemsSQL() {
		StringBuilder sb = new StringBuilder();
		Column[] columns = table.columns;
		if (columns != null && columns.length > 0) {
			for (Column column : columns) {
				sb.append(processUpdateItem(column));
			}
		}
		return sb.toString();
	}

	protected String processIncludeExtendedUpdateSQL() {
		return tab(3)+"<include refid=\""+processIncludeExtendedUpdateSQLId()+"\" />"+line();
	}

	protected String processIncludeExtendedUpdateSQLId() {
		return "extendedUpdateSql";
	}

	protected String processUpdateItem(Column column) {
		if (column.name.equals(primaryKeyColumnName)) {
			return "";
		}
		String parameterName = columnNameTranslator.translate(column.name);
		if (isStringParameter(column)) {
			return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">," + column.name + " = #{" + parameterName + "}</if>" + line();
		}
		return tab(3) + "<if test=\"null!=" + parameterName + "\">," + column.name + " = #{" + parameterName + "}</if>" + line();
	}

	protected String processUpdateSQLId() {
		return "update";
	}
	
	protected String processGetListSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<select id=\""+processGetListSQLId()+"\" parameterType=\""+processParameterType()+"\" resultType=\""+processResultType()+"\">"+line());
		sb.append(tab(2)+"SELECT <include refid=\""+processRequestAllFieldsSQLId()+"\"/> FROM "+table.name+""+line());
		sb.append(tab(2)+"<include refid=\""+processWhereClauseSQLId()+"\" />"+line());
		sb.append(tab(2)+"<include refid=\""+processExtendedOrderByClauseSQLId()+"\" />"+line());
		sb.append(tab(2)+"<if test=\"null!="+processRequestOffsetName()+"\">"+line());
		sb.append(tab(3)+"LIMIT #{"+processRequestOffsetName()+"}, #{"+processRequestCountName()+"}"+line());
		sb.append(tab(2)+"</if>"+line());
		sb.append(tab()+"</select>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processRequestOffsetName() {
		return "requestOffset";
	}
	
	protected String processRequestCountName() {
		return "requestCount";
	}
	
	protected String processGetListSQLId() {
		return "query";
	}
	
	protected String processCountSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<select id=\""+processCountSQLId()+"\" parameterType=\""+processParameterType()+"\" resultType=\"int\">"+line());
		sb.append(tab(2)+"SELECT COUNT(1) FROM "+table.name+" <include refid=\""+processWhereClauseSQLId()+"\" />"+line());
		sb.append(tab()+"</select>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processCountSQLId() {
		return "count";
	}
	
	protected String processExtendedWhereClauseSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<!-- 扩展的条件过滤语句（自定义）-->"+line());
		sb.append(tab()+"<sql id=\""+processExtendedWhereClauseSQLId()+"\">"+line());
		sb.append(tab(2)+"<if test=\"null!="+processExtendedParameterName()+"\">"+line());
		sb.append(tab(3)+line());
		sb.append(tab(2)+"</if>"+line());
		sb.append(tab()+"</sql>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processExtendedOrderByClauseSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<!-- 扩展的排序等语句（自定义）-->"+line());
		sb.append(tab()+"<sql id=\""+processExtendedOrderByClauseSQLId()+"\">"+line());
		sb.append(tab(2)+"<if test=\"null!="+processExtendedParameterName()+"\">"+line());
		sb.append(tab(3)+line());
		sb.append(tab(2)+"</if>"+line());
		sb.append(tab()+"</sql>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}

	protected String processExtendedUpdateSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<!-- 扩展的更新等语句（自定义）-->"+line());
		sb.append(tab()+"<sql id=\""+processIncludeExtendedUpdateSQLId()+"\">" +line());
		sb.append(tab(2)+"<if test=\"null!="+processExtendedParameterName()+"\">"+line());
		sb.append(tab(3)+line());
		sb.append(tab(2)+"</if>"+line());
		sb.append(tab()+"</sql>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processExtendedOrderByClauseSQLId() {
		return "extendedOrderByClause";
	}
	
	protected String processExtendedParameterName() {
		return "extendedParameter";
	}

	@Override
	public String processOutputFileName() {
		return processMapperName();
	}

	@Override
	public String processOutputExtensionName() {
		return "xml";
	}

	@Override
	public String getPackageName() {
//		return packageName;
		return "";
	}
	
	protected String processGetOneSQLId() {
		return "get";
	}
	protected String processGetOneSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<select id=\""+processGetOneSQLId()+"\" parameterType=\""+processParameterType()+"\" resultType=\""+processResultType()+"\">"+line());
		sb.append(tab(2)+"SELECT <include refid=\""+processRequestAllFieldsSQLId()+"\"/> FROM "+table.name+""+line());
		sb.append(tab(2)+"<include refid=\""+processWhereClauseSQLId()+"\" />"+line());
		sb.append(tab(2)+"LIMIT 1"+line());
		sb.append(tab()+"</select>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processGetByIdSQLId() {
		return "getById";
	}
	protected String processGetByIdSQL() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab()+"<select id=\""+processGetByIdSQLId()+"\" parameterType=\"long\" resultType=\""+processResultType()+"\">"+line());
		sb.append(tab(2)+"SELECT <include refid=\""+processRequestAllFieldsSQLId()+"\"/> FROM "+table.name+" WHERE "+primaryKeyColumnName+" = #{"+columnNameTranslator.translate(primaryKeyColumnName)+"}"+line());
		sb.append(tab()+"</select>"+line());
		sb.append(tab()+line());
		return sb.toString();
	}
	
	protected String processParameterType() {
		return pojoClass.fullClassName;
	}
	protected String processResultType() {
		return pojoClass.fullClassName;
	}
	
}
