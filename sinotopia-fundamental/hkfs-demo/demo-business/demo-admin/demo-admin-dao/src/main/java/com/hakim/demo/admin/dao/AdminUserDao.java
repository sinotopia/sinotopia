package com.hakim.demo.admin.dao;

import com.hakim.demo.admin.bean.AdminUser;
import com.hkfs.fundamental.database.PageDaoBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserDao extends PageDaoBase<AdminUser, Long> {
}
