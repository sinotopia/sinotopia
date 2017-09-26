package com.sinotopia.cms.rpc.service.impl;

import com.sinotopia.cms.rpc.mapper.CmsArticleExtMapper;
import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.cms.dao.mapper.CmsArticleMapper;
import com.sinotopia.cms.dao.model.CmsArticle;
import com.sinotopia.cms.dao.model.CmsArticleExample;
import com.sinotopia.cms.rpc.api.CmsArticleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CmsArticleService实现
 * Created by sinotopia on 2017/4/5.
 */
@Service
@Transactional
@BaseService
public class CmsArticleServiceImpl extends BaseServiceImpl<CmsArticleMapper, CmsArticle, CmsArticleExample> implements CmsArticleService {

    private static Logger _log = LoggerFactory.getLogger(CmsArticleServiceImpl.class);

    @Autowired
    CmsArticleMapper cmsArticleMapper;

    @Autowired
    CmsArticleExtMapper cmsArticleExtMapper;

    @Override
    public List<CmsArticle> selectCmsArticlesByCategoryId(Integer categoryId, Integer offset, Integer limit) {
        return cmsArticleExtMapper.selectCmsArticlesByCategoryId(categoryId, offset, limit);
    }

    @Override
    public long countByCategoryId(Integer categoryId) {
        return cmsArticleExtMapper.countByCategoryId(categoryId);
    }

    @Override
    public List<CmsArticle> selectCmsArticlesByTagId(Integer tagId, Integer offset, Integer limit) {
        return cmsArticleExtMapper.selectCmsArticlesByTagId(tagId, offset, limit);
    }

    @Override
    public long countByTagId(Integer tagId) {
        return cmsArticleExtMapper.countByTagId(tagId);
    }

}