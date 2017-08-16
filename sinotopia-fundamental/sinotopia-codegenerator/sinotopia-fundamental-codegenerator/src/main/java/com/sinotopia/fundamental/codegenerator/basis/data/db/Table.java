package com.sinotopia.fundamental.codegenerator.basis.data.db;

import java.util.ArrayList;

import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

/**
 * 数据库表
 * @author zhoub 2015年10月28日 下午11:24:51
 */
public class Table {
	public String name;//数据库表名
	public Column[] columns;//数据库字段
	public String comment;//数据库评论
	
	public Table(String name, Column[] columns, String comment) {
		this.name = name;
		this.columns = columns;
		this.comment = comment;
	}
	
	public Table addColumn(Column column) {
		return this.setColumns(CodeUtils.addNew(new ArrayList<Column>(), columns, column));
	}
	public Table setColumns(Column[] columns) {
		this.columns = columns;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ").append(comment).append(AbsFormat.line());
		if (columns != null && columns.length > 0) {
			for (int i=0;i<columns.length-1;i++) {
				sb.append(AbsFormat.tab()).append(columns[i]).append(AbsFormat.line());
			}
			sb.append(AbsFormat.tab()).append(columns[columns.length-1]);
		}
		return sb.toString();
	}
	
	/**
	 * 判断表是否包含自动增长字段
	 */
	public boolean hasAutoIncrementField() {
		if (columns != null && columns.length > 0) {
			for (Column column : columns) {
				if (column.structure != null && column.structure.extra != null
						&& column.structure.extra.equalsIgnoreCase("auto_increment")) {
					return true;
				}
			}
		}
		return false;
	}
}
