package com.hakim.demo.admin.result;

import com.sinotopia.fundamental.api.data.DataObjectBase;
import java.util.Date;
/**
 * 系统用户角色关系表
 */
public class AdminUserRoleView extends DataObjectBase {
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一性编号
	 */
	private Long id;
	/**
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 手机号码
	 */
	private String userPhone;
	/**
	 * 电子邮箱
	 */
	private String userEmail;



	/**
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 角色名称
	 */
	private String roleName;
	/**
	 * 角色代码
	 */
	private String roleCode;

	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
