package com.hkfs.fundamental.codegenerator.basis.translator;

import com.hkfs.fundamental.codegenerator.basis.data.db.Column;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

/**
 * 默认的数据库字段名称转换器
 * @author zhoubing 2015年11月24日 上午9:25:33
 */
public class BaseColumnNameTranslator implements ColumnNameTranslator {
	@Override
	public String translate(String columnName) {
		return CodeUtils.getParameterName(columnName);
	}

	@Override
	public String translate(Column column) {
		return translate(column.name);
	}
}
