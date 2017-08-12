package com.hakim.demo.admin.result;

import com.sinotopia.fundamental.api.data.DataObjectBase;
import java.util.Date;
/**
 * 系统角色表
 */
public class AdminRoleView extends DataObjectBase {
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
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色代码
	 */
	private String code;
	/**
	 * 角色描述
	 */
	private String description;
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
