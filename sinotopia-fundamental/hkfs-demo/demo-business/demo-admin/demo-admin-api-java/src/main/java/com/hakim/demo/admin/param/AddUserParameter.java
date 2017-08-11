package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 管理员基本信息表
 */
public class AddUserParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空")
	private String username;
	/**
	 * 登录密码
	 */
	@NotBlank(message = "用户名不能为空")
	private String password;
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
	public String getUsername() {
		return this.username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return this.password;
	}
	public void setPassword(String password) {
		this.password = password;
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
}
