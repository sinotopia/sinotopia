package com.hkfs.fundamental.codegenerator.test;


import com.hkfs.fundamental.codegenerator.basis.data.*;
import com.hkfs.fundamental.codegenerator.basis.data.Class;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.output.MultiCodeOutputer;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.*;

public class ClassDefination {
	public static Controller[] customerControllers;

	public static Controller[] consumeControllers;

	public static Class[] classes;

	static {
		customerControllers = new Controller[]{
			new Controller("CustomerController", "/user", "CustomerService", "用户服务", new ControllerMethod[] {
					new ControllerMethod("queryUser", "查询用户列表", "ObjectResultEx",
							new Field[] {
									new Field("id", "Long", "用户id"),
							},
							new Field[] {
									new Field("id", "Long", "用户id"),
							}
					)}
			),
		};

		consumeControllers = new Controller[]{
				new Controller("ConsumeController", "/consume", "ConsumeService", "提现服务", new ControllerMethod[] {
						new ControllerMethod("apply", "用户申请提现接口", "ResultEx",
								new Field[] {
										new Field("verifyCode", "String", "验证码"),
										new Field("consumeAmount", "Double", "提现额度"),
										new Field("couponId", "Long", "红包ID"),
										new Field("bankCardId", "Long", "银行卡ID"),
										new Field("investorId", "Long", "出资方ID"),
										new Field("term", "Integer", "分期期数")
								},
								new Field[] {
								}
						).pageNeedLogin(),


						new ControllerMethod("appApplyCheck", "app申请校验接口", "ObjectResultEx",
								new Field[] {
										new Field("consumeAmount", "Double", "提现额度"),
								},
								new Field[] {
										new Field("checkStatus", "Integer", "校验结果").setRelatedEnum(TypeEnumDefination.checkStatus)
								}
						).needLogin(),
				}
				),
		};
	}


	public static void main(String[] args) throws Exception {
		generateModule("creditloan", "consume", consumeControllers);
	}

	private static final String SESSION_PARAMETER_CLASS = "com.djd.fundamental.api.params.SessionParameter";
	private static final String WEB_PAGE_PARAMETER_CLASS = "com.djd.fundamental.api.params.WebPageParameter";
	private static final String WEB_PAGE_SESSION_PARAMETER_CLASS = "com.djd.fundamental.api.params.WebPageSessionParameter";
	private static final String DEFAULT_PARAMETER_SUPER_CLASS = "com.djd.fundamental.api.data.DataObjectBase";
	private static final String DEFAULT_RETURN_TYPE_WRAP_PACKAGE_NAME = "com.djd.fundamental.api.data";

	private static void generateModule(String module, String controllerPath, Controller[] controllers) throws Exception {
		String controllerPackageName = "com.djd.platform.web.controller."+controllerPath;

		String parameterPackageName = "com.djd.business."+module+".params";
		String itemPackageName = "com.djd.business."+module+".result";
		String servicePackageName = "com.djd.business."+module+".service.api";
		String serviceImplPackageName = "com.djd.business."+module+".service.impl";
		String apiRoot = "djd-business\\djd-business-"+module+"\\djd-business-"+module+"-api\\src\\main\\java";
		String implRoot =  "djd-business\\djd-business-"+module+"\\djd-business-"+module+"-impl\\src\\main\\java";

		for (Controller controller : controllers) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("controllerPackageName", controllerPackageName);
			map.put("parameterPackageName", parameterPackageName);
			map.put("itemPackageName", itemPackageName);
			map.put("servicePackageName", servicePackageName);
			map.put("controller", controller);

//			loadContentByTemplate(map, "controller.ftl",
//					"F:\\dzr\\git-djd-v2.0\\djd\\djd-front\\djd-platform-web\\src\\main\\java\\"+CodeUtils.getPathFromPackageName(controllerPackageName)+"\\"+controller.name+".java");
			loadContentByTemplate(map, "controller.ftl",
					"F:\\dzr\\git-djd-v2.0\\djd\\djd-front\\djd-platform-web\\src\\main\\java\\"+CodeUtils.getPathFromPackageName(controllerPackageName)+"\\"+controller.name+".java");


			List<Method> serviceMethodList = new ArrayList<Method>();

			for (ControllerMethod controllerMethod : controller.methods) {
				Class parameterClass = new Class(parameterPackageName + "." + controllerMethod.parameterType);
				parameterClass.setFields(controllerMethod.parameterFields);
				parameterClass.setParentClassName(controllerMethod.parameterSuperClass);

				MultiCodeOutputer.newInstance(apiRoot, parameterPackageName).output(parameterClass);
				//枚举
				MultiCodeOutputer.newInstance(apiRoot, itemPackageName).outputEnum(parameterClass);

				Class itemClass = new Class(itemPackageName+"."+ controllerMethod.itemType);
				itemClass.setFields(controllerMethod.resultFields);
				itemClass.setParentClassName(DEFAULT_PARAMETER_SUPER_CLASS);

				MultiCodeOutputer.newInstance(apiRoot, itemPackageName).output(itemClass);
				//枚举
				MultiCodeOutputer.newInstance(apiRoot, itemPackageName).outputEnum(itemClass);

				Method method = new Method(controllerMethod.name, controllerMethod.getFullReturnType(itemPackageName),
						new Comment[]{new Comment(controllerMethod.getComment())},
						new Param[]{
								new Param(parameterClass.fullClassName, "param"),
						});
				serviceMethodList.add(method);
			}

			//生成服务接口
			Interface serviceInterface = new Interface(servicePackageName+"."+controller.serviceName);
			serviceInterface.setComment(new Comment(controller.comment));
			serviceInterface.setMethods(serviceMethodList.toArray(new Method[serviceMethodList.size()]));

			MultiCodeOutputer.newInstance(apiRoot, itemPackageName).output(serviceInterface);

			Class serviceImplClass = new Class(serviceImplPackageName+"."+controller.serviceName+"Impl");
			serviceImplClass.setComment(new Comment(controller.comment));
			serviceImplClass.setInterface(serviceInterface);

			MultiCodeOutputer.newInstance(implRoot, itemPackageName).output(serviceImplClass);



		}
	}


	private  static void loadContentByTemplate(Map root,String TemplateName,String targetFile){
		try {
			File file = new File(targetFile);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			Configuration cfg = getConfiguration();
			//加载freemarker模板文件
			//定义并设置数据
			Template template = cfg.getTemplate(TemplateName);
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
			template.process(root,out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Configuration cfg = null;
	private static Configuration getConfiguration() throws IOException {
		String TEMPLATEPath = "";
		if (null == cfg) {
			TEMPLATEPath = ClassDefination.class.getResource("/").getPath();
			System.out.println(TEMPLATEPath);
			cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATEPath));
			// setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码
			cfg.setEncoding(Locale.getDefault(), "UTF-8");
			// 设置对象的包装器
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			// 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		}
		return cfg;
	}


	public static Class getParameterClass(String methodName) {
		String className = CodeUtils.getParameterName(methodName)+"Parameter";
		for (Class cls : classes) {
			if (cls.getClassName().equals(className)) {
				return cls;
			}
		}
		return null;
	}

	public static class Controller {
		public String url;
		public String name;
		public String comment;
		public String serviceName;
		public ControllerMethod[] methods;

		private String serviceParameterName;

		public Controller(String name, String url, String serviceName, String comment, ControllerMethod[] methods) {
			this.name = name;
			this.url = url;
			this.comment = comment;
			this.serviceName = serviceName;
			this.serviceParameterName = CodeUtils.getParameterName(serviceName);
			this.methods = methods;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public String getServiceParameterName() {
			return serviceParameterName;
		}

		public void setServiceParameterName(String serviceParameterName) {
			this.serviceParameterName = serviceParameterName;
		}

		public ControllerMethod[] getMethods() {
			return methods;
		}

		public void setMethods(ControllerMethod[] methods) {
			this.methods = methods;
		}
	}

	public static class ControllerMethod {
		public String comment;
		public String returnType;
		public String itemType;
		public String url;
		public String name;
		public String parameterType;
		public Field[] parameterFields;
		public Field[] resultFields;
		public String returnTypeWrap;
		public String parameterSuperClass = DEFAULT_PARAMETER_SUPER_CLASS;

		public ControllerMethod(String name, String comment, String returnTypeWrap, Field[] parameterFields, Field[] resultFields) {
			this.name = name;
			this.url = "/"+CodeUtils.getConstantName(name).toLowerCase();
			this.comment = comment;
			this.itemType = CodeUtils.getClassName(name)+"View";
			this.returnTypeWrap = returnTypeWrap;
			this.returnType = processReturnType(returnTypeWrap, itemType);
			this.parameterType = CodeUtils.getClassName(name)+"Param";
			this.parameterFields = parameterFields;
			this.resultFields = resultFields;
		}

		private String getFullReturnType(String itemPackageName) {
			String type = returnType;
			if (returnType.startsWith("ObjectResultEx")
					|| returnType.startsWith("ListResultEx")) {
				type = DEFAULT_RETURN_TYPE_WRAP_PACKAGE_NAME+".";
				if (itemPackageName != null) {
					type = type + returnType.replace("<"+itemType+">", "<"+itemPackageName+"."+itemType+">");
					System.out.println(type);
				}
				else {
					type = type + returnType;
				}
			}
			else if ("ResultEx".equalsIgnoreCase(returnType)) {
				type = DEFAULT_RETURN_TYPE_WRAP_PACKAGE_NAME+"." + returnType;
			}
			return type;
		}

		private String processReturnType(String returnType, String itemType) {
			if ("ObjectResultEx".equalsIgnoreCase(returnType)
					||"ListResultEx".equalsIgnoreCase(returnType) ) {
				return returnType+"<"+itemType+">";
			}
			return returnType;
		}

		//参数基类
		public ControllerMethod needLogin() {
			this.parameterSuperClass = SESSION_PARAMETER_CLASS;
			return this;
		}
		public ControllerMethod pageNeedLogin() {
			this.parameterSuperClass = WEB_PAGE_SESSION_PARAMETER_CLASS;
			return this;
		}
		public ControllerMethod page() {
			this.parameterSuperClass = WEB_PAGE_PARAMETER_CLASS;
			return this;
		}

		public Field[] getResultFields() {
			return resultFields;
		}

		public void setResultFields(Field[] resultFields) {
			this.resultFields = resultFields;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public String getReturnType() {
			return returnType;
		}

		public void setReturnType(String returnType) {
			this.returnType = returnType;
		}

		public String getItemType() {
			return itemType;
		}

		public void setItemType(String itemType) {
			this.itemType = itemType;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParameterType() {
			return parameterType;
		}

		public void setParameterType(String parameterType) {
			this.parameterType = parameterType;
		}

		public Field[] getParameterFields() {
			return parameterFields;
		}

		public void setParameterFields(Field[] parameterFields) {
			this.parameterFields = parameterFields;
		}
	}
}
