package com.sinotopia.fundamental.codegenerator.basis.data;

import com.sinotopia.fundamental.codegenerator.basis.data.base.AbsFormat;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;

import java.util.ArrayList;

/**
 * 注释
 * @author zhoub 2015年10月28日 下午11:22:44
 */
public class Comment extends AbsFormat {
	public String[] comments;//注释项
	
	public Comment(String comment) {
		this(new String[]{comment});
	}
	public Comment(String[] comments) {
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		init();
		StringBuilder sb = new StringBuilder();
		if (comments != null && comments.length > 0) {
			sb.append(processHeader());
			for (String comment : comments) {
				sb.append(processItem(comment));
			}
			sb.append(processFooter());
		}
		return render(sb.toString());
	}
	
	protected String processHeader() {
		return "/**"+line();
	}

	protected String processItem(String item) {
		return " * "+item+line();
	}
	
	protected String processFooter() {
		return " */"+line();
	}
	
	public Comment setItems(String[] items) {
		this.comments = items;
		return this;
	}
	public Comment addItem(String item) {
		this.setItems(CodeUtils.addNew(new ArrayList<String>(), comments != null ? comments : new String[0], item));
		return this;
	}
	public Comment addItems(String[] items) {
		for (String item : items) {
			this.addItem(item);
		}
		return this;
	}
	
	
	
	public static void main(String[] args) {
		Comment c = new Comment(new String[]{"function", "注册"});
		System.out.println(c);
	}
}
