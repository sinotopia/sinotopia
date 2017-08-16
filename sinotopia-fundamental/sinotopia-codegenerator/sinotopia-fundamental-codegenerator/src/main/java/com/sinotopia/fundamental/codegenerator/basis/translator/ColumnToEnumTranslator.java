package com.sinotopia.fundamental.codegenerator.basis.translator;

import com.sinotopia.fundamental.codegenerator.basis.data.custom.CDict;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.CEnum;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.StrDict;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.StrEnum;
import com.sinotopia.fundamental.codegenerator.basis.data.db.Column;
import com.sinotopia.fundamental.codegenerator.basis.data.db.Table;
import com.sinotopia.fundamental.codegenerator.basis.render.NameRender;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;
import com.sinotopia.fundamental.codegenerator.basis.data.Enum;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库字段枚举转换器
 * @author zhoub 2015年11月21日 下午9:58:25
 */
public class ColumnToEnumTranslator {
	/**
	 * 枚举默认的包名
	 */
	private String packageName;
	/**
	 * 数据库字段类型转换器
	 */
	private ColumnTypeTranslator columnTypeTranslator;
	/**
	 * 枚举字段名称处理器
	 */
	private NameRender dictNameRender;
	
	private TableToClassTranslator tableToClassTranslator;
	
	public ColumnToEnumTranslator(String packageName) {
		this(packageName, new BaseColumnTypeTranslator());
	}
	public ColumnToEnumTranslator(String packageName, NameRender dictNameRender) {
		this(packageName, new BaseColumnTypeTranslator(), dictNameRender);
	}
	public ColumnToEnumTranslator(String packageName, ColumnTypeTranslator columnTypeTranslator) {
		this(packageName, columnTypeTranslator, NameRender.Uppercase);
	}
	public ColumnToEnumTranslator(String packageName, ColumnTypeTranslator columnTypeTranslator, NameRender dictNameRender) {
		this.packageName = packageName;
		this.columnTypeTranslator = columnTypeTranslator;
		this.dictNameRender = dictNameRender;
	}
	public void setTableToClassTranslator(TableToClassTranslator tableToClassTranslator) {
		this.tableToClassTranslator = tableToClassTranslator;
	}

	public Enum translate(Table table, Column column) {
		if (!isEnumColumn(column)) {
			return null;
		}

		String className = processEnumClassName(table, column);
		String fullClassName = CodeUtils.getFullClassName(processEnumPackageName(table, column), className);

		String fieldType = columnTypeTranslator.translate(column);
		if (fieldType.equalsIgnoreCase("Integer")) {
			CDict[] dicts = processDicts(column);
			if (dicts != null && dicts.length > 0) {
				return new CEnum(fullClassName, column.comment, dicts);
			}
		}
		else if (fieldType.equalsIgnoreCase("String")) {
			StrDict[] dicts = processStrDicts(column);
			if (dicts != null && dicts.length > 0) {
				return new StrEnum(fullClassName, column.comment, dicts);
			}
		}
		
		return null;
	}

	protected String processEnumPackageName(Table table, Column column) {
		return packageName;
	}
	protected String processEnumClassName(Table table, Column column) {
		StringBuilder sb = new StringBuilder();
		if (tableToClassTranslator != null) {
			sb.append(tableToClassTranslator.tableNameToClassName(table.name));
		}
		else {
			sb.append(CodeUtils.getClassName(table.name));
		}
		sb.append(CodeUtils.getClassName(column.name));
		return sb.toString();
	}
	
	private boolean isEnumColumn(Column column) {
		String fieldType = columnTypeTranslator.translate(column);
		if (!fieldType.equalsIgnoreCase("Integer") && !fieldType.equalsIgnoreCase("String")) {
			return false;
		}

		String comment = column.comment;
		if (StrUtils.isEmpty(comment)) {
			return false;
		}
		if (!comment.contains(":")) {
			return false;
		}
		if (!comment.contains(",")) {
			return false;
		}
		return true;
	}

	protected StrDict[] processStrDicts(Column column) {
		CDict[] dicts = processDicts(column);
		if (dicts != null) {
			List<StrDict> dictList = new ArrayList<StrDict>(dicts.length);
			for (CDict dict : dicts) {
				dictList.add(StrDict.clone(dict));
			}
			return dictList.toArray(new StrDict[dictList.size()]);
		}
		return null;
	}
	
	protected CDict[] processDicts(Column column) {
		String[] parameters = column.comment.split(":");
		if (parameters.length != 2) {
			System.err.println("column "+column.name+" dictionary comment format error "+column.comment+
					", the correct format like : 安装包下载类型:1,server,服务器;2,third part,第三方;");
			return null;
		}
		
		parameters = parameters[1].split(";");
		if (parameters.length > 0) {
			List<CDict> dictList = new ArrayList<CDict>();
			for (String p : parameters) {
				if (StrUtils.notEmpty(p)) {
					String[] ps = p.split(",");
					if (ps.length != 3) {
						System.err.println("column "+column.name+" dictionary comment format error "+p+
							", the correct format like : 安装包下载类型:1,server,服务器;2,third part,第三方;");
					}
					else {
						try {
							int value = Integer.valueOf(ps[0]);
							String name = CodeUtils.getClassName(ps[1]);
							String comm = ps[2];
							dictList.add(new CDict(value, name, comm).setNameRender(dictNameRender));
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			return dictList.toArray(new CDict[dictList.size()]);
		}
		
		return null;
	}
}
