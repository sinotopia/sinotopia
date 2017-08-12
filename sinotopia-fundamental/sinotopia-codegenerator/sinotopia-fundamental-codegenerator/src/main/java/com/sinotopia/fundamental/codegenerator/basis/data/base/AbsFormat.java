package com.hkfs.fundamental.codegenerator.basis.data.base;

import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.render.Initializer;
import com.hkfs.fundamental.codegenerator.basis.render.TabRender;


public class AbsFormat {
	public TabRender tabRender;
	public Initializer initializer;
	protected String render(String content) {
		return tabRender != null ? tabRender.process(content) : content;
	}
	protected void init() {
		if (initializer != null) {
			initializer.init(this);
		}
	}
	
	
	public void setTabRender(TabRender tabRender) {
		this.tabRender = tabRender;
	}
	public void setInitializer(Initializer initializer) {
		this.initializer = initializer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String line() {
		return line(1);
	}
	public static String tab() {
		return tab(1);
	}
	public static String tabLine() {
		return tabLine(1, 1);
	}
	public static String tabLine(int tabNumber, int lineNumber) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<lineNumber;i++) {
			sb.append(tab(tabNumber)).append(line());
		}
		return sb.toString();
	}
	public static String lineTab() {
		return lineTab(1, 1);
	}
	public static String lineTab(int lineNumber, int tabNumber) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<lineNumber;i++) {
			sb.append(line()).append(tab(tabNumber));
		}
		sb.append(line());
		return sb.toString();
	}
	
	
	/**
	 * 返回指定数量的换行
	 */
	public static String line(int lineNumber) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<lineNumber;i++) {
			sb.append(Consts.LINE);
		}
		return sb.toString();
	}
	
	/**
	 * 返回指定数量的tab
	 */
	public static String tab(int tabNumber) {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<tabNumber;i++) {
			sb.append(Consts.TAB);
		}
		return sb.toString();
	}
}
