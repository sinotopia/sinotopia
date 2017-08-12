package com.hkfs.fundamental.codegenerator.basis.translator;

import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.basis.data.Field;
import com.hkfs.fundamental.codegenerator.basis.data.db.Column;
import com.hkfs.fundamental.codegenerator.basis.data.db.Table;
import com.hkfs.fundamental.codegenerator.basis.render.FieldMethodRender;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表转换成Class
 * @author zhoub 2015年11月17日 下午10:09:11
 */
public class TableToClassTranslator {
	/**
	 * 生成的POJO类的包名
	 */
	private String packageName;
	private ColumnTypeTranslator columnTypeTranslator;
	private ColumnNameTranslator columnNameTranslator;
	private ColumnToEnumTranslator columnToEnumTranslator;
	
	private String parentClassName;
	private boolean isSerializable;
	private String[] tablePrefixes;
	
	private List<FieldMethodRender> fieldMethodRenderList = new ArrayList<FieldMethodRender>();
	
	/**
	 * 构造方法（使用默认的数据库字段类型转换器 {@link BaseColumnTypeTranslator}）
	 * @param packageName 生成的POJO类的包名
	 */
	public TableToClassTranslator(String packageName) {
		this(packageName, new BaseColumnTypeTranslator(), new BaseColumnNameTranslator());
	}
	/**
	 * 构造方法
	 * @param packageName 生成的POJO类的包名
	 * @param columnTypeTranslator 数据库字段类型转换器
	 */
	public TableToClassTranslator(String packageName, ColumnTypeTranslator columnTypeTranslator, ColumnNameTranslator columnNameTranslator) {
		this.packageName = packageName;
		this.columnTypeTranslator = columnTypeTranslator;
		this.columnNameTranslator = columnNameTranslator;
	}
	
	/**
	 * 设置父类
	 */
	public TableToClassTranslator setParentClass(Clazz parentClass) {
		this.parentClassName = parentClass!=null?parentClass.fullClassName:null;
		return this;
	}
	/**
	 * 设置父类的名称
	 */
	public TableToClassTranslator setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
		return this;
	}
	/**
	 * 设置生成的对象是否实现了默认的序列化接口
	 */
	public TableToClassTranslator setIsSerializable(boolean isSerializable) {
		this.isSerializable = isSerializable;
		return this;
	}
	/**
	 * 设置表名的前缀，如tb_
	 */
	public TableToClassTranslator setTablePrefix(String...tablePrefixes) {
		this.tablePrefixes = tablePrefixes;
		return this;
	}
	
	/**
	 * 数据库表转换成Class
	 * @param table 数据库表
	 * @return 转换后的Class
	 */
	public Clazz translate(Table table) {
		String className = tableNameToClassName(table.name);
		String fullClassName = CodeUtils.getFullClassName(packageName, className);
		Clazz cls = new Clazz(fullClassName);
		cls.setComment(table.comment);
		Column[] columns = table.columns;
		if (columns != null) {
			for (Column column : columns) {
				String fieldName = columnNameTranslator.translate(column);
				String fieldType = columnTypeTranslator.translate(column);
				Field field = new Field(fieldName, fieldType);
				if (StrUtils.notEmpty(column.comment)) {
					field.setComment(column.comment);
				}
				if (columnToEnumTranslator != null) {
					field.setRelatedEnum(columnToEnumTranslator.translate(table, column));
				}
				cls.addField(field);
			}
		}
		
		if (fieldMethodRenderList.size() > 0) {
			for (FieldMethodRender fieldMethodRender : fieldMethodRenderList) {
				cls.addFieldMethodRender(fieldMethodRender);
			}
		}
		
		cls.setParentClassName(parentClassName);
		cls.setIsSerializable(isSerializable);
		return cls;
	}
	
	protected String tableNameToClassName(String tableName) {
		if (tablePrefixes == null || tablePrefixes.length == 0) {
			return CodeUtils.getClassName(tableName);
		}
		for (String tablePrefix : tablePrefixes) {
			if (tableName.startsWith(tablePrefix)) {
				tableName = tableName.replaceFirst(tablePrefix, "");
				break;
			}
		}
		return CodeUtils.getClassName(tableName);
	}
	
	/**
	 * 数据库表数组转换成Class数组
	 * @param tables 数据库表数组
	 * @return 转换后的Class数组
	 */
	public Clazz[] translate(Table[] tables) {
		List<Clazz> classList = new ArrayList<Clazz>(tables.length);
		for (Table table : tables) {
			classList.add(translate(table));
		}
		return classList.toArray(new Clazz[classList.size()]);
	}
	public ColumnToEnumTranslator getColumnToEnumTranslator() {
		return columnToEnumTranslator;
	}
	/**
	 * 设置数据字段枚举转换器
	 * @param columnToEnumTranslator 数据库字段枚举转换器
	 */
	public void setColumnToEnumTranslator(ColumnToEnumTranslator columnToEnumTranslator) {
		if (columnToEnumTranslator != null) {
			columnToEnumTranslator.setTableToClassTranslator(this);
		}
		this.columnToEnumTranslator = columnToEnumTranslator;
	}
	public ColumnTypeTranslator getColumnTypeTranslator() {
		return columnTypeTranslator;
	}
	public void setColumnTypeTranslator(ColumnTypeTranslator columnTypeTranslator) {
		this.columnTypeTranslator = columnTypeTranslator;
	}
	public ColumnNameTranslator getColumnNameTranslator() {
		return columnNameTranslator;
	}
	public void setColumnNameTranslator(ColumnNameTranslator columnNameTranslator) {
		this.columnNameTranslator = columnNameTranslator;
	}
	public void addFieldMethodRender(FieldMethodRender fieldMethodRender) {
		fieldMethodRenderList.add(fieldMethodRender);
	}
}
