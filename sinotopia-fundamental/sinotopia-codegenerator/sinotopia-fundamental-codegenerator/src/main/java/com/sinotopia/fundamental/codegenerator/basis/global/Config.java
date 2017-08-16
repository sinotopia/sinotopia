package com.sinotopia.fundamental.codegenerator.basis.global;

import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

import java.util.HashMap;
import java.util.HashSet;

public class Config {
	private static HashSet<String> basicClassSet = new HashSet<String>();
	private static HashMap<String, String> basicTypeMap = new HashMap<String, String>();
	private static HashSet<String> basicImportClassSet = new HashSet<String>();
	private static HashMap<String, String> classMap = new HashMap<String, String>();
	public static boolean isLogEnabled = true;
	
	static {
		addBasicImportClass("java.util.Date");
		addBasicImportClass("java.util.List");
		
		
		basicClassSet.add("String");
		basicClassSet.add("Integer");
		basicClassSet.add("Double");
		basicClassSet.add("Float");
		basicClassSet.add("Long");
		basicClassSet.add("Boolean");
		basicClassSet.add("Object");
		
		basicTypeMap.put("int", "0");
		basicTypeMap.put("double", "0");
		basicTypeMap.put("short", "0");
		basicTypeMap.put("float", "0");
		basicTypeMap.put("long", "0");
		basicTypeMap.put("byte", "0");
		basicTypeMap.put("char", "'0'");
		basicTypeMap.put("boolean", "false");
		basicTypeMap.put("void", "");
	}
	private static void addBasicImportClass(String fullClassName) {
		String className = CodeUtils.getClassNameFromFullClassName(fullClassName);
		basicImportClassSet.add(className);
		addFullClass(fullClassName);
	}
	
	public static void addFullClass(String fullClassName) {
		if (!fullClassName.contains(".")) {
			throw new IllegalArgumentException("class name "+fullClassName+" illegal, full class name is required!");
		}
		classMap.put(CodeUtils.getClassNameFromFullClassName(fullClassName), fullClassName);
	}
	
	/**
	 * 判断是否是基础类型
	 */
	public static boolean isBasicClass(String className) {
		String cls = CodeUtils.getClassNameFromFullClassName(className);
		return basicClassSet.contains(cls) || basicTypeMap.containsKey(cls);
	}
	/**
	 * 判断否是是基础类型
	 */
	public static boolean isBasicType(String className) {
		return basicTypeMap.containsKey(className);
	}
	/**
	 * 获取基础类型默认值，如果不是基础类型则返回null
	 */
	public static String getBasicTypeDefaultValue(String className) {
		return basicTypeMap.get(className);
	}
	/**
	 * 判断是否是需要导入的类名
	 */
	public static boolean isImportClass(String fullClassName) {
		return classMap.containsKey(CodeUtils.getClassNameFromFullClassName(fullClassName))
				|| classMap.containsValue(fullClassName);
	}
	/**
	 * 获取需要引入的完整类名，如果不在引入类里则返回null
	 */
	public static String getImportFullClassName(String fullClassName) {
		return classMap.get(CodeUtils.getClassNameFromFullClassName(fullClassName));
	}
//	/**
//	 * 判断是否是需要导入的基础类型，如果List,Date等
//	 */
//	public static boolean isImportBasicClass(String className) {
//		return normalClassMap.containsKey(CodeUtils.getClassNameFromFullClassName(className));
//	}
//	/**
//	 * 根据类名获取需要导入的基础类型的完整类名
//	 */
//	public static String getImportBasicClassName(String className) {
//		return classMap.get(CodeUtils.getClassNameFromFullClassName(className));
//	}
}
