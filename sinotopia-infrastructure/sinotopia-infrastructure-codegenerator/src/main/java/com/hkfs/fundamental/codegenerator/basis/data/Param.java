package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.utils.AppendUtils;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;

/**
 * 参数
 * @author zhoub 2015年10月28日 下午11:44:29
 */
public class Param extends AbsFormat {
	public String fullClassName;//参数的完整类名或类型名
	public String name;//参数的名称
	
	public Param(String fullClassName, String name) {
		this.fullClassName = CodeUtils.formatFullClassName(fullClassName);
		this.name = name;
	}
	
	@Override
	public String toString() {
		init();
		return render(CodeUtils.getClassNameFromFullClassName(fullClassName)+" "+name);
	}
	
	public String processSetterValue() {
		return AppendUtils.processSetterValue(name);
	}
	
	public static void main(String[] args) {
		Param p = new Param("int", "id");
		System.out.println(p);
	}
}
