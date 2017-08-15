package com.hakim.demo.admin.result;

import com.sinotopia.fundamental.api.data.DataObjectBase;

import java.util.Date;

/**
 * 登录用户结果
 * Created by brucezee on 2017/3/1.
 */
public class LoginUserView extends DataObjectBase {
    /**
     * 登录token
     */
    private String accessToken;
    /**
     * 唯一性编号
     */
    private Long id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 管理员类型
     */
    private Integer type;
    /**
     * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
     */
    private Integer status;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
    /**
     * 角色列表
     */
    private String[] roles;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
