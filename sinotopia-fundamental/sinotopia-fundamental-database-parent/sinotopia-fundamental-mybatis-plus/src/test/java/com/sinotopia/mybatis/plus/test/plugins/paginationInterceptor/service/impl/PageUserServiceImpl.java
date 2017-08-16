package com.sinotopia.mybatis.plus.test.plugins.paginationInterceptor.service.impl;

import com.sinotopia.mybatis.plus.service.impl.ServiceImpl;
import com.sinotopia.mybatis.plus.test.plugins.paginationInterceptor.service.PageUserService;
import org.springframework.stereotype.Service;

import com.sinotopia.mybatis.plus.test.plugins.paginationInterceptor.entity.PageUser;
import com.sinotopia.mybatis.plus.test.plugins.paginationInterceptor.mapper.PageUserMapper;

@Service
public class PageUserServiceImpl extends ServiceImpl<PageUserMapper, PageUser> implements PageUserService {

}
