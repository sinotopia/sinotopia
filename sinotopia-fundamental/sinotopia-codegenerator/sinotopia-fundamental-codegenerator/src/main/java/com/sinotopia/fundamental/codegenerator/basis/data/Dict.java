package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.hkfs.fundamental.codegenerator.basis.render.NameRender;
import com.hkfs.fundamental.codegenerator.basis.render.TabRender;
import com.hkfs.fundamental.codegenerator.basis.validate.Unique;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

/**
 * 枚举项
 * @author zhoub 2015年10月28日 下午11:58:12
 */
public class Dict extends AbsFormat implements Unique {
	public String name;//枚举项的名称
	public Object[] paramValues;//枚举项的参数值
	public NameRender nameRender;//名称的处理器
	
	public Dict(String name) {
		this(name, null);
	}
	public Dict(String name, Object[] paramValues) {
		this.name = name;
		this.paramValues = paramValues;
	}
	
	public Dict setNameRender(NameRender nameRender) {
		this.nameRender = nameRender;
		return this;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processComment());
		sb.append(tab());
		sb.append(nameRender!=null?nameRender.render(name):name);
		if (paramValues != null && paramValues.length > 0) {
			sb.append("(");
			sb.append(processParams());
			sb.append(")");
		}
		//这里结尾不跟换行，因为后面可能是逗号或分号
		return render(sb.toString());
	}
	
	protected String processComment() {
		String params = processParams();
		if (StrUtils.notEmpty(params)) {
			Comment comment = new Comment(processParams().replace("\"", ""));
			comment.setTabRender(new TabRender(1));;
			return comment.toString();
		}
		return "";
	}
	protected String processParams() {
		StringBuilder sb = new StringBuilder();
		if (paramValues != null && paramValues.length > 0) {
			for (int i=0;i<paramValues.length-1;i++) {
				sb.append(processParamValue(paramValues[i])).append(", ");
			}
			sb.append(processParamValue(paramValues[paramValues.length-1]));
		}
		return sb.toString();
	}
	
	protected String processParamValue(Object paramValue) {
		StringBuilder sb = new StringBuilder();
		if (paramValue instanceof String) {
			sb.append("\"").append(paramValue).append("\"");
		}
		else {
			sb.append(paramValue);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Dict d = new Dict("Normal", new Object[]{1, "正常"});
		System.out.println(d);
		
		d = new Dict("Expired", new Object[]{2, "已过期"});
		System.out.println(d);
		d = new Dict("New");
		System.out.println(d);
	}
	@Override
	public Object getUniqueKey() {
		return name;
	}
}
