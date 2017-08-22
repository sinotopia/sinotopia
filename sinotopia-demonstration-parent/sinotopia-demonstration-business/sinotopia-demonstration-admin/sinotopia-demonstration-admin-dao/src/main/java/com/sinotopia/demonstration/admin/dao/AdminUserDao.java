package com.sinotopia.demonstration.admin.dao;

import com.sinotopia.demonstration.admin.bean.AdminUser;
import com.sinotopia.fundamental.database.PageDaoBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserDao extends PageDaoBase<AdminUser, Long> {
}
