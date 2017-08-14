package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsClass;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.render.NameRender;
import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.basis.validate.UniqueValidator;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

/**
 * 枚举
 * @author zhoub 2015年10月28日 下午11:25:42
 */
public class Enum extends AbsClass {
	public Dict[] dicts;//枚举项
	public Constructor constructor;//枚举的构造方法
	public Field[] fields;//枚举的成员变量
	
	protected boolean hasGetterMethod = Consts.HAS_GETTER_METHOD;
	protected boolean hasSetterMethod = Consts.HAS_SETTER_METHOD;
	
	public Enum(String fullClassName, Dict[] dicts) {
		this(fullClassName, (Comment[])null, dicts);
	}
	public Enum(String fullClassName, Comment comment, Dict[] dicts) {
		this(fullClassName, new Comment[]{comment}, dicts);
	}
	public Enum(String fullClassName, String comment, Dict[] dicts) {
		this(fullClassName, new Comment(comment), dicts);
	}
	public Enum(String fullClassName, String[] comments, Dict[] dicts) {
		this(fullClassName, new Comment(comments), dicts);
	}
	public Enum(String fullClassName, Comment[] comments, Dict[] dicts) {
		super(fullClassName, comments);
		this.dicts = dicts;
		validate(dicts);
	}
	
	protected void validate(Dict[] dicts) {
		if (dicts != null && dicts.length > 0) {
			new UniqueValidator().validate(dicts);
		}
	}
	
	protected boolean isInnerEnum;
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		if (!isInnerEnum) {
			sb.append(processPackage());
			sb.append(processImports());
		}
		sb.append(processComments());
		sb.append(processDefine());
		sb.append(processDicts());
		sb.append(processFields());
		sb.append(processGetterSetterMethods());
		sb.append(processConstructor());
		sb.append(processMethods());
		sb.append(processTail());
		return render(sb.toString());
	}
	
	protected String processConstructor() {
		StringBuilder sb = new StringBuilder();
		if (constructor != null) {
			sb.append(constructor);
		}
		return sb.toString();
	}
	
	protected String processMethods() {
		return AppendUtils.processMethods(methods);
	}
	
	protected String processFields() {
		return AppendUtils.processFields(fields);
	}
	
	protected String processDicts() {
		return AppendUtils.processDicts(dicts);
	}
	
	public String processImports() {
		StringBuilder sb = new StringBuilder();
		sb.append(processAnnotationClassImports());
		sb.append(processFieldImports());
		sb.append(processMethodImports());
		return AppendUtils.filterImports(sb.toString());
	}
	
	protected String processDefine() {
		StringBuilder sb = new StringBuilder();
		sb.append(AppendUtils.processModifier(modifier));
		sb.append("enum ").append(getClassName()).append(" {"+line());
		return sb.toString();
	}
	
	protected String processFieldImports() {
		return AppendUtils.processFieldImports(fields);
	}
	
	private Object processGetterSetterMethods() {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (hasGetterMethod) {
					sb.append(processSetterMethod(field));
				}
				if (hasSetterMethod) {
					sb.append(processGetterMethod(field));
				}
			}
		}
		return sb.toString();
	}
	
	protected String processSetterMethod(Field field) {
		return AppendUtils.processSetterMethod(field);
	}
	
	protected String processGetterMethod(Field field) {
		return AppendUtils.processGetterMethod(field);
	}
	
	public Enum setDicts(Dict[] dicts) {
		this.dicts = dicts;
		return this;
	}
	public Enum setConstructor(Constructor constructor) {
		this.constructor = constructor;
		return this;
	}
	public Enum setFields(Field[] fields) {
		this.fields = fields;
		return this;
	}
	public Enum setFullClassName(String fullClassName) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		return this;
	}
	public Enum setComments(Comment[] comments) {
		this.comments = comments;
		return this;
	}
	public Enum setComment(Comment comment) {
		this.comments = new Comment[]{comment};
		return this;
	}
	public Enum setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
		return this;
	}
	public Enum setAnnotation(Annotation annotation) {
		this.annotations = new Annotation[]{annotation};
		return this;
	}
	public Enum setAnnotation(String annotation) {
		this.annotations = new Annotation[]{new Annotation(annotation)};
		return this;
	}
	public Enum setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	public Enum setMethods(Method[] methods) {
		this.methods = methods;
		return this;
	}
	public Enum setIsInnerEnum(boolean isInnerEnum) {
		this.isInnerEnum = isInnerEnum;
		return this;
	}
	
	public Enum setHasGetterMethod(boolean hasGetterMethod) {
		this.hasGetterMethod = hasGetterMethod;
		return this;
	}
	public Enum setHasSetterMethod(boolean hasSetterMethod) {
		this.hasSetterMethod = hasSetterMethod;
		return this;
	}
	public Enum setHasGetterSetterMethod(boolean hasGetterMethod) {
		return setHasGetterMethod(hasGetterMethod).setHasSetterMethod(hasGetterMethod);
	}
	public String processFieldRelatedEnumMethods(Field field) {
		return "";
	}
	public Enum setDictNameRender(NameRender nameRender) {
		if (dicts != null && dicts.length > 0) {
			for (Dict dict : dicts) {
				dict.setNameRender(nameRender);
			}
		}
		return this;
	}
	public static void main(String[] args) {
		Enum em = new Enum("com.brucezee.tool.dict.UserType", new Dict[]{
				new Dict("Admin", new Object[]{1, "管理员"}),
				new Dict("Normal", new Object[]{2, "普通人员"}),
		})
		.setModifier("public")
		.setComments(new Comment[]{
				new Comment(new String[]{"这是一段测试的注释", "使用前请注意"})
		})
		.setAnnotations(new Annotation[] {
				new Annotation("@NotNull", "com.hakim.anno.NotNull")
		})
		.setFields(new Field[]{
				new Field("code", "int", new Comment("编号")).setModifier("public"),
				new Field("label", "String", "内容"),
				new Field("serialVersionUID", "long", "序列号").setModifier("private static final").setDefaultValue("1L"),
		})
		.setConstructor(new Constructor("UserType", new Param[]{
				new Param("int", "code"),
				new Param("String", "label")}));
		
		System.out.println(em);
	}
}
