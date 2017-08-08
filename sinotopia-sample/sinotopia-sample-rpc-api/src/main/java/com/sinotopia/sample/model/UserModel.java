package com.sinotopia.sample.model;

/**
 * 测试接口
 * Created by sinotopia on 2017/4/1.
 */
public class UserModel {

    private int userId;

    private  String userName;

    private int age;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}