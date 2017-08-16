package com.sinotopia.fundamental.codegenerator.basis.utils;

import com.sinotopia.fundamental.codegenerator.basis.data.Enum;
import com.sinotopia.fundamental.codegenerator.basis.data.*;
import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;
import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.sinotopia.fundamental.codegenerator.basis.global.Config;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AppendUtils {
	public static String processPackage(String packageName) {
		if (StrUtils.notEmpty(packageName)) {
			return "package "+packageName+";"+ AbsFormat.line();
		}
		return "";
	}
	public static String processImport(String fullClassName) {
		StringBuilder sb = new StringBuilder();
		if (fullClassName.endsWith("[]")) {
			fullClassName = fullClassName.substring(0, fullClassName.length()-2);
		}
		if (fullClassName.contains("<")) {
			//泛型类
			String innerClassName = StrUtils.getMiddleText(fullClassName, "<", ">");
			if (StrUtils.notEmpty(innerClassName)) {
				if (innerClassName.contains(",")) {
					String[] innerClasses = innerClassName.split(",");
					for (String innerClass : innerClasses) {
						String innerClassImport = processImport(innerClass);
						if (StrUtils.notEmpty(innerClassImport)) {
							sb.append(innerClassImport);
						}
					}
				}
				else {
					String innerClassImport = processImport(innerClassName);
					if (StrUtils.notEmpty(innerClassImport)) {
						sb.append(innerClassImport);
					}
				}
			}
			
			fullClassName = StrUtils.replaceWholeText(fullClassName, "<", ">", "");
		}
		
		if (Config.isImportClass(fullClassName)) {
			String name = Config.getImportFullClassName(fullClassName);
			if (StrUtils.notEmpty(name)) {
				sb.append("import ").append(name).append(";"+AbsFormat.line());
			}
		}
		else if (CodeUtils.isFullClassClass(fullClassName)) {
			if (!Config.isBasicClass(fullClassName)) {
				sb.append("import ").append(fullClassName).append(";"+AbsFormat.line());
			}
		}
		return sb.toString();
	}
	
	public static String processComments(Comment[] comments) {
		StringBuilder sb = new StringBuilder();
		if (comments != null && comments.length > 0) {
			for (Comment comment : comments) {
				sb.append(comment);
			}
		}
		return sb.toString();
	}
	public static String processAnnotations(Annotation[] annotations) {
		StringBuilder sb = new StringBuilder();
		if (annotations != null && annotations.length > 0) {
			for (Annotation annotation : annotations) {
				sb.append(annotation);
			}
		}
		return sb.toString();
	}
	public static String processMethods(Method[] methods) {
		StringBuilder sb = new StringBuilder();
		if (methods != null && methods.length > 0) {
			for (Method method : methods) {
				sb.append(method);
			}
		}
		return sb.toString();
	}
	public static String processMethodDefines(Method[] methods) {
		StringBuilder sb = new StringBuilder();
		if (methods != null && methods.length > 0) {
			for (Method method : methods) {
				sb.append(method.getDefine());
			}
		}
		return sb.toString();
	}
	public static String processFields(Field[] fields) {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				sb.append(field);
			}
		}
		return sb.toString();
	}
	public static String processDicts(Dict[] dicts) {
		StringBuilder sb = new StringBuilder();
		if (dicts != null && dicts.length > 0) {
			for (int i=0;i<dicts.length-1;i++) {
				sb.append(dicts[i]).append(","+AbsFormat.line());
			}
			sb.append(dicts[dicts.length-1]).append(";"+AbsFormat.line());
		}
		return sb.toString();
	}
	public static String processParams(Param[] params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length > 0) {
			for (int i=0;i<params.length-1;i++) {
				sb.append(params[i]).append(", ");
			}
			sb.append(params[params.length-1]);
		}
		return sb.toString();
	}
	public static String filterImports(String allImports) {
		StringBuilder sb = new StringBuilder();
		if (StrUtils.notEmpty(allImports)) {
			String[] imports = allImports.split(AbsFormat.line());
			Arrays.sort(imports);
			HashSet<String> set = new HashSet<String>();
			for (String im : imports) {
				if (set.add(im)) {
					sb.append(im).append(AbsFormat.line());
				}
			}
		}
		return sb.toString();
	}
	
	public static String processAnnotationClassImports(Annotation[] annotations) {
		StringBuilder sb = new StringBuilder();
		if (annotations != null && annotations.length > 0) {
			for (Annotation annotation : annotations) {
				if (StrUtils.notEmpty(annotation.fullClassName)) {
					if (annotation.fullClassName.startsWith("@")) {
						sb.append(AppendUtils.processImport(annotation.fullClassName.substring(1)));
					}
					else {
						sb.append(AppendUtils.processImport(annotation.fullClassName));
					}
				}
				else {
					if (annotation.name.startsWith("@")) {
						sb.append(AppendUtils.processImport(annotation.name.substring(1)));
					}
					else {
						sb.append(AppendUtils.processImport(annotation.name));
					}
				}
			}
		}
		return sb.toString();
	}
	public static String processInterfaceImports(Interface[] interfaces) {
		StringBuilder sb = new StringBuilder();
		if (interfaces != null && interfaces.length > 0) {
			for (Interface it : interfaces) {
				sb.append(AppendUtils.processImport(it.fullClassName));
			}
		}
		return sb.toString();
	}
	public static String processFieldImports(Field[] fields) {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				sb.append(AppendUtils.processImport(field.fullClassName));
				sb.append(AppendUtils.processAnnotationClassImports(field.annotations));
			}
		}
		return sb.toString();
	}
	public static String processCustomImports(String[] imports) {
		StringBuilder sb = new StringBuilder();
		if (imports != null && imports.length > 0) {
			for (String im : imports) {
				sb.append(AppendUtils.processImport(im));
			}
		}
		return sb.toString();
	}
	public static String processParamImports(Param[] params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length > 0) {
			for (Param param : params) {
				sb.append(AppendUtils.processImport(param.fullClassName));
			}
		}
		return sb.toString();
	}
	public static String processMethodImports(Method[] methods) {
		StringBuilder sb = new StringBuilder();
		if (methods != null && methods.length > 0) {
			for (Method method : methods) {
				sb.append(AppendUtils.processImport(method.returnFullClassName));
				sb.append(AppendUtils.processParamImports(method.params));
			}
		}
		return sb.toString();
	}
	public static String processClassImports(Clazz[] classes) {
		StringBuilder sb = new StringBuilder();
		if (classes != null && classes.length > 0) {
			for (Clazz cls : classes) {
				sb.append(cls.processImports());
			}
		}
		return AppendUtils.filterImports(sb.toString());
	}
	public static String processInnerEnumImports(Enum[] enums) {
		StringBuilder sb = new StringBuilder();
		if (enums != null && enums.length > 0) {
			for (Enum em : enums) {
				sb.append(em.processImports());
			}
		}
		return AppendUtils.filterImports(sb.toString());
	}
	public static String processSetterValue(Param[] params) {
		StringBuilder sb = new StringBuilder();
		if (params != null && params.length > 0) {
			for (Param param : params) {
				sb.append(param.processSetterValue());
			}
		}
		return sb.toString();
	}
	//this.value = value;
	public static String processSetterValue(String paramName) {
		StringBuilder sb = new StringBuilder();
		sb.append(AbsFormat.tab(2)+"this.").append(paramName).append(" = ").append(paramName).append(";"+AbsFormat.line());
		return sb.toString();
	}
	public static String processModifier(String modifier) {
		StringBuilder sb = new StringBuilder();
		if (StrUtils.notEmpty(modifier)) {
			sb.append(modifier).append(" ");
		}
		return sb.toString();
	}
	public static String processParentClassImport(Clazz parentClass) {
		return parentClass != null ? AppendUtils.processImport(parentClass.fullClassName) : "";
	}
	
	public static String processSetterMethod(Field field) {
		StringBuilder sb = new StringBuilder();
		sb.append(AbsFormat.tab()+"public void set").append(field.getFieldUpperName()).append("(");
		sb.append(CodeUtils.getClassNameFromFullClassName(field.fullClassName)).append(" ").append(field.name).append(") {"+AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"this.").append(field.name).append(" = ").append(field.name).append(";"+AbsFormat.line());
		sb.append(AbsFormat.tab()+"}"+AbsFormat.line());
		return sb.toString();
	}
	public static String processGetterMethod(Field field) {
		StringBuilder sb = new StringBuilder();
		sb.append(AbsFormat.tab()+"public ").append(CodeUtils.getClassNameFromFullClassName(field.fullClassName));
		sb.append(" get").append(field.getFieldUpperName()).append("() {"+AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"return this.").append(field.name).append(";"+AbsFormat.line());
		sb.append(AbsFormat.tab()+"}"+AbsFormat.line());
		return sb.toString();
	}
	public static String processDefaultReturn(String returnFullClassName) {
		StringBuilder sb = new StringBuilder();
		if (StrUtils.notEmpty(returnFullClassName)) {
			String defaultValue = Config.getBasicTypeDefaultValue(returnFullClassName);
			sb.append(AbsFormat.tab(2));
			if (StrUtils.notEmpty(defaultValue)) {
				sb.append("return ").append(defaultValue).append(";");
			}
		}
		sb.append(AbsFormat.line());
		return sb.toString();
	}
	
	public static Param fieldToParam(Field field) {
		return new Param(field.fullClassName, field.name);
	}
	public static Param[] fieldsToParams(Field[] fields) {
		if (fields != null) {
			List<Param> list = new ArrayList<Param>(fields.length);
			for (Field field : fields) {
				list.add(fieldToParam(field));
			}
			return list.toArray(new Param[list.size()]);
		}
		return null;
	}
}
