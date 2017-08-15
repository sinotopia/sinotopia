package com.hakim.demo.admin.dao;

import com.hakim.demo.admin.bean.AdminRole;
import com.hkfs.fundamental.database.PageDaoBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleDao extends PageDaoBase<AdminRole, Long> {
}
