package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

/**
 * 参数
 * @author zhoub 2015年10月28日 下午11:44:29
 */
public class Param extends AbsFormat {
	public Annotation[] annotations;//方法的注解
	public String fullClassName;//参数的完整类名或类型名
	public String name;//参数的名称
	
	public Param(String fullClassName, String name) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		this.name = name;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processAnnotations());
		sb.append(processDefine());
		return render(sb.toString());
	}

	protected String processAnnotations() {
		return annotations != null && annotations.length > 0 ? StrUtils.trim(AppendUtils.processAnnotations(annotations))+" " : "";
	}

	protected String processDefine() {
		return CodeUtils.getClassNameFromFullClassName(fullClassName)+" "+name;
	}

	public String processSetterValue() {
		return AppendUtils.processSetterValue(name);
	}
	
	public static void main(String[] args) {
		Param p = new Param("int", "id");
		System.out.println(p);
	}
}
