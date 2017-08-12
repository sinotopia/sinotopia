package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.SessionParameter;

import javax.validation.constraints.NotNull;
/**
 * 管理员菜单表
 */
public class UpdateMenuParameter extends SessionParameter {
	private static final long serialVersionUID = 1L;
	/**
	 * 唯一性编号
	 */
	@NotNull(message = "菜单id不能为空")
	private Long id;
	/**
	 * 通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;
	 */
	private Integer status;
	/**
	 * 菜单名称
	 */
	private String name;
	/**
	 * 菜单URL
	 */
	private String url;
	/**
	 * 上级菜单Id
	 */
	private Long parentId;
	/**
	 * 菜单排序号
	 */
	private Integer sequence;
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
