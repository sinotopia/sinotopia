package com.sinotopia.fundamental.codegenerator.basis.translator;

import com.sinotopia.fundamental.codegenerator.basis.data.db.Column;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的数据库字段类型转换器
 * @author zhoub 2015年11月17日 下午10:10:15
 */
public class BaseColumnTypeTranslator implements ColumnTypeTranslator {
	/**
	 * 数据库字段类型与java字段类型映射
	 */
	public Map<String, String> reflection = new HashMap<String, String>();
	public BaseColumnTypeTranslator setReflection(Map<String, String> reflection) {
		this.reflection.putAll(reflection);
		return this;
	}
	public BaseColumnTypeTranslator setReflection(String key, String type) {
		this.reflection.put(key, type);
		return this;
	}
	

	@Override
	public String translate(Column column) {
		String columnType = column.type;
		String structureType = column.structure.type;

		if (reflection.containsKey(columnType)) {
			return reflection.get(columnType);
		}
		if (reflection.containsKey(columnType.toLowerCase())) {
			return reflection.get(columnType.toLowerCase());
		}

		columnType = columnType.toUpperCase();
		if (reflection.containsKey(columnType)) {
			return reflection.get(columnType);
		}

		if (columnType.startsWith("BIGINT")) {
			return "Long";
		}
		else if (columnType.contains("INT")) {
			if (structureType.contains("20")) {
				return "Long";
			}
			return "Integer";
		}
		else if (columnType.contains("CHAR") || columnType.contains("TEXT")) {
			return "String";
		}
		else if (columnType.contains("DATE") || columnType.contains("TIMESTAMP")) {
			return "java.util.Date";
		}
		else if (columnType.contains("DECIMAL") || columnType.contains("DOUBLE")) {
//			return "Double";
			return "java.math.BigDecimal";
		}
		else if (columnType.contains("BLOB")) {
			return "byte[]";
		}
		else if (columnType.contains("BIT")) {
			return "Boolean";
		}
		else {
			System.err.println("unknown column type : "+columnType);
			return "Object";
		}
	}

}
