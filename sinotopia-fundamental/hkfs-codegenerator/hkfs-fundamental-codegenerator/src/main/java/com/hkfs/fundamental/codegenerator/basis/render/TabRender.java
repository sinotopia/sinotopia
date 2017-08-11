package com.hkfs.fundamental.codegenerator.basis.render;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsFormat;


public class TabRender {
	private int tabNumber;
	public TabRender(int tabNumber) {
		this.tabNumber = tabNumber;
	}
	public static TabRender newInstance(int tabNumber) {
		return new TabRender(tabNumber);
	}
	public static TabRender newInstance() {
		return new TabRender(1);
	}
	
	public String process(String content) {
		if (tabNumber <= 0) {
			return content;
		}
		
		String[] contents = content.split(AbsFormat.line());
		StringBuilder sb = new StringBuilder();
		for (String c : contents) {
			sb.append(AbsFormat.tab(tabNumber)).append(c).append(AbsFormat.line());
		}
		return sb.toString();
	}
	
	public void setTabRender(AbsFormat[] formats) {
		if (formats != null && formats.length > 0) {
			for (AbsFormat format : formats) {
				format.setTabRender(this);
			}
		}
	}
	public void setTabRender(AbsFormat format) {
		format.setTabRender(this);
	}
}
