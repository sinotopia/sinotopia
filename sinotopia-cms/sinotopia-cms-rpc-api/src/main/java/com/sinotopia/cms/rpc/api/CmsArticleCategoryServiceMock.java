package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsArticleCategoryMapper;
import com.sinotopia.cms.dao.model.CmsArticleCategory;
import com.sinotopia.cms.dao.model.CmsArticleCategoryExample;

/**
* 降级实现CmsArticleCategoryService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsArticleCategoryServiceMock extends BaseServiceMock<CmsArticleCategoryMapper, CmsArticleCategory, CmsArticleCategoryExample> implements CmsArticleCategoryService {

}
