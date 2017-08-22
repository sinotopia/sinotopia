package com.sinotopia.demonstration.admin.dao;

import com.sinotopia.demonstration.admin.bean.AdminRoleMenu;
import com.sinotopia.demonstration.admin.result.AdminRoleMenuView;
import com.sinotopia.fundamental.database.PageDaoBase;
import com.sinotopia.fundamental.database.page.PageList;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleMenuDao extends PageDaoBase<AdminRoleMenu, Long> {
    /**
     * 获取角色菜单列表
     * @param adminRoleMenu
     * @return
     */
    public PageList<AdminRoleMenuView> queryRoleMenuList(AdminRoleMenu adminRoleMenu);
}
