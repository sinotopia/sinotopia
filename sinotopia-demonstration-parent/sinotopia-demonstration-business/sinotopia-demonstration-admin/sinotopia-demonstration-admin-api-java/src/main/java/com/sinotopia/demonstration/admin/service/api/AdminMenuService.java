package com.sinotopia.demonstration.admin.service.api;

import com.sinotopia.demonstration.admin.result.AdminMenuView;
import com.sinotopia.demonstration.admin.result.AdminRoleMenuView;
import com.sinotopia.demonstration.admin.param.*;
import com.sinotopia.fundamental.api.data.ListResultEx;
import com.sinotopia.fundamental.api.data.ObjectResultEx;
import com.sinotopia.fundamental.api.data.ResultEx;

/**
 * 菜单服务
 */
public interface AdminMenuService {

    /**
     * 新增菜单
     * @param parameter
     * @return
     */
     ResultEx addMenu(AddMenuParameter parameter);

    /**
     * 删除菜单
     * @param parameter
     * @return
     */
     ResultEx deleteMenu(DeleteMenuParameter parameter);

    /**
     * 获取菜单
     * @param parameter
     * @return
     */
     ObjectResultEx<AdminMenuView> getMenu(GetMenuParameter parameter);

    /**
     * 更新菜单
     * @param parameter
     * @return
     */
     ResultEx updateMenu(UpdateMenuParameter parameter);

    /**
     * 获取菜单列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminMenuView> getMenuList(GetMenuListParameter parameter);


    /**
     * 新增角色对应菜单
     * @param parameter
     * @return
     */
     ResultEx addRoleMenu(AddRoleMenuParameter parameter);

    /**
     * 删除角色对应菜单
     * @param parameter
     * @return
     */
     ResultEx deleteRoleMenu(DeleteRoleMenuParameter parameter);

    /**
     * 获取角色对应菜单
     * @param parameter
     * @return
     */
     ObjectResultEx<AdminRoleMenuView> getRoleMenu(GetRoleMenuParameter parameter);

    /**
     * 更新角色对应菜单
     * @param parameter
     * @return
     */
     ResultEx updateRoleMenu(UpdateRoleMenuParameter parameter);

    /**
     * 获取角色对应菜单列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminRoleMenuView> getRoleMenuList(GetRoleMenuListParameter parameter);

    /**
     * 获取会话菜单列表
     * @param parameter
     * @return
     */
     ListResultEx<AdminMenuView> getSessionMenuList(GetSessionMenuListParameter parameter);
}
