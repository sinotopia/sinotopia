package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsTagMapper;
import com.sinotopia.cms.dao.model.CmsTag;
import com.sinotopia.cms.dao.model.CmsTagExample;
import com.sinotopia.cms.rpc.api.CmsTagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* CmsTagService实现
* Created by shuzheng on 2017/4/5.
*/
@Service
@Transactional
@BaseService
public class CmsTagServiceImpl extends BaseServiceImpl<CmsTagMapper, CmsTag, CmsTagExample> implements CmsTagService {

    private static Logger _log = LoggerFactory.getLogger(CmsTagServiceImpl.class);

    @Autowired
    CmsTagMapper cmsTagMapper;

}