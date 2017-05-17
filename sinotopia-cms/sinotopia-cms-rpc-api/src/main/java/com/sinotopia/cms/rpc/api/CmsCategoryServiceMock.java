package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsCategoryMapper;
import com.sinotopia.cms.dao.model.CmsCategory;
import com.sinotopia.cms.dao.model.CmsCategoryExample;

/**
* 降级实现CmsCategoryService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsCategoryServiceMock extends BaseServiceMock<CmsCategoryMapper, CmsCategory, CmsCategoryExample> implements CmsCategoryService {

}
