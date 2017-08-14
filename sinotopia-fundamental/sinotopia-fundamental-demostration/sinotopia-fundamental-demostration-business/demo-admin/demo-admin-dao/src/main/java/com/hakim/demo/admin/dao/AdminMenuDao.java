package com.hakim.demo.admin.dao;

import com.hakim.demo.admin.bean.AdminMenu;
import com.hkfs.fundamental.database.PageDaoBase;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMenuDao extends PageDaoBase<AdminMenu, Long> {
}
