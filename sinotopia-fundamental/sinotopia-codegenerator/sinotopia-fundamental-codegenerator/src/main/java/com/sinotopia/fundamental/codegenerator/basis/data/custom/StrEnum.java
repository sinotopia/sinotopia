package com.sinotopia.fundamental.codegenerator.basis.data.custom;

import com.sinotopia.fundamental.codegenerator.basis.data.Annotation;
import com.sinotopia.fundamental.codegenerator.basis.data.Comment;
import com.sinotopia.fundamental.codegenerator.basis.data.Constructor;
import com.sinotopia.fundamental.codegenerator.basis.data.Dict;
import com.sinotopia.fundamental.codegenerator.basis.data.Field;
import com.sinotopia.fundamental.codegenerator.basis.data.Method;
import com.sinotopia.fundamental.codegenerator.basis.data.Param;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

public class StrEnum extends CEnum {
	public StrEnum(String fullClassName, Dict[] dicts) {
		this(fullClassName, (Comment[])null, dicts);
	}
	public StrEnum(String fullClassName, Comment comment, Dict[] dicts) {
		this(fullClassName, new Comment[]{comment}, dicts);
	}
	public StrEnum(String fullClassName, String comment, Dict[] dicts) {
		this(fullClassName, new Comment(comment), dicts);
	}
	public StrEnum(String fullClassName, String[] comments, Dict[] dicts) {
		this(fullClassName, new Comment(comments), dicts);
	}
	public StrEnum(String fullClassName, Comment[] comments, Dict[] dicts) {
		super(fullClassName, comments, dicts);
	}
	
	@Override
	protected Constructor newConstructor() {
		return new Constructor(fullClassName, new Param[]{
			new Param("String", "value"),
			new Param("String", "comment"),
		}).setModifier("");
	}
	
	@Override
	protected Field[] newFields() {
		return new Field[]{
			new Field("value", "String", "值"),
			new Field("comment", "String", "注释"),
		};
	}
	
	@Override
	protected Method[] newMethods() {
		String className = CodeUtils.getClassNameFromFullClassName(fullClassName);
		return new Method[]{
			new Method("parse", className,
				new String[]{
					"根据值获取对应的枚举",
					"@param value 枚举的值",
					"@return 成功返回相应的枚举，否则返回null。"
				},
				new Param[]{
					new Param("String", "value")
				}).setModifier("public static").setBody(processMethodParseBody())
		};
	}
	private String processMethodParseBody() {
		String className = CodeUtils.getClassNameFromFullClassName(fullClassName);
		
		StringBuilder sb = new StringBuilder();
		sb.append(tab(2)+"if (value != null) {"+line());
		sb.append(tab(3)+className+"[] array = values();"+line());
		sb.append(tab(3)+"for ("+className+" each : array) {"+line());
		sb.append(tab(4)+"if (value.equals(each.value)) {"+line());
		sb.append(tab(5)+"return each;"+line());
		sb.append(tab(4)+"}"+line());
		sb.append(tab(3)+"}"+line());
		sb.append(tab(2)+"}"+line());
		sb.append(tab(2)+"return null;");
		return sb.toString();
	}
	
	public StrEnum setDicts(Dict[] dicts) {
		super.setDicts(dicts);
		return this;
	}
	public StrEnum setConstructor(Constructor constructor) {
		super.setConstructor(constructor);
		return this;
	}
	public StrEnum setFields(Field[] fields) {
		super.setFields(fields);
		return this;
	}
	public StrEnum setFullClassName(String fullClassName) {
		super.setFullClassName(fullClassName);
		return this;
	}
	public StrEnum setComments(Comment[] comments) {
		super.setComments(comments);
		return this;
	}
	public com.sinotopia.fundamental.codegenerator.basis.data.Enum setComment(Comment comment) {
		super.setComment(comment);
		return this;
	}
	public StrEnum setAnnotations(Annotation[] annotations) {
		super.setAnnotations(annotations);
		return this;
	}
	public StrEnum setAnnotation(Annotation annotation) {
		super.setAnnotations(new Annotation[]{annotation});
		return this;
	}
	public StrEnum setAnnotation(String annotation) {
		super.setAnnotation(new Annotation(annotation));
		return this;
	}
	public StrEnum setModifier(String modifier) {
		super.setModifier(modifier);
		return this;
	}
	public StrEnum setMethods(Method[] methods) {
		super.setMethods(methods);
		return this;
	}
	public StrEnum setIsInnerEnum(boolean isInnerEnum) {
		this.isInnerEnum = isInnerEnum;
		return this;
	}
	public static StrEnum clone(CEnum em) {
		return new StrEnum(em.fullClassName, em.comments, em.dicts);
	}
	
	public static void main(String[] args) {
		StrEnum c = new StrEnum("Status", new StrDict[]{
				new StrDict(1, "Normal", "正常"),
				new StrDict(2, "Publish", "已发布"),
				new StrDict(3, "Expired", "已过期"),
		}).setComments(new Comment[]{
				new Comment(new String[]{"这是一段测试的注释", "使用前请注意"})
		});
		System.out.println(c);
	}
	
	
}
