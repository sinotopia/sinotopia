package com.hkfs.fundamental.api.params;

import com.hkfs.fundamental.api.data.DataObjectBase;

/**
 * 用户登录信息
 * Created by zhoubing on 2016/4/12.
 */
public class SessionIdentity extends DataObjectBase {
    private static final long serialVersionUID = 1L;
    //用户id
    private Long userId;
    //用户手机号码
    private String phone;
    //用户姓名
    private String name;
    //用户统一标识
    private String uuid;
    //用户身份证号码
    private String idCard;
    private String userType;
    private String[] roles;
    private String terminalType;

    public SessionIdentity() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String toString() {
        return "SessionIdentity [userId=" + this.userId + ", phone=" + this.phone + ", name=" + this.name + " userType=" + this.userType + ", terminalType=" + this.terminalType + ", roles=" + roles +", uuid=" + this.uuid + ", idCard=" +this.idCard+ "]";
    }
}

