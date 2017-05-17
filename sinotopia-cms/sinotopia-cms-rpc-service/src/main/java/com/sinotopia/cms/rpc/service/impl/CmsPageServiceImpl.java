package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsPageMapper;
import com.sinotopia.cms.dao.model.CmsPage;
import com.sinotopia.cms.dao.model.CmsPageExample;
import com.sinotopia.cms.rpc.api.CmsPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* CmsPageService实现
* Created by shuzheng on 2017/4/5.
*/
@Service
@Transactional
@BaseService
public class CmsPageServiceImpl extends BaseServiceImpl<CmsPageMapper, CmsPage, CmsPageExample> implements CmsPageService {

    private static Logger _log = LoggerFactory.getLogger(CmsPageServiceImpl.class);

    @Autowired
    CmsPageMapper cmsPageMapper;

}