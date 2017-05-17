package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsSystemMapper;
import com.sinotopia.cms.dao.model.CmsSystem;
import com.sinotopia.cms.dao.model.CmsSystemExample;
import com.sinotopia.cms.rpc.api.CmsSystemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* CmsSystemService实现
* Created by shuzheng on 2017/4/5.
*/
@Service
@Transactional
@BaseService
public class CmsSystemServiceImpl extends BaseServiceImpl<CmsSystemMapper, CmsSystem, CmsSystemExample> implements CmsSystemService {

    private static Logger _log = LoggerFactory.getLogger(CmsSystemServiceImpl.class);

    @Autowired
    CmsSystemMapper cmsSystemMapper;

}