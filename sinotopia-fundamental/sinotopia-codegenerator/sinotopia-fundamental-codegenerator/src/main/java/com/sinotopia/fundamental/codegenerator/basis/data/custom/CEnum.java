package com.hkfs.fundamental.codegenerator.basis.data.custom;

import com.hkfs.fundamental.codegenerator.basis.data.*;
import com.hkfs.fundamental.codegenerator.basis.data.Enum;
import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.render.Initializer;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

public class CEnum extends Enum {
	public CEnum(String fullClassName, Dict[] dicts) {
		this(fullClassName, (Comment[])null, dicts);
	}
	public CEnum(String fullClassName, Comment comment, Dict[] dicts) {
		this(fullClassName, new Comment[]{comment}, dicts);
	}
	public CEnum(String fullClassName, String comment, Dict[] dicts) {
		this(fullClassName, new Comment(comment), dicts);
	}
	public CEnum(String fullClassName, String[] comments, Dict[] dicts) {
		this(fullClassName, new Comment(comments), dicts);
	}
	public CEnum(String fullClassName, Comment[] comments, Dict[] dicts) {
		super(fullClassName, comments, dicts);
		setInitializer(new Initializer() {
			@Override
			public void init(AbsFormat format) {
				CEnum c = (CEnum) format;
				c.setConstructor(newConstructor());
				c.setFields(newFields());
				c.setMethods(newMethods());
			}
		});
	}
	
	protected Constructor newConstructor() {
		return new Constructor(fullClassName, new Param[]{
			new Param("int", "value"),
			new Param("String", "comment"),
		}).setModifier("");
	}
	
	protected Field[] newFields() {
		return new Field[]{
			new Field("value", "int", "值").setModifier("public"),
			new Field("comment", "String", "注释").setModifier("public"),
		};
	}
	
	protected Method[] newMethods() {
		String className = CodeUtils.getClassNameFromFullClassName(fullClassName);
		return new Method[]{
			new Method("parse", className,
				new String[]{
					"根据值获取对应的枚举",
					"@param value 枚举的数值",
					"@return 成功返回相应的枚举，否则返回null。"
				},
				new Param[]{
					new Param("Integer", "value")
				}).setModifier("public static").setBody(processMethodParseBody())
		};
	}
	private String processMethodParseBody() {
		String className = CodeUtils.getClassNameFromFullClassName(fullClassName);
		
		StringBuilder sb = new StringBuilder();
		sb.append(AbsFormat.tab(2)+"if (value != null) {"+ AbsFormat.line());
		sb.append(AbsFormat.tab(3)+className+"[] array = values();"+ AbsFormat.line());
		sb.append(AbsFormat.tab(3)+"for ("+className+" each : array) {"+ AbsFormat.line());
		sb.append(AbsFormat.tab(4)+"if (value == each.value) {"+ AbsFormat.line());
		sb.append(AbsFormat.tab(5)+"return each;"+ AbsFormat.line());
		sb.append(AbsFormat.tab(4)+"}"+ AbsFormat.line());
		sb.append(AbsFormat.tab(3)+"}"+ AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"}"+ AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"return null;");
		return sb.toString();
	}
	
	public CEnum setDicts(Dict[] dicts) {
		super.setDicts(dicts);
		return this;
	}
	public CEnum setConstructor(Constructor constructor) {
		super.setConstructor(constructor);
		return this;
	}
	public CEnum setFields(Field[] fields) {
		super.setFields(fields);
		return this;
	}
	public CEnum setFullClassName(String fullClassName) {
		super.setFullClassName(fullClassName);
		return this;
	}
	public CEnum setComments(Comment[] comments) {
		super.setComments(comments);
		return this;
	}
	public Enum setComment(Comment comment) {
		super.setComment(comment);
		return this;
	}
	public CEnum setAnnotations(Annotation[] annotations) {
		super.setAnnotations(annotations);
		return this;
	}
	public CEnum setAnnotation(Annotation annotation) {
		super.setAnnotations(new Annotation[]{annotation});
		return this;
	}
	public CEnum setAnnotation(String annotation) {
		super.setAnnotation(new Annotation(annotation));
		return this;
	}
	public CEnum setModifier(String modifier) {
		super.setModifier(modifier);
		return this;
	}
	public CEnum setMethods(Method[] methods) {
		super.setMethods(methods);
		return this;
	}
	public CEnum setIsInnerEnum(boolean isInnerEnum) {
		this.isInnerEnum = isInnerEnum;
		return this;
	}
	
	@Override
	public String processFieldRelatedEnumMethods(Field field) {
		StringBuilder sb = new StringBuilder();
		String enumName = CodeUtils.getClassNameFromFullClassName(fullClassName);
		String fieldUpperName = field.getFieldUpperName();
		sb.append(AbsFormat.tab()+"public ").append(enumName).append(" find").append(fieldUpperName).append("Enum() {"+AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"return ").append(enumName).append(".parse(").append(field.name).append(");"+AbsFormat.line());
		sb.append(AbsFormat.tab()+"}"+AbsFormat.line()+ AbsFormat.tab()+"public void put").append(fieldUpperName).append("Enum(").append(enumName).append(" ").append(field.name).append(") {"+AbsFormat.line());
		sb.append(AbsFormat.tab(2)+"this.").append(field.name).append(" = ").append(field.name).append(" != null ? ").append(field.name).append(".value : null;"+AbsFormat.line());
		sb.append(AbsFormat.tab()+"}"+AbsFormat.line());
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		CEnum c = new CEnum("Status", new CDict[]{
				new CDict(1, "Normal", "正常"),
				new CDict(2, "Publish", "已发布"),
				new CDict(3, "Expired", "已过期"),
		}).setComments(new Comment[]{
				new Comment(new String[]{"这是一段测试的注释", "使用前请注意"})
		});
		System.out.println(c);
	}
	
}
