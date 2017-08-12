package com.hkfs.fundamental.codegenerator.basis.translator;

import com.hkfs.fundamental.codegenerator.basis.data.db.Column;

/**
 * 数据库字段类型转换器
 * @author zhoub 2015年11月17日 下午10:09:45
 */
public interface ColumnTypeTranslator {
	/**
	 * 将数据库中字段的类型转换成java的类型
	 * @param column 数据库字段
	 * @return java类型
	 */
	public String translate(Column column);
}
