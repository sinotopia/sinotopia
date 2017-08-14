package com.hkfs.fundamental.codegenerator.basis.render;

import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

/**
 * 名称的处理器
 * @author zhoub 2015年11月18日 下午9:54:41
 */
public class NameRender {
	private int renderType = RENDER_TYPE_UPPERCASE_CONSTANT;
	
	public NameRender(int renderType) {
		this.renderType = renderType;
	}
	/**
	 * 处理名称
	 * @param name 名称
	 * @return 处理后的名称
	 */
	public String render(String name) {
		if (renderType == RENDER_TYPE_UPPERCASE_CONSTANT) {
			return getUppercaeConstant(name);
		}
		if (renderType == RENDER_TYPE_CAMEL_UPPERCASE) {
			return getCamelUppercase(name);
		}
		if (renderType == RENDER_TYPE_CAMEL_LOWERCASE) {
			return getCamelLowercase(name);
		}
		return null;
	}
	
	/***
	 * 大写常量，中间间隔下划线，如：USER_TYPE_ADMIN
	 */
	public static NameRender Constant = new NameRender(NameRender.RENDER_TYPE_UPPERCASE_CONSTANT);
	/**
	 * 驼峰方式，首字母大写
	 */
	public static NameRender Uppercase = new NameRender(NameRender.RENDER_TYPE_CAMEL_UPPERCASE);
	/**
	 * 驼峰方式，首字母小写
	 */
	public static NameRender Lowercase = new NameRender(NameRender.RENDER_TYPE_CAMEL_LOWERCASE);
	
	/***
	 * 大写常量，中间间隔下划线，如：USER_TYPE_ADMIN
	 */
	public static final int RENDER_TYPE_UPPERCASE_CONSTANT = 1;
	/**
	 * 驼峰方式，首字母大写
	 */
	public static final int RENDER_TYPE_CAMEL_UPPERCASE = 2;
	/**
	 * 驼峰方式，首字母小写
	 */
	public static final int RENDER_TYPE_CAMEL_LOWERCASE = 3;
	
	public String getCamelUppercase(String name) {
		return CodeUtils.getClassName(name);
	}
	public String getCamelLowercase(String name) {
		return CodeUtils.getParameterName(name);
	}
	public String getUppercaeConstant(String name) {
		return CodeUtils.getConstantName(name);
	}
}
