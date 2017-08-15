package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.hibernate.validator.constraints.NotBlank;
/**
 * 系统角色表
 */
public class AddRoleParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String name;
	/**
	 * 角色代码
	 */
	@NotBlank(message = "角色代码不能为空")
	private String code;
	/**
	 * 角色描述
	 */
	@NotBlank(message = "角色描述不能为空")
	private String description;
	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return this.code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
