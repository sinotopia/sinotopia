package com.hkfs.fundamental.codegenerator.utils;

import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.basis.data.Enum;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.render.NameRender;
import com.hkfs.fundamental.codegenerator.io.Outputable;

import java.util.List;


public class CodeUtils {
	/**
	 * 第一个字母大写
	 * @param str 字符串
	 * @return 第一个字母大写其他字母小写的字符串
	 */
	public static String makeFirstLetterUpperCase(String str) {
		if (str != null && str.length() > 0) {
			if (!StrUtils.isUpperCase(str.charAt(0))) {
				return (str.charAt(0)+"").toUpperCase()+str.substring(1);
			}
		}
		return str;
	}
	
	/**
	 * 第一个字母小写
	 * @param str 字符串
	 * @return 第一个字母小写其他字母不变的字符串
	 */
	public static String makeFirstLetterLowerCase(String str) {
		if (str != null && str.length() > 0) {
			if (!StrUtils.isLowerCase(str.charAt(0))) {
				return (str.charAt(0)+"").toLowerCase()+str.substring(1);
			}
		}
		return str;
	}
	/**
	 * 根据完整类名（包含包名）获取类名（不包含包名）
	 */
	public static String getClassNameFromFullClassName(String fullClassName) {
//		int index = fullClassName.lastIndexOf("<");
//		if (index != -1) {
//			String innerClassName = getClassNameFromFullClassName(StrUtils.getMiddleText(fullClassName, "<", ">"));
//			String outerClassName = getClassNameFromFullClassName(fullClassName.substring(0, index));
//			return outerClassName+"<"+innerClassName+">";
//		}
		
		int index = fullClassName.lastIndexOf(".");
		String className = index != -1 ? fullClassName.substring(index+1) : fullClassName;
		return Config.isBasicType(className) ? className : makeFirstLetterUpperCase(className);
	}
	/**
	 * 根据完整类名（包含包名）获取包名（不包含类名）
	 */
	public static String getPackageNameFromFullClassName(String fullClassName) {
		int index = fullClassName.lastIndexOf(".");
		return index > 0 ? fullClassName.substring(0, index) : "";
	}
	/**
	 * 判断类名是否包含完整的包名
	 */
	public static boolean isFullClassClass(String fullClassName) {
		return fullClassName != null && fullClassName.contains(".");
	}
	/**
	 * 判断是否是虚类或虚方法
	 */
	public static boolean isAbstract(String modifier) {
		return modifier != null && modifier.contains("abstract");
	}
	/**
	 * 判断是否是Serializable
	 */
	public static boolean isSerializable(String fullClassName) {
		return fullClassName != null && fullClassName.contains("Serializable");
	}
	/**
	 * 根据包名获取文件路径
	 */
	public static String getPathFromPackageName(String packageName) {
		return packageName.replace(".", "/")+"/";
	}
	/**
	 * 根据完整类名获取文件路径
	 */
	public static String getPathFromFullClassName(String fullClassName) {
		if (CodeUtils.isFullClassClass(fullClassName)) {
			String packageName = CodeUtils.getPackageNameFromFullClassName(fullClassName);
			String realClassName = CodeUtils.getClassNameFromFullClassName(fullClassName);
			StringBuilder sb = new StringBuilder();
			sb.append(CodeUtils.getPathFromPackageName(packageName));
			sb.append(realClassName);
			return sb.toString();
		}
		return getPathFromPackageName(makeFirstLetterUpperCase(fullClassName));
	}
	/**
	 * 标准化处理完整类名
	 */
	public static String formatFullClassName(String fullClassName) {
		if (isFullClassClass(fullClassName)) {
			return getPackageNameFromFullClassName(fullClassName)+"."+getClassNameFromFullClassName(fullClassName);
		}
		return getClassNameFromFullClassName(fullClassName);
	}
	
	/**
	 * 将一个新的元素添加到一个数组中并返回新的数组
	 * @param list 空的列表
	 * @param origin 原数组
	 * @param newItem 新项
	 * @return 返回新生成的数组
	 */
	public static <T> T[] addNew(List<T> list, T[] origin, T newItem) {
		for (T each : origin) {
			list.add(each);
		}
		list.add(newItem);
		return list.toArray(origin);
	}
	/**
	 * 默认分隔符
	 */
	public static String DEFAULT_DIVIDER = " |_";
	
	/**
	 * 将一个字符串格式化成驼峰方式的类名
	 * @param str 字符串
	 * @return 驼峰方式的类名
	 */
	public static String getClassName(String str) {
		return getClassName(str, DEFAULT_DIVIDER);
	}
	/**
	 * 将一个字符串格式化成驼峰方式的类名
	 * @param str 字符串
	 * @param separator 分隔符
	 * @return 驼峰方式的类名
	 */
	public static String getClassName(String str, String separator) {
		if (StrUtils.notEmpty(str)) {
			if (StrUtils.isEmpty(separator)) {
				return makeFirstLetterUpperCase(str);
			}
			else {
				String[] words = str.split(separator);
				StringBuilder sb = new StringBuilder();
				for (String word : words) {
					sb.append(makeFirstLetterUpperCase(word));
				}
				return sb.toString();
			}
		}
		return str;
	}
	/**
	 * 将一个字符串格式化成驼峰方式的类名对应的参数名
	 * @param str 字符串
	 * @return 驼峰方式的类名对应的参数名
	 */
	public static String getParameterName(String str) {
		return getParameterName(str, DEFAULT_DIVIDER);
	}
	/**
	 * 将一个字符串格式化成驼峰方式的类名对应的参数名
	 * @param str 字符串
	 * @param separator 分隔符
	 * @return 驼峰方式的类名对应的参数名
	 */
	public static String getParameterName(String str, String separator) {
		return makeFirstLetterLowerCase(getClassName(str, separator));
	}
	
	public static String getFullClassName(String packageName, String className) {
		return new StringBuilder().append(packageName).append(".").append(className).toString();
	}
	
	/**
	 * 将字符串格式化成使用下划线分隔的大写字母常量定义
	 * @param str 字符串
	 * @return 下划线分隔的大写常量定义
	 */
	public static String getConstantName(String str) {
		return getConstantName(str, DEFAULT_DIVIDER);
	}
	/**
	 * 将字符串格式化成使用下划线分隔的大写字母常量定义
	 * @param str 字符串
	 * @param separator 分隔符
	 * @return 下划线分隔的大写常量定义
	 */
	public static String getConstantName(String str, String separator) {
		String parameterName = getParameterName(str, separator);
		int length = parameterName.length();
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<length;i++) {
			char ch = parameterName.charAt(i);
			if (StrUtils.isUpperCase(ch)) {
				sb.append("_");
			}
			sb.append(ch);
		}
		return sb.toString().toUpperCase();
	}
	/**
	 * 对枚举列表设置枚举名处理器
	 * @param enumList 枚举列表
	 * @param nameRender 枚举名称处理器
	 */
	public static <T extends com.hkfs.fundamental.codegenerator.basis.data.Enum> List<T> setDictNameRender(List<T> enumList, NameRender nameRender) {
		for (Enum em : enumList) {
			em.setDictNameRender(nameRender);
		}
		return enumList;
	}
	
	/**
	 * 从完整类名中获取类名（不包含泛型）
	 * @param fullClassName 完整类名
	 * @return 类名（不包含泛型）
	 */
	public static String getClassNameWithoutGeneric(String fullClassName) {
		String className = getClassNameFromFullClassName(fullClassName);
		int index = className.indexOf("<");
		if (index != -1) {
			className = className.substring(0, index);
		}
		return className;
	}

	/**
	 * 将如com.test.Hello<com.baidu.OK>这样的泛型类格式化成Hello<OK>
	 * @param fullClassName
	 * @return
	 */
	public static String getClassNameWithGeneric(String fullClassName) {
		int index = fullClassName.lastIndexOf("<");
		if (index != -1) {
			String innerClassName = CodeUtils.getClassNameFromFullClassName(StrUtils.getMiddleText(fullClassName, "<", ">"));
			String outerClassName = CodeUtils.getClassNameFromFullClassName(fullClassName.substring(0, index));
			return outerClassName+"<"+innerClassName+">";
		}
		return CodeUtils.getClassNameFromFullClassName(fullClassName);
	}

	/**
	 * 打印
	 * @param outputs
	 */
	public static void print(Outputable[] outputs) {
		for (Outputable output : outputs) {
			System.out.println(output);
		}
	}

	/**
	 * 简单的格式化java代码
	 * @param code java代码
	 * @param tabCount 初始化的tab数量
	 * @return
	 */
	public static String formatJavaCode(String code, int tabCount) {
		String[] array = StrUtils.trim(code).split("\n");
		StringBuilder sb = new StringBuilder();
		for (String each : array) {
			each = StrUtils.trim(each);
			tabCount -= getChildCount(each, "}", true);
			for (int i = 0; i < tabCount; i++) {
				sb.append(Consts.TAB);
			}
			sb.append(each).append(Consts.LINE);
			tabCount += getChildCount(each, "{", false);
		}
		return sb.length() > 0 ? sb.substring(0, sb.length()-Consts.LINE.length()) : null;
	}

	/**
	 * 获取父字符串中以字串开头或以字串结尾的数量
	 * @param parent 父字符串
	 * @param child 子字符串
	 * @param isHead true表示开头，false表示结尾
	 * @return
	 */
	private static int getChildCount(String parent, String child, boolean isHead) {
		int count = 0;
		parent = StrUtils.trim(parent);
		while ((isHead && parent.startsWith(child)) || (!isHead && parent.endsWith(child))) {
			count++;
			parent = isHead ? StrUtils.trim(parent.substring(child.length())) :
					StrUtils.trim(parent.substring(0, parent.length() - child.length()));;
		}
		return count;
	}

	/**
	 * 批量设置类的包名
	 * @param array
	 * @param packageName
	 */
	public static void setPackageNames(Clazz[] array, String packageName) {
		for (Clazz clazz : array) {
			clazz.setPackageName(packageName);
		}
	}

	/**
	 * 批量追加注释
	 * @param array
	 * @param comment
	 */
	public static void appendComments(Clazz[] array, String comment) {
		for (Clazz clazz : array) {
			clazz.appendComment(comment);
		}
	}
}
