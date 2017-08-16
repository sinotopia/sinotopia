package com.sinotopia.fundamental.codegenerator.basis.data.db;

/**
 * 数据库字段
 * @author zhoub 2015年10月28日 下午11:23:50
 */
public class Column {
	public String name;//字段名
	public String type;//字段类型
	public String comment;//字段评论
	public Structure structure;//结构
	
	public Column(String name, String type, String comment) {
		this.name = name;
		this.type = type;
		this.comment = comment;
	}
	
	public Column setStructure(Structure structure) {
		this.structure = structure;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append(" ").append(type).append(" ").append(comment).append(" ").append(structure);
		return sb.toString();
	}
}
