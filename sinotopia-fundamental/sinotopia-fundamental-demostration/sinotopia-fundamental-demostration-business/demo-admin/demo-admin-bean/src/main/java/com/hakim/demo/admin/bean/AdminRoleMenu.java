package com.hakim.demo.admin.bean;

import com.sinotopia.fundamental.api.data.PojoDataObjectBase;
import java.util.Date;
/**
 * 系统角色对应菜单表
 */
public class AdminRoleMenu extends PojoDataObjectBase {
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
     * 角色id
     */
    private Long roleId;
    /**
     * 菜单id
     */
    private Long menuId;
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
