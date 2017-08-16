package com.sinotopia.fundamental.codegenerator.basis.data.db;

/**
 * 数据库表结构
 * @author zhoubing 2015年11月30日 下午3:14:45
 */
public class Structure {
	/**
	 * 字段名称
	 */
	public String field;
	/**
	 * 字段类型
	 */
	public String type;
	/**
	 * 是否允许空
	 */
	public String permitsNull;
	/**
	 * 主键，外键等
	 */
	public String key;
	/**
	 * 默认值
	 */
	public String defaultValue;
	/**
	 * 额外
	 */
	public String extra;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(field).append(" ").append(type).append(" ").append(permitsNull)
		.append(" ").append(key).append(" ").append(defaultValue).append(" ").append(extra);
		return sb.toString();
	}
}
