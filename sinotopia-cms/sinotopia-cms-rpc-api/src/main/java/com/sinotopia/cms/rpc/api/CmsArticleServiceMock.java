package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsArticleMapper;
import com.sinotopia.cms.dao.model.CmsArticle;
import com.sinotopia.cms.dao.model.CmsArticleExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
* 降级实现CmsArticleService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsArticleServiceMock extends BaseServiceMock<CmsArticleMapper, CmsArticle, CmsArticleExample> implements CmsArticleService {

    private static Logger _log = LoggerFactory.getLogger(CmsArticleServiceMock.class);

    @Override
    public List<CmsArticle> selectCmsArticlesByCategoryId(Integer categoryId, Integer offset, Integer limit) {
        _log.info("CmsArticleServiceMock => getCmsArticlesByCategoryId");
        return null;
    }

    @Override
    public long countByCategoryId(Integer categoryId) {
        _log.info("CmsArticleServiceMock => countByCategoryId");
        return 0;
    }

    @Override
    public List<CmsArticle> selectCmsArticlesByTagId(Integer tagId, Integer offset, Integer limit) {
        _log.info("CmsArticleServiceMock => getCmsArticlesByCategoryId");
        return null;
    }

    @Override
    public long countByTagId(Integer tagId) {
        _log.info("CmsArticleServiceMock => countByTagId");
        return 0;
    }

}
