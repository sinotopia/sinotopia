package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;
import java.util.Date;
/**
 * 系统用户角色关系表
 */
public class GetUserRoleParameter extends SessionParameter {
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
	 * 角色id
	 */
	private Long roleId;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 更新时间
	 */
	private Date updatedTime;
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getUserId() {
		return this.userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return this.roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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
}
