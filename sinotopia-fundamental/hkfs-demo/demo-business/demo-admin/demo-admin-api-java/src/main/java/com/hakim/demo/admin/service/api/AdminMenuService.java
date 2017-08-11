package com.hakim.demo.admin.service.api;

import com.hakim.demo.admin.result.AdminMenuView;
import com.hakim.demo.admin.result.AdminRoleMenuView;
import com.hakim.demo.admin.param.*;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 菜单服务
 * Created by brucezee on 2017/3/1.
 */
public interface AdminMenuService {
    /**
     * 新增菜单
     * @param parameter
     * @return
     */
    public ResultEx addMenu(AddMenuParameter parameter);

    /**
     * 删除菜单
     * @param parameter
     * @return
     */
    public ResultEx deleteMenu(DeleteMenuParameter parameter);

    /**
     * 获取菜单
     * @param parameter
     * @return
     */
    public ObjectResultEx<AdminMenuView> getMenu(GetMenuParameter parameter);

    /**
     * 更新菜单
     * @param parameter
     * @return
     */
    public ResultEx updateMenu(UpdateMenuParameter parameter);

    /**
     * 获取菜单列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminMenuView> getMenuList(GetMenuListParameter parameter);


    /**
     * 新增角色对应菜单
     * @param parameter
     * @return
     */
    public ResultEx addRoleMenu(AddRoleMenuParameter parameter);

    /**
     * 删除角色对应菜单
     * @param parameter
     * @return
     */
    public ResultEx deleteRoleMenu(DeleteRoleMenuParameter parameter);

    /**
     * 获取角色对应菜单
     * @param parameter
     * @return
     */
    public ObjectResultEx<AdminRoleMenuView> getRoleMenu(GetRoleMenuParameter parameter);

    /**
     * 更新角色对应菜单
     * @param parameter
     * @return
     */
    public ResultEx updateRoleMenu(UpdateRoleMenuParameter parameter);

    /**
     * 获取角色对应菜单列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminRoleMenuView> getRoleMenuList(GetRoleMenuListParameter parameter);

    /**
     * 获取会话菜单列表
     * @param parameter
     * @return
     */
    public ListResultEx<AdminMenuView> getSessionMenuList(GetSessionMenuListParameter parameter);
}
