package com.hkfs.fundamental.codegenerator.basis.data;

import com.hkfs.fundamental.codegenerator.basis.data.base.AbsClass;

/**
 * Created by pc on 2016/4/13.
 */
public class Controller extends AbsClass {
    public String url;

    public Controller(String fullClassName) {
        this(fullClassName, (Comment[]) null, (Method[]) null);
    }
    public Controller(String fullClassName, Comment[] comments) {
        this(fullClassName, comments, (Method[])null);
    }
    public Controller(String fullClassName, String[] comments) {
        this(fullClassName, new Comment[]{new Comment(comments)});
    }
    public Controller(String fullClassName, Comment comment) {
        this(fullClassName, new Comment[]{comment}, (Method[])null);
    }
    public Controller(String fullClassName, Comment comment, Method[] methods) {
        this(fullClassName, new Comment[]{comment}, methods);
    }
    public Controller(String fullClassName, String comment) {
        this(fullClassName, new Comment[]{new Comment(comment)});
    }
    public Controller(String fullClassName, String comment, String url, Method[] methods) {
        this(fullClassName, new Comment[]{new Comment(comment)});
        this.url = url;
        this.methods = methods;
    }
    public Controller(String fullClassName, String comment, Method[] methods) {
        this(fullClassName, new Comment[]{new Comment(comment)}, methods);
    }
    public Controller(String fullClassName, Comment[] comments, Method[] methods) {
        super(fullClassName, comments, methods);
    }

    public Controller setUrl(String url) {
        this.url = url;
        return this;
    }

}
