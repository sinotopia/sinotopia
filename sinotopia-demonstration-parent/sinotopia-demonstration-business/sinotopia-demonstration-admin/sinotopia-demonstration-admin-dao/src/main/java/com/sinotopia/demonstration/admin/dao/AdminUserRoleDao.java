package com.sinotopia.demonstration.admin.dao;

import com.sinotopia.demonstration.admin.domain.AdminUserRole;
import com.sinotopia.demonstration.admin.result.AdminUserRoleView;
import com.sinotopia.fundamental.database.PageDaoBase;
import com.sinotopia.fundamental.database.page.PageList;
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
