package com.sinotopia.demonstration.admin.dao;

import com.sinotopia.demonstration.admin.bean.AdminRole;
import com.sinotopia.fundamental.database.PageDaoBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleDao extends PageDaoBase<AdminRole, Long> {
}
