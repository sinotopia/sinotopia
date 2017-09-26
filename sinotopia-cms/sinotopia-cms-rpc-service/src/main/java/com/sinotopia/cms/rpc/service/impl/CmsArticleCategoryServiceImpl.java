package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsArticleCategoryMapper;
import com.sinotopia.cms.dao.model.CmsArticleCategory;
import com.sinotopia.cms.dao.model.CmsArticleCategoryExample;
import com.sinotopia.cms.rpc.api.CmsArticleCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CmsArticleCategoryService实现
 * Created by sinotopia on 2017/4/5.
 */
@Service
@Transactional
@BaseService
public class CmsArticleCategoryServiceImpl extends BaseServiceImpl<CmsArticleCategoryMapper, CmsArticleCategory, CmsArticleCategoryExample> implements CmsArticleCategoryService {

    private static Logger _log = LoggerFactory.getLogger(CmsArticleCategoryServiceImpl.class);

    @Autowired
    private CmsArticleCategoryMapper cmsArticleCategoryMapper;

}