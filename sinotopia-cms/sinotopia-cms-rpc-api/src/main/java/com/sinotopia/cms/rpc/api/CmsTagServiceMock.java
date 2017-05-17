package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsTagMapper;
import com.sinotopia.cms.dao.model.CmsTag;
import com.sinotopia.cms.dao.model.CmsTagExample;

/**
* 降级实现CmsTagService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsTagServiceMock extends BaseServiceMock<CmsTagMapper, CmsTag, CmsTagExample> implements CmsTagService {

}
