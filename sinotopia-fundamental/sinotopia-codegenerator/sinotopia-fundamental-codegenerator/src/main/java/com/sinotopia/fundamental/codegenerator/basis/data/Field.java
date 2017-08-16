package com.sinotopia.fundamental.codegenerator.basis.data;

import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.sinotopia.fundamental.codegenerator.basis.global.Consts;
import com.sinotopia.fundamental.codegenerator.basis.render.TabRender;
import com.sinotopia.fundamental.codegenerator.basis.utils.AppendUtils;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;

/**
 * 成员变量
 * @author zhoub 2015年10月28日 下午11:24:02
 */
public class Field extends AbsFormat {
	public Comment[] comments;//成员变量的注释
	public Annotation[] annotations;//成员变量的注解
	public String modifier = Consts.FIELD_MODIFIER;//成员变量的修饰符
	public String fullClassName;//成员变量的完整类名或类型名
	public String name;//成员变量的名称
	public String defaultValue;//成员变量的默认值
	public Enum relatedEnum;//关联的枚举
	public Enum innerEnum;//内部枚举
	
	public Field(String name, String fullClassName) {
		this(name, fullClassName, (Comment[])null, null);
	}
	public Field(String name, String fullClassName, Comment comment) {
		this(name, fullClassName, new Comment[]{comment}, null);
	}
	public Field(String name, String fullClassName, String comment) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, null);
	}
	public Field(String name, String fullClassName, Comment[] comments) {
		this(name, fullClassName, comments, null);
	}
	public Field(String name, String fullClassName, String comment, Dict[] dicts) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, new Enum(CodeUtils.formatFullClassName(name), comment, dicts));
	}
	public Field(String name, String fullClassName, String comment, Enum innerEnum) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, innerEnum);
	}
	public Field(String name, String fullClassName, Comment[] comments, Enum innerEnum) {
		this.name = name;
		this.fullClassName = fullClassName;
		this.comments = comments;
		this.innerEnum = innerEnum;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processComments());
		sb.append(processAnnotations());
		sb.append(processDefine());
		return render(sb.toString());
	}
	
	protected String processDefine() {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		if (StrUtils.notEmpty(modifier)) {
			sb.append(modifier).append(" ");
		}
		sb.append(CodeUtils.getClassNameFromFullClassName(fullClassName)).append(" ");
		sb.append(name);
		if (StrUtils.notEmpty(defaultValue)) {
			sb.append(" = ").append(defaultValue);
		}
		sb.append(";"+line());
		return sb.toString();
	}
	protected String processComments() {
		TabRender.newInstance().setTabRender(comments);
		return AppendUtils.processComments(comments);
	}
	protected String processAnnotations() {
		TabRender.newInstance().setTabRender(annotations);
		return AppendUtils.processAnnotations(annotations);
	}
	
	
	public Field setComments(Comment[] comments) {
		this.comments = comments;
		return this;
	}
	public Field setComments(String[] comments) {
		this.comments = new Comment[]{new Comment(comments)};
		return this;
	}
	public Field setComment(Comment comment) {
		this.comments = new Comment[]{comment};
		return this;
	}
	public Field setComment(String comment) {
		this.comments = new Comment[]{new Comment(comment)};
		return this;
	}
	public Field addComment(Comment comment) {
		if (comments != null) {
			comments[0].addItems(comment.comments);
		}
		else {
			setComment(comment);
		}
		return this;
	}
	public Field addComment(String comment) {
		return this.addComment(new Comment(comment));
	}
	public Field addComments(String[] comments) {
		return this.addComment(new Comment(comments));
	}
	public Field setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
		return this;
	}
	public Field setAnnotation(Annotation annotation) {
		this.annotations = new Annotation[]{annotation};
		return this;
	}
	public Field setAnnotation(String annotation) {
		this.annotations = new Annotation[]{new Annotation(annotation)};
		return this;
	}
	public Field setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	public Field setFullClassName(String fullClassName) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		return this;
	}
	public Field setEnum(Enum em) {
		this.fullClassName = em.fullClassName;
		return this;
	}
	public Field setClass(Clazz cls) {
		this.fullClassName = cls.fullClassName;
		return this;
	}
	public Field setInterface(Interface in) {
		this.fullClassName = in.fullClassName;
		return this;
	}
	public Field setName(String name) {
		this.name = name;
		return this;
	}
	public Field setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}
	public Field setRelatedEnum(Enum em) {
		this.relatedEnum = em;
		return this;
	}
	public String getFieldUpperName() {
		return CodeUtils.makeFirstLetterUpperCase(name);
	}
	public String processRelatedEnumMethods() {
		StringBuilder sb = new StringBuilder();
		if (relatedEnum != null) {
			sb.append(relatedEnum.processFieldRelatedEnumMethods(this));
		}
		if (innerEnum != null) {
			sb.append(innerEnum.processFieldRelatedEnumMethods(this));
		}
		return sb.toString();
	}
}
