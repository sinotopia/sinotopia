package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsMenuMapper;
import com.sinotopia.cms.dao.model.CmsMenu;
import com.sinotopia.cms.dao.model.CmsMenuExample;
import com.sinotopia.cms.rpc.api.CmsMenuService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* CmsMenuService实现
* Created by shuzheng on 2017/4/5.
*/
@Service
@Transactional
@BaseService
public class CmsMenuServiceImpl extends BaseServiceImpl<CmsMenuMapper, CmsMenu, CmsMenuExample> implements CmsMenuService {

    private static Logger _log = LoggerFactory.getLogger(CmsMenuServiceImpl.class);

    @Autowired
    CmsMenuMapper cmsMenuMapper;

}