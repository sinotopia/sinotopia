package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsClass;
import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

import java.util.ArrayList;

/**
 * 接口
 * @author zhoub 2015年10月28日 下午11:24:39
 */
public class Interface extends AbsClass  {
	public Interface(String fullClassName) {
		this(fullClassName, (Comment[])null, (Method[])null);
	}
	public Interface(String fullClassName, Comment[] comments) {
		this(fullClassName, comments, (Method[])null);
	}
	public Interface(String fullClassName, String[] comments) {
		this(fullClassName, new Comment[]{new Comment(comments)});
	}
	public Interface(String fullClassName, Comment comment) {
		this(fullClassName, new Comment[]{comment}, (Method[])null);
	}
	public Interface(String fullClassName, Comment comment, Method[] methods) {
		this(fullClassName, new Comment[]{comment}, methods);
	}
	public Interface(String fullClassName, String comment) {
		this(fullClassName, new Comment[]{new Comment(comment)});
	}
	public Interface(String fullClassName, String comment, Method[] methods) {
		this(fullClassName, new Comment[]{new Comment(comment)}, methods);
	}
	public Interface(String fullClassName, Comment[] comments, Method[] methods) {
		super(fullClassName, comments, methods);
	}
	
	public Interface[] parentInterfaces;//接口的父接口
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processPackage());
		sb.append(processImports());
		sb.append(processComments());
		sb.append(processAnnotations());
		sb.append(processDefine());
		sb.append(processMethodDefines());
		sb.append(processTail());
		return render(sb.toString());
	}
	
	protected String processMethodDefines() {
		return AppendUtils.processMethodDefines(methods);
	}
	
	protected String processAnnotations() {
		return AppendUtils.processAnnotations(annotations);
	}
	
	protected String processDefine() {
		StringBuilder sb = new StringBuilder();
		sb.append(AppendUtils.processModifier(modifier));
		sb.append("interface ").append(getClassName()).append(" ");
		if (parentInterfaces != null && parentInterfaces.length > 0) {
			sb.append("extends ");
			for (int i=0;i<parentInterfaces.length-1;i++) {
				sb.append(CodeUtils.getClassNameWithGeneric(parentInterfaces[i].fullClassName)).append(", ");
			}
			sb.append(CodeUtils.getClassNameWithGeneric(parentInterfaces[parentInterfaces.length - 1].fullClassName)).append(" ");
		}
		sb.append("{"+line());
		return sb.toString();
	}
	

	
	public String processImports() {
		StringBuilder sb = new StringBuilder();
		sb.append(processCustomImports());
		sb.append(processAnnotationClassImports());
		sb.append(processParentInterfaceImport());
		sb.append(processMethodImports());
		return AppendUtils.filterImports(sb.toString())+ AbsFormat.line();
	}
	protected String processParentInterfaceImport() {
		return AppendUtils.processInterfaceImports(parentInterfaces);
	}
	protected String processCustomImports() {
		return AppendUtils.processCustomImports(imports);
	}
	
	public Interface setParentInterfaces(Interface[] parentInterfaces) {
		this.parentInterfaces = parentInterfaces;
		return this;
	}
	public Interface setParentInterface(Interface parentInterface) {
		this.parentInterfaces = new Interface[]{parentInterface};
		return this;
	}
	public Interface setFullClassName(String fullClassName) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		return this;
	}
	public Interface setComments(Comment[] comments) {
		this.comments = comments;
		return this;
	}
	public Interface setComment(Comment comment) {
		this.comments = new Comment[]{comment};
		return this;
	}
	public Interface setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
		return this;
	}
	public Interface setAnnotation(Annotation annotation) {
		this.annotations = new Annotation[]{annotation};
		return this;
	}
	public Interface setAnnotation(String annotation) {
		this.annotations = new Annotation[]{new Annotation(annotation)};
		return this;
	}
	public Interface setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	public Interface setMethods(Method[] methods) {
		this.methods = methods;
		return this;
	}
	public Interface addImport(String fullClassName) {
		this.imports = CodeUtils.addNew(new ArrayList<String>(), imports!=null?imports:new String[0], fullClassName);
		return this;
	}
	public static void main(String[] args) {
		Interface it = new Interface("com.brucezee.Service");
		it.modifier = "public";
		it.methods = new Method[]{
				new Method("sayHi", "com.org.data.Person", new Param[]{
						new Param("String", "name"),
						new Param("int", "age"),
				}, "打招呼")
		};
		
		System.out.println(it);
	}
}
