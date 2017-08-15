package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;

import javax.validation.constraints.NotNull;
/**
 * 管理员菜单表
 */
public class DeleteMenuParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一性编号
	 */
	@NotNull(message = "菜单id不能为空")
	private Long id;

	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
