package com.sinotopia.fundamental.codegenerator.basis.data.base;

import com.sinotopia.fundamental.codegenerator.basis.data.Annotation;
import com.sinotopia.fundamental.codegenerator.basis.data.Comment;
import com.sinotopia.fundamental.codegenerator.basis.data.Method;
import com.sinotopia.fundamental.codegenerator.basis.global.Consts;
import com.sinotopia.fundamental.codegenerator.basis.utils.AppendUtils;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.io.Outputable;

public class AbsClass extends AbsFormat implements Outputable {
	public String fullClassName;//接口的完整类名
	public Comment[] comments;//类的注释
	public Annotation[] annotations;//类的注解
	public String modifier = Consts.CLASS_MODIFIER;//类的修饰符
	public Method[] methods;//类的方法
	public String[] imports;//手动导入的包
	
	public AbsClass(String fullClassName) {
		this(fullClassName, (Comment[])null, (Method[])null);
	}
	public AbsClass(String fullClassName, Comment[] comments) {
		this(fullClassName, comments, (Method[])null);
	}
	public AbsClass(String fullClassName, String[] comments) {
		this(fullClassName, new Comment[]{new Comment(comments)}, null);
	}
	public AbsClass(String fullClassName, Comment comment) {
		this(fullClassName, new Comment[]{comment}, (Method[])null);
	}
	public AbsClass(String fullClassName, Comment comment, Method[] methods) {
		this(fullClassName, new Comment[]{comment}, methods);
	}
	public AbsClass(String fullClassName, String comment) {
		this(fullClassName, new Comment[]{new Comment(comment)}, (Method[])null);
	}
	public AbsClass(String fullClassName, String comment, Method[] methods) {
		this(fullClassName, new Comment[]{new Comment(comment)}, methods);
	}
	public AbsClass(String fullClassName, Comment[] comments, Method[] methods) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		this.comments = comments;
		this.methods = methods;
	}
	
	protected String processPackage() {
		return AppendUtils.processPackage(getPackageName())+line();
	}
	public String getClassName() {
		return CodeUtils.getClassNameFromFullClassName(fullClassName);
	}
//	public String getClassNameWithoutGeneric() {
//		return CodeUtils.getClassNameWithoutGeneric(fullClassName);
//	}
	public String getPackageName() {
		return CodeUtils.getPackageNameFromFullClassName(fullClassName);
	}
	protected String processAnnotationClassImports() {
		return AppendUtils.processAnnotationClassImports(annotations);
	}
	protected String processMethodImports() {
		return AppendUtils.processMethodImports(methods);
	}
	protected String processComments() {
		return AppendUtils.processComments(comments);
	}
	protected String processTail() {
		return "}"+line();
	}
	@Override
	public String processOutputFileName() {
		return CodeUtils.getClassNameWithoutGeneric(fullClassName);
	}
	@Override
	public String processOutputExtensionName() {
		return "java";
	}
}
