package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.render.TabRender;
import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

/**
 * 方法
 * @author zhoub 2015年10月28日 下午11:24:45
 */
public class Method extends AbsFormat {
	public Comment[] comments;//方法的注释
	public Annotation[] annotations;//方法的注解
	public String modifier = Consts.METHOD_MODIFIER;//方法的修饰符
	public String returnFullClassName;//方法的返回的完整类名或类型名
	public Param[] params;//方法的参数
	public String name;//方法的名称
	public String body;//方法的内容
	
//	protected boolean appendBody;
	
	public Method(String name, String returnFullClassName) {
		this(name, returnFullClassName, (Comment[])null, (Param[])null);
	}
	public Method(String name, String returnFullClassName, String comment) {
		this(name, returnFullClassName, new Comment[]{new Comment(comment)}, (Param[])null);
	}
	public Method(String name, String returnFullClassName, Comment[] comments) {
		this(name, returnFullClassName, comments, (Param[])null);
	}
	public Method(String name, String returnFullClassName, String[] comments) {
		this(name, returnFullClassName, new Comment[]{new Comment(comments)}, (Param[])null);
	}
	public Method(String name, String returnFullClassName, String[] comments, Param[] params) {
		this(name, returnFullClassName, new Comment[]{new Comment(comments)}, params);
	}
	public Method(String name, String returnFullClassName, Param[] params) {
		this(name, returnFullClassName, (Comment[])null, params);
	}
	public Method(String name, String returnFullClassName, Param[] params, String comment) {
		this(name, returnFullClassName, new Comment[]{new Comment(comment)}, params);
	}
	public Method(String name, String returnFullClassName, Param param) {
		this(name, returnFullClassName, (Comment[])null, new Param[]{param});
	}
	public Method(String name, String returnFullClassName, Param param, String comment) {
		this(name, returnFullClassName, new Comment[]{new Comment(comment)}, new Param[]{param});
	}
	public Method(String name, String returnFullClassName, Param param, Comment[] comments) {
		this(name, returnFullClassName, comments, new Param[]{param});
	}
	
	public Method(String name, String returnFullClassName, Comment[] comments, Param[] params) {
		this.name = name;
		this.returnFullClassName = returnFullClassName;
		this.comments = comments;
		this.params = params;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processComments());
		sb.append(processAnnotations());
		sb.append(processDefine(false));
		sb.append(processBody());
		sb.append(processTail());
		return render(sb.toString());
	}
	
	public String getDefine() {
		StringBuilder sb = new StringBuilder();
		sb.append(processComments());
		sb.append(processAnnotations());
		sb.append(processDefine(true));
		return sb.toString();
	}
	public String getImplements(boolean override) {
		StringBuilder sb = new StringBuilder();
		if (override) {
			sb.append(tab()+"@Override"+line());
		}
		sb.append(processDefine(!override));
		sb.append(processBody());
		sb.append(processTail());
		return sb.toString();
	}
	
	protected String processDefine(boolean isDefine) {
		StringBuilder sb = new StringBuilder();
		sb.append(tab());
		if (StrUtils.notEmpty(modifier)) {
			sb.append(modifier).append(" ");
		}
		String returnClassName = CodeUtils.getClassNameWithGeneric(returnFullClassName);
		if (StrUtils.notEmpty(returnClassName)) {
			sb.append(returnClassName).append(" ");
		}
		sb.append(name).append("(");
		sb.append(AppendUtils.processParams(params)).append(")");
		sb.append(isDefine?";":" {");
		sb.append(line());
		return sb.toString();
	}
	protected String processBody() {
		StringBuilder sb = new StringBuilder();
		if (StrUtils.notEmpty(body)) {
//			if (appendBody) {
//				sb.append(processDefaultBody());
//			}
			sb.append(body+line());
		}
		else {
			sb.append(processDefaultBody());
		}
		return sb.toString();
	}
	protected String processTail() {
		return tab()+"}"+line();
	}
	protected String processDefaultBody() {
		return AppendUtils.processDefaultReturn(returnFullClassName);
	}
	
	public boolean isAbstract() {
		return CodeUtils.isAbstract(modifier);
	}
	
	protected String processComments() {
		TabRender.newInstance().setTabRender(comments);
		return AppendUtils.processComments(comments);
	}
	protected String processAnnotations() {
		TabRender.newInstance().setTabRender(annotations);
		return AppendUtils.processAnnotations(annotations);
	}
	
//	public Method appendBody(String body) {
//		this.appendBody = true;
//		this.body = body;
//		return this;
//	}
	public Method setComments(Comment[] comments) {
		this.comments = comments;
		return this;
	}
	public Method setComments(String[] comments) {
		this.comments = new Comment[]{new Comment(comments)};
		return this;
	}
	public Method setComment(Comment comment) {
		this.comments = new Comment[]{comment};
		return this;
	}
	public Method setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
		return this;
	}
	public Method setAnnotation(Annotation annotation) {
		this.annotations = new Annotation[]{annotation};
		return this;
	}
	public Method setAnnotation(String annotation) {
		this.annotations = new Annotation[]{new Annotation(annotation)};
		return this;
	}
	public Method setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	public Method setReturnFullClassName(String returnFullClassName) {
		this.returnFullClassName = returnFullClassName;
		return this;
	}
	public Method setParams(Param[] params) {
		this.params = params;
		return this;
	}
	public Method setName(String name) {
		this.name = name;
		return this;
	}
	public Method setBody(String body) {
		this.body = body;
		return this;
	}
}
