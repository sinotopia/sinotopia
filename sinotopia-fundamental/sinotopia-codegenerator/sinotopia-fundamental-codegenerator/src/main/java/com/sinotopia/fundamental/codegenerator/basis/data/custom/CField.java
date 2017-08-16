package com.sinotopia.fundamental.codegenerator.basis.data.custom;

import com.sinotopia.fundamental.codegenerator.basis.data.*;
import com.sinotopia.fundamental.codegenerator.basis.data.Enum;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

public class CField extends Field {
	public CField(String name, String fullClassName) {
		this(name, fullClassName, (Comment[])null, null);
	}
	public CField(String name, String fullClassName, Comment comment) {
		this(name, fullClassName, new Comment[]{comment}, null);
	}
	public CField(String name, String fullClassName, String comment) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, null);
	}
	public CField(String name, String fullClassName, Comment[] comments) {
		this(name, fullClassName, comments, null);
	}
	public CField(String name, String fullClassName, String comment, CDict[] dicts) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, new CEnum(CodeUtils.formatFullClassName(name), comment, dicts));
	}
	public CField(String name, String fullClassName, String comment, CEnum innerEnum) {
		this(name, fullClassName, new Comment[]{new Comment(comment)}, innerEnum);
	}
	public CField(String name, String fullClassName, Comment[] comments, CEnum innerEnum) {
		super(name, fullClassName, comments, innerEnum);
	}
	
	public CField setComments(Comment[] comments) {
		super.setComments(comments);
		return this;
	}
	public CField setComment(Comment comment) {
		super.setComments(new Comment[]{comment});
		return this;
	}
	public CField setAnnotations(Annotation[] annotations) {
		super.setAnnotations(annotations);
		return this;
	}
	public CField setAnnotation(Annotation annotation) {
		super.setAnnotations(new Annotation[]{annotation});
		return this;
	}
	public CField setAnnotation(String annotation) {
		super.setAnnotations(new Annotation[]{new Annotation(annotation)});
		return this;
	}
	public CField setModifier(String modifier) {
		super.setModifier(modifier);
		return this;
	}
	public CField setFullClassName(String fullClassName) {
		super.setFullClassName(fullClassName);
		return this;
	}
	public CField setEnum(Enum em) {
		super.setEnum(em);
		return this;
	}
	public CField setClass(Clazz cls) {
		super.setClass(cls);
		return this;
	}
	public CField setInterface(Interface in) {
		super.setInterface(in);
		return this;
	}
	public CField setName(String name) {
		super.setName(name);
		return this;
	}
	public CField setDefaultValue(String defaultValue) {
		super.setDefaultValue(defaultValue);
		return this;
	}
}
