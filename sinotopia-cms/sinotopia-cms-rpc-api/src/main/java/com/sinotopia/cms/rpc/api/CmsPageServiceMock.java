package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsPageMapper;
import com.sinotopia.cms.dao.model.CmsPage;
import com.sinotopia.cms.dao.model.CmsPageExample;

/**
* 降级实现CmsPageService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsPageServiceMock extends BaseServiceMock<CmsPageMapper, CmsPage, CmsPageExample> implements CmsPageService {

}
