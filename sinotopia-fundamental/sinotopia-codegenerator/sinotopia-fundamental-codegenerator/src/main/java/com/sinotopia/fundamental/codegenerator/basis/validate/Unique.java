package com.sinotopia.fundamental.codegenerator.basis.validate;

public interface Unique {
	/**
	 * 多个key验证需要将多个key用逗号隔开
	 */
	public static final String DIVIDER = ",";
	
	/**
	 * 返回唯一性的key，如果是多个key都唯一则需要将多个key用{@link Unique Unique.DIVIDER}隔开
	 */
	public Object getUniqueKey();
}
