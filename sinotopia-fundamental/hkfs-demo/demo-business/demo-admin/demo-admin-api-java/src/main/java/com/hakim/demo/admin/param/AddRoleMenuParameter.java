package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;

import javax.validation.constraints.NotNull;

/**
 * 系统角色对应菜单表
 */
public class AddRoleMenuParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 角色id
	 */
	@NotNull(message = "角色id不能为空")
	private Long roleId;
	/**
	 * 菜单id
	 */
	private Long menuId;
	/**
	 * 多个菜单id使用逗号连接
	 */
	private String menuIds;
	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getRoleId() {
		return this.roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getMenuId() {
		return this.menuId;
	}
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
