package com.hakim.demo.admin.param;

import com.sinotopia.fundamental.api.params.WebPageSessionParameter;
/**
 * 管理员菜单表
 */
public class GetMenuListParameter extends WebPageSessionParameter {
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
	 * 菜单名称
	 */
	private String name;
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
