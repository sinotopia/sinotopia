package com.sinotopia.fundamental.codegenerator.basis.data;

import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsClass;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.CDict;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.CEnum;
import com.sinotopia.fundamental.codegenerator.basis.global.Config;
import com.sinotopia.fundamental.codegenerator.basis.global.Consts;
import com.sinotopia.fundamental.codegenerator.basis.render.FieldMethodRender;
import com.sinotopia.fundamental.codegenerator.basis.render.RelatedEnumFieldMethodRender;
import com.sinotopia.fundamental.codegenerator.basis.render.TabRender;
import com.sinotopia.fundamental.codegenerator.basis.utils.AppendUtils;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

import java.util.ArrayList;

/**
 * 类
 * @author zhoub 2015年10月28日 下午11:23:41
 */
public class Clazz extends AbsClass {
	public Clazz(String fullClassName) {
		this(fullClassName, null, null, null, null);
	}
	public Clazz(String fullClassName, Comment[] comments) {
		this(fullClassName, null, comments, null, null);
	}
	public Clazz(String fullClassName, String[] comments) {
		this(fullClassName, null, new Comment[]{new Comment(comments)}, null, null);
	}
	public Clazz(String fullClassName, Comment comment) {
		this(fullClassName, null, new Comment[]{comment}, null, null);
	}
	public Clazz(String fullClassName, String comment) {
		this(fullClassName, null, new Comment[]{new Comment(comment)}, null, null);
	}
	public Clazz(String fullClassName, String parentClassName, Comment[] comments) {
		this(fullClassName, new Clazz(parentClassName), comments, null, null);
	}
	public Clazz(String fullClassName, String parentClassName, String[] comments) {
		this(fullClassName, new Clazz(parentClassName), new Comment[]{new Comment(comments)}, null, null);
	}
	public Clazz(String fullClassName, String parentClassName, Comment comment) {
		this(fullClassName, new Clazz(parentClassName), new Comment[]{comment}, null, null);
	}
	public Clazz(String fullClassName, String parentClassName, String comment) {
		this(fullClassName, new Clazz(parentClassName), new Comment[]{new Comment(comment)}, null, null);
	}
	public Clazz(String fullClassName, Comment comment, Field[] fields) {
		this(fullClassName, null, new Comment[]{comment}, fields, null);
	}
	public Clazz(String fullClassName, String comment, Field[] fields) {
		this(fullClassName, null, new Comment[]{new Comment(comment)}, fields, null);
	}
	public Clazz(String fullClassName, String parentClassName, String comment, Field[] fields) {
		this(fullClassName, new Clazz(parentClassName), new Comment[]{new Comment(comment)}, fields, null);
	}
	
	public Clazz(String fullClassName, Clazz parentClass, Comment[] comments, Field[] fields, Method[] methods) {
		super(fullClassName, comments, methods);
		this.parentClass = parentClass;
		this.fields = fields;
	}
	
	public Clazz parentClass;//父类
	public Interface[] interfaces;//实现的接口
	public Field[] fields;//成员变量
	public Clazz[] innerClasses;//内部类
	public Enum[] innerEnums;//内部枚举
	public FieldMethodRender[] fieldMethodRenders;
	
	protected boolean hasGetterMethod = Consts.HAS_GETTER_METHOD;
	protected boolean hasSetterMethod = Consts.HAS_SETTER_METHOD;
	protected boolean isInnerClass;
	protected boolean isInterface;
	protected boolean isSerializable;
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		if (!isInnerClass) {
			sb.append(processPackage());
			sb.append(processImports());
		}
		sb.append(processComments());
		sb.append(processAnnotations());
		sb.append(processDefine());
		sb.append(processSerializableField());
		sb.append(processFields());
		
		sb.append(processRenderMethods());
		sb.append(processOverride());
		sb.append(processMethods());
		sb.append(processInnerClasses());
		sb.append(processInnerEnums());
		sb.append(processFieldInnerEnums());
		sb.append(processTail());
		return render(sb.toString());
	}
	
	protected String processMethods() {
		return AppendUtils.processMethods(methods);
	}
	
	protected String processRenderMethods() {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (hasGetterMethod) {
					sb.append(processGetterMethod(field));
				}
				if (hasSetterMethod) {
					sb.append(processSetterMethod(field));
				}
				if (fieldMethodRenders != null && fieldMethodRenders.length > 0) {
					for (FieldMethodRender render : fieldMethodRenders) {
						sb.append(render.render(field));
					}
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
	
	protected String processFields() {
		return AppendUtils.processFields(fields);
	}
	
	protected String processDefine() {
		StringBuilder sb = new StringBuilder();
		sb.append(AppendUtils.processModifier(modifier));
		if (isInterface) {
			sb.append("interface");
		}
		else {
			sb.append("class");
		}
		sb.append(" ").append(getClassName()).append(" ");
		if (parentClass != null) {
			sb.append("extends ").append(CodeUtils.getClassNameWithGeneric(parentClass.fullClassName)).append(" ");
		}
		if (interfaces != null && interfaces.length > 0) {
			sb.append("implements ");
			for (int i=0;i<interfaces.length-1;i++) {
				sb.append(CodeUtils.getClassNameWithGeneric(interfaces[i].fullClassName)).append(", ");
			}
			sb.append(CodeUtils.getClassNameWithGeneric(interfaces[interfaces.length - 1].fullClassName)).append(" ");
		}
		
		sb.append("{"+line());
		return sb.toString();
	}
	
	protected String processAnnotations() {
		return AppendUtils.processAnnotations(annotations);
	}
	
	protected String processSerializableField() {
		StringBuilder sb = new StringBuilder();
		if (isSerializable) {
			sb.append(processSerialVersionUID());
		}
		else {
			if (interfaces != null && interfaces.length > 0) {
				for (Interface in : interfaces) {
					if (CodeUtils.isSerializable(in.fullClassName)) {
						sb.append(processSerialVersionUID());
						break;
					}
				}
			}
		}
		return sb.toString();
	}
	
	protected String processSerialVersionUID() {
		return tab()+"private static final long serialVersionUID = 1L;"+line();
	}
	
	public String processImports() {
		StringBuilder sb = new StringBuilder();
		sb.append(processCustomImports());
		sb.append(processAnnotationClassImports());
		sb.append(processParentClassImport());
		sb.append(processInterfaceImports());
		sb.append(processFieldImports());
		sb.append(processMethodImports());
		sb.append(processInnerClassImports());
		sb.append(processInnerEnumImports());
		sb.append(processRelatedEnumImports());
		return AppendUtils.filterImports(sb.toString());
	}
	protected String processInnerClassImports() {
		return AppendUtils.processClassImports(innerClasses);
	}
	protected String processInnerEnumImports() {
		return AppendUtils.processInnerEnumImports(innerEnums);
	}
	protected String processParentClassImport() {
		return AppendUtils.processParentClassImport(parentClass);
	}
	protected String processInterfaceImports() {
		StringBuilder sb = new StringBuilder();
		sb.append(AppendUtils.processInterfaceImports(interfaces));
		if (interfaces != null) {
			for (Interface it : interfaces) {
				sb.append(AppendUtils.processMethodImports(it.methods));
			}
		}
		return sb.toString();
	}
	protected String processFieldImports() {
		return AppendUtils.processFieldImports(fields);
	}
	protected String processCustomImports() {
		return AppendUtils.processCustomImports(imports);
	}

	protected String processOverride() {
		StringBuilder sb = new StringBuilder();
		sb.append(processInterfacesOverride());
		sb.append(processClassesOverride());
		return sb.toString();
	}
	
	protected String processClassesOverride() {
		StringBuilder sb = new StringBuilder();
		if (parentClass != null && parentClass.isAbstract()) {
			Method[] methods = parentClass.methods;
			if (methods != null && methods.length > 0) {
				for(Method method : methods) {
					if (method.isAbstract()) {
						sb.append(method.getImplements(true));
					}
				}
			}
		}
		return sb.toString();
	}
	protected String processInterfacesOverride() {
		StringBuilder sb = new StringBuilder();
		if (interfaces != null && interfaces.length > 0) {
			for (Interface in : interfaces) {
				Method[] methods = in.methods;
				if (methods != null && methods.length > 0) {
					for(Method method : methods) {
						sb.append(method.getImplements(true));
					}
				}
			}
		}
		return sb.toString();
	}
	
	protected String processInnerClasses() {
		TabRender.newInstance().setTabRender(innerClasses);
		StringBuilder sb = new StringBuilder();
		if (innerClasses != null && innerClasses.length > 0) {
			for (Clazz cls : innerClasses) {
				sb.append(cls.setIsInnerClass(true));
			}
		}
		return sb.toString();
	}
	
	protected String processRelatedEnumImports() {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0 && hasRelatedEnumFieldMethod()) {
			for (Field field : fields) {
				if (field.relatedEnum != null) {
					sb.append(AppendUtils.processImport(field.relatedEnum.fullClassName));
				}
			}
		}	
		return sb.toString();
	}

	protected boolean hasRelatedEnumFieldMethod() {
		if (fieldMethodRenders != null && fieldMethodRenders.length > 0) {
			for (FieldMethodRender render : fieldMethodRenders) {
				if (render instanceof RelatedEnumFieldMethodRender) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected String processInnerEnums() {
		TabRender.newInstance().setTabRender(innerEnums);
		StringBuilder sb = new StringBuilder();
		if (innerEnums != null && innerEnums.length > 0) {
			for (Enum em : innerEnums) {
				sb.append(em.setIsInnerEnum(true));
			}
		}
		return sb.toString();
	}
	
	protected String processFieldInnerEnums() {
		StringBuilder sb = new StringBuilder();
		if (fields != null && fields.length > 0) {
			TabRender tabRender = TabRender.newInstance();
			for (Field field : fields) {
				if (field.innerEnum != null) {
					tabRender.setTabRender(field.innerEnum);
					sb.append(field.innerEnum.setIsInnerEnum(true));
				}
			}
		}
		return sb.toString();
	}
	
	public boolean isAbstract() {
		return CodeUtils.isAbstract(modifier);
	}
	
	public Clazz setParentClass(Clazz parentClass) {
		this.parentClass = parentClass;
		return this;
	}
	public Clazz setParentClassName(String className) {
		return this.setParentClass(new Clazz(className));
	}
	
	public Clazz setInterfaces(Interface[] interfaces) {
		this.interfaces = interfaces;
		return this;
	}
	public Clazz setInterface(Interface in) {
		this.interfaces = new Interface[]{in};
		return this;
	}
	public Clazz addInterface(Interface in) {
		return this.setInterfaces(CodeUtils.addNew(new ArrayList<Interface>(), interfaces!=null?interfaces:new Interface[0], in));
	}
	
	
	public Clazz setFields(Field[] fields) {
		this.fields = fields;
		return this;
	}
	public Clazz setField(Field field) {
		return this.setFields(new Field[]{field});
	}
	public Clazz addField(Field field) {
		return this.setFields(CodeUtils.addNew(new ArrayList<Field>(), fields!=null?fields:new Field[0], field));
	}

	public Clazz setFullClassName(String fullClassName) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		return this;
	}

	public Clazz setPackageName(String packageName) {
		this.fullClassName = CodeUtils.getFullClassName(packageName, getClassName());
		return this;
	}

	public Clazz setComments(Comment[] comments) {
		this.comments = comments;
		return this;
	}
	public Clazz setComment(Comment comment) {
		return this.setComments(new Comment[]{comment});
	}
	public Clazz setComment(String comment) {
		return this.setComment(new Comment(comment));
	}
	public Clazz setComments(String[] comments) {
		return this.setComment(new Comment(comments));
	}
	public Clazz addComment(Comment comment) {
		return this.setComments(CodeUtils.addNew(new ArrayList<Comment>(), comments!=null?comments:new Comment[0], comment));
	}
	public Clazz addComment(String comment) {
		return this.addComment(new Comment(comment));
	}
	public Clazz addComments(String[] comments) {
		return this.addComment(new Comment(comments));
	}

	public Clazz appendComment(Comment comment) {
		if (comments != null && comments.length > 0) {
			for (Comment each : comments) {
				each.addItems(comment.comments);
			}
			return this;
		}
		else {
			return this.setComments(CodeUtils.addNew(new ArrayList<Comment>(), comments!=null?comments:new Comment[0], comment));
		}
	}
	public Clazz appendComment(String comment) {
		return this.appendComment(new Comment(comment));
	}
	public Clazz appendComment(String[] comments) {
		return this.appendComment(new Comment(comments));
	}

	public Clazz setAnnotations(Annotation[] annotations) {
		this.annotations = annotations;
		return this;
	}
	public Clazz setAnnotation(Annotation annotation) {
		return this.setAnnotations(new Annotation[]{annotation});
	}
	public Clazz setAnnotation(String annotation) {
		return this.setAnnotation(new Annotation(annotation));
	}
	public Clazz addAnnotation(Annotation annotation) {
		return this.setAnnotations(CodeUtils.addNew(new ArrayList<Annotation>(), annotations!=null?annotations:new Annotation[0], annotation));
	}
	public Clazz addAnnotation(String annotation) {
		return this.addAnnotation(new Annotation(annotation));
	}
	
	
	public Clazz setModifier(String modifier) {
		this.modifier = modifier;
		return this;
	}
	
	
	public Clazz setMethods(Method[] methods) {
		this.methods = methods;
		return this;
	}
	public Clazz setMethod(Method method) {
		return this.setMethods(new Method[]{method});
	}
	public Clazz addMethod(Method method) {
		return this.setMethods(CodeUtils.addNew(new ArrayList<Method>(), methods!=null?methods:new Method[0], method));
	}
	public Clazz addConstructor(Constructor constructor) {
		return this.addMethod(constructor);
	}
	
	public Clazz setInnerClasses(Clazz[] innerClasses) {
		this.innerClasses = innerClasses;
		return this;
	}
	public Clazz addInnerClass(Clazz cls) {
		return this.setInnerClasses(CodeUtils.addNew(new ArrayList<Clazz>(), innerClasses!=null?innerClasses:new Clazz[0], cls));
	}
	
	
	
	
	public Clazz setInnerEnums(Enum[] innerEnums) {
		this.innerEnums = innerEnums;
		return this;
	}
	public Clazz addInnerEnum(Enum em) {
		return this.setInnerEnums(CodeUtils.addNew(new ArrayList<Enum>(), innerEnums!=null?innerEnums:new Enum[0], em));
	}
	public Clazz setHasGetterMethod(boolean hasGetterMethod) {
		this.hasGetterMethod = hasGetterMethod;
		return this;
	}
	public Clazz setHasSetterMethod(boolean hasSetterMethod) {
		this.hasSetterMethod = hasSetterMethod;
		return this;
	}
	public Clazz setIsSerializable(boolean isSerializable) {
		this.isSerializable = isSerializable;
		return this;
	}
	public Clazz setHasGetterSetterMethod(boolean hasGetterMethod) {
		return setHasGetterMethod(hasGetterMethod).setHasSetterMethod(hasGetterMethod);
	}
	public Clazz setIsInnerClass(boolean isInnerClass) {
		this.isInnerClass = isInnerClass;
		return this;
	}
	public Clazz setIsInterface(boolean isInterface) {
		this.isInterface = isInterface;
		return this;
	}
	public Clazz setFieldMethodRenders(FieldMethodRender[] fieldMethodRenders) {
		this.fieldMethodRenders = fieldMethodRenders;
		return this;
	}
	public Clazz addFieldMethodRender(FieldMethodRender fieldMethodRender) {
		return this.setFieldMethodRenders(CodeUtils.addNew(new ArrayList<FieldMethodRender>(), fieldMethodRenders!=null?fieldMethodRenders:new FieldMethodRender[0], fieldMethodRender));
	}
	public Clazz addImport(String fullClassName) {
		this.imports = CodeUtils.addNew(new ArrayList<String>(), imports!=null?imports:new String[0], fullClassName);
		return this;
	}
	
	
	
	public static void main(String[] args) {
		Clazz parent = new Clazz("com.baidu.jpush.JPush");
		Clazz cls = new Clazz("com.brucezee.tool.push.PushClient");
		cls.parentClass = parent;
		cls.modifier = "public";
		cls.comments = new Comment[]{
				new Comment(new String[]{"这是一段测试的注释", "使用前请注意"})
		};
		cls.annotations = new Annotation[] {
				new Annotation("@NotNull", "com.hakim.anno.NotNull"),
				new Annotation("@RequestMapping(\"user\")", "org.springframework.web.bind.annotation.RequestMapping"),
		};
		cls.fields = new Field[]{
				new Field("userId", "Long", "用户id").setModifier("private"),
				new Field("client", "com.baidu.Client[]", "客户端数组"),
				new Field("timeList", "List<Date>", "时间列表"),
		};
		cls.interfaces = new Interface[]{
				new Interface("com.buz.service.BusinessService"),
				new Interface("java.io.Serializable")
		};
		cls.methods = new Method[]{
				new Method("sayHi", "com.org.data.Person", new Param("Long", "userId"), "说Hello")
		};
		
//		System.out.println(cls);
		
		
		CEnum em = new CEnum("Status", new CDict[]{
				new CDict(1, "Normal", "正常"),
				new CDict(2, "Publish", "已发布"),
				new CDict(3, "Expired", "已过期"),
		}).setComments(new Comment[]{
				new Comment(new String[]{"这是一段测试的注释", "使用前请注意"})
		}).setModifier("public static");
		
		
		Clazz c = null;
		
		Config.addFullClass("com.brucezee.NotNull");
		
		c = new Clazz("com.baidu.User", "用户", new Field[]{
				new Field("id", "long", "用户id").setAnnotation(new Annotation("@NotNull", "NotNull")),
				new Field("username", "String", "用户名"),
				new Field("createdTime", "Date", "创建时间"),
		}).setHasGetterMethod(true).setHasSetterMethod(true);
		
		c.setInterfaces(new Interface[]{
				new Interface("UserService", "用户服务", new Method[]{
						new Method("register", "Boolean", new Param("String", "username")),
						new Method("login", "int"),
				}),
				
		});
		c.setInnerClasses(new Clazz[]{cls});
		c.setInnerEnums(new Enum[]{em});
		System.out.println(c);
		
		
//		c = new Class("com.baidu.User", new Comment(new String[]{"用户","注册"}));
//		System.out.println(c);
//		
//		c = new Class("com.baidu.User", "用户");
//		System.out.println(c);
//		
//		c = new Class("com.baidu.User");
//		System.out.println(c);
		
		
		
	}
}
