package com.hakim.demo.admin.dao;

import com.hakim.demo.admin.bean.AdminRoleMenu;
import com.hakim.demo.admin.result.AdminRoleMenuView;
import com.hkfs.fundamental.database.PageDaoBase;
import com.hkfs.fundamental.database.page.PageList;
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
