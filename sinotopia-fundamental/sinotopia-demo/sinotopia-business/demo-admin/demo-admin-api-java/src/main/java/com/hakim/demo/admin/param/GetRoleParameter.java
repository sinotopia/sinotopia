package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;

import javax.validation.constraints.NotNull;

/**
 * 系统角色表
 */
public class GetRoleParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一性编号
	 */
	@NotNull(message = "角色id不能为空")
	private Long id;
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
