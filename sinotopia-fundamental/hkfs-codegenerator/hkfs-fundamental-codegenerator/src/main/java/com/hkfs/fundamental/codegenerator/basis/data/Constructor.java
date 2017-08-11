package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

import java.lang.*;

/**
 * 构造方法
 * @author zhoub 2015年10月30日 下午11:46:11
 */
public class Constructor extends Method {
	public Constructor(String fullClassName) {
		this(CodeUtils.getClassNameFromFullClassName(fullClassName), "", (Param[])null);
	}
	public Constructor(String fullClassName, Param[] params) {
		this(CodeUtils.getClassNameFromFullClassName(fullClassName), "", params);
	}
	public Constructor(Clazz cls) {
		this(CodeUtils.getClassNameFromFullClassName(cls.fullClassName), "", (Param[])null);
	}
	public Constructor(Clazz cls, Param[] params) {
		this(CodeUtils.getClassNameFromFullClassName(cls.fullClassName), "", params);
	}
	public Constructor(String fullClassName, Field[] fields) {
		this(CodeUtils.getClassNameFromFullClassName(fullClassName), "", AppendUtils.fieldsToParams(fields));
	}
	public Constructor(Clazz cls, Field[] fields) {
		this(CodeUtils.getClassNameFromFullClassName(cls.fullClassName), "", AppendUtils.fieldsToParams(fields));
	}
	public Constructor(String name, String fullClassName, Param[] params) {
		super(name, "", params);
	}
	
	@Override
	protected String processDefaultBody() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(AppendUtils.processSetterValue(params));
		return render(sb.toString());
	}
	
	public Constructor setComments(Comment[] comments) {
		super.setComments(comments);
		return this;
	}
	public Constructor setComment(Comment comment) {
		super.setComment(comment);
		return this;
	}
	public Constructor setAnnotations(Annotation[] annotations) {
		super.setAnnotations(annotations);
		return this;
	}
	public Constructor setAnnotation(Annotation annotation) {
		super.setAnnotations(new Annotation[]{annotation});
		return this;
	}
	public Constructor setAnnotation(String annotation) {
		super.setAnnotation(new Annotation(annotation));
		return this;
	}
	public Constructor setModifier(String modifier) {
		super.setModifier(modifier);
		return this;
	}
	public Constructor setReturnFullClassName(String returnFullClassName) {
		super.setReturnFullClassName(returnFullClassName);
		return this;
	}
	public Constructor setParams(Param[] params) {
		super.setParams(params);
		return this;
	}
	public Constructor setName(String name) {
		super.setName(name);
		return this;
	}
	public Constructor setBody(String body) {
		super.setBody(body);
		return this;
	}
//	public Constructor appendBody(String body) {
//		super.appendBody(body);
//		return this;
//	}
	
	public static void main(String[] args) {
		Constructor c = new Constructor("User", new Param[]{
				new Param("int", "age"),
				new Param("String", "name"),
		}).setModifier("public").setComments(new Comment[]{new Comment("构造方法")});
		
		System.out.println(c);
		
		c = new Constructor("Person").setModifier("public").setComments(new Comment[]{new Comment("构造方法")});
		System.out.println(c);
	}

}
