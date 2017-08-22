package com.sinotopia.demonstration.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 管理员菜单表
 */
public class AddMenuParameter extends SessionParameter {

	private static final long serialVersionUID = 1L;
	/**
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 菜单名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	private String name;
	/**
	 * 菜单URL
	 */
	@NotBlank(message = "菜单URL不能为空")
	private String url;
	/**
	 * 上级菜单id
	 */
	@NotNull(message = "上级菜单id不能为空")
	private Long parentId;
	/**
	 * 菜单排序号
	 */
	@NotNull(message = "菜单排序号不能为空")
	private Integer sequence;

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
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getParentId() {
		return this.parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getSequence() {
		return this.sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
