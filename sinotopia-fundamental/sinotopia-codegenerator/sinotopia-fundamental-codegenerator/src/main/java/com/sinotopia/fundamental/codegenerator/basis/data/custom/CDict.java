package com.sinotopia.fundamental.codegenerator.basis.data.custom;

import com.sinotopia.fundamental.codegenerator.basis.data.Dict;
import com.sinotopia.fundamental.codegenerator.basis.render.NameRender;
import com.sinotopia.fundamental.codegenerator.basis.validate.Unique;
import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;

public class CDict extends Dict {
	public CDict(int value, String name, String comment) {
		super(name, new Object[]{value, comment});
		
		this.value = value;
		this.comment = comment;
	}
	
	public int value;
	public String comment;
	
	public CDict setNameRender(NameRender nameRender) {
		this.nameRender = nameRender;
		return this;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		sb.append(processComment());
		sb.append(AbsFormat.tab());
		sb.append(nameRender!=null?nameRender.render(name):name);
		sb.append("(");
		sb.append(processValue()).append(", \"").append(comment).append("\"");
		sb.append(")");
		//这里结尾不跟换行，因为后面可能是逗号或分号
		return render(sb.toString());
	}
	
	public static void main(String[] args) {
		CDict c = new CDict(1, "Normal", "正常");
		
		System.out.println(c);
	}

	protected String processValue() {
		return value+"";
	}
	
	@Override
	public Object getUniqueKey() {
		return value+ Unique.DIVIDER+name;
	}
}
