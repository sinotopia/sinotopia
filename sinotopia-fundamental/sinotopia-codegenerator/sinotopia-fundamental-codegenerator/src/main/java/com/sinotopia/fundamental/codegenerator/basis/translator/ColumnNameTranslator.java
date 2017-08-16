package com.sinotopia.fundamental.codegenerator.basis.translator;

import com.sinotopia.fundamental.codegenerator.basis.data.db.Column;

/**
 * 数据库字段名称转换器
 * @author zhoub 2015年11月17日 下午10:09:45
 */
public interface ColumnNameTranslator {
	/**
	 * 将数据库中字段的名称转换成java类的成员变量名称
	 * @param columnName 数据库字段名称
	 * @return java类的成员变量名称
	 */
	public String translate(String columnName);

	/**
	 * 将数据库中字段的名称转换成java类的成员变量名称
	 * @param column 数据库字段
	 * @return java类的成员变量名称
	 */
	public String translate(Column column);
}
