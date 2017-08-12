package com.hkfs.fundamental.codegenerator.basis.web;

import com.hkfs.fundamental.codegenerator.basis.data.Comment;
import com.hkfs.fundamental.codegenerator.basis.data.Method;
import com.hkfs.fundamental.codegenerator.basis.data.base.AbsClass;

/**
 * SpringMVC的Controller
 * @author brucezee 2015年12月22日 下午10:05:07
 *
 */
public class Controller extends AbsClass {
	public Controller(String fullClassName) {
		super(fullClassName, (Comment[])null, (Method[])null);
	}
	public Controller(String fullClassName, Comment[] comments) {
		super(fullClassName, comments, (Method[])null);
	}
	public Controller(String fullClassName, String[] comments) {
		super(fullClassName, new Comment[]{new Comment(comments)}, null);
	}
	public Controller(String fullClassName, Comment comment) {
		super(fullClassName, new Comment[]{comment}, (Method[])null);
	}
	public Controller(String fullClassName, Comment comment, Method[] methods) {
		super(fullClassName, new Comment[]{comment}, methods);
	}
	public Controller(String fullClassName, String comment) {
		super(fullClassName, new Comment[]{new Comment(comment)}, (Method[])null);
	}
	public Controller(String fullClassName, String comment, Method[] methods) {
		super(fullClassName, new Comment[]{new Comment(comment)}, methods);
	}
}
