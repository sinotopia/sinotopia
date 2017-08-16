package com.sinotopia.fundamental.codegenerator.basis.data.custom;

import com.sinotopia.fundamental.codegenerator.basis.render.NameRender;


public class StrDict extends CDict {
	public StrDict(int value, String name, String comment) {
		super(value, name, comment);
	}
	
	@Override
	protected String processValue() {
		return "\""+value+"\"";
	}
	
	public StrDict setNameRender(NameRender nameRender) {
		this.nameRender = nameRender;
		return this;
	}
	
	public static void main(String[] args) {
		StrDict c = new StrDict(1, "Normal", "正常");
		
		System.out.println(c);
	}
	
	public static StrDict clone(CDict dict) {
		return new StrDict(dict.value, dict.name, dict.comment).setNameRender(dict.nameRender);
	}
}
