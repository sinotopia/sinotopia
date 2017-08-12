package com.hakim.demo.admin.dao;

import com.hakim.demo.admin.bean.AdminUserRole;
import com.hakim.demo.admin.result.AdminUserRoleView;
import com.hkfs.fundamental.database.PageDaoBase;
import com.hkfs.fundamental.database.page.PageList;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRoleDao extends PageDaoBase<AdminUserRole, Long> {
    /**
     * 获取用户角色列表
     * @param adminUserRole
     * @return
     */
    public PageList<AdminUserRoleView> queryUserRoleList(AdminUserRole adminUserRole);
}
