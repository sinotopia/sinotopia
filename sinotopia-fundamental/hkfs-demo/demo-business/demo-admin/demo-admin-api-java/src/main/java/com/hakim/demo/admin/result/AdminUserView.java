package com.hakim.demo.admin.result;

import com.sinotopia.fundamental.api.data.DataObjectBase;

import java.util.Date;
import java.util.List;

/**
 * 管理员基本信息表
 */
public class AdminUserView extends DataObjectBase {
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一性编号
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
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
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 上次登录时间
	 */
	private Date lastLoginTime;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	/**
	 * 角色列表
	 */
	private List<AdminRoleView> roles;
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return this.phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getCreatedTime() {
		return this.createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return this.updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	public List<AdminRoleView> getRoles() {
		return roles;
	}
	public void setRoles(List<AdminRoleView> roles) {
		this.roles = roles;
	}
}
