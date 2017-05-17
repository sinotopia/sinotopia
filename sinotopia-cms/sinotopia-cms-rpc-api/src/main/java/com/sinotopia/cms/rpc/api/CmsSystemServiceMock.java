package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsSystemMapper;
import com.sinotopia.cms.dao.model.CmsSystem;
import com.sinotopia.cms.dao.model.CmsSystemExample;

/**
* 降级实现CmsSystemService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsSystemServiceMock extends BaseServiceMock<CmsSystemMapper, CmsSystem, CmsSystemExample> implements CmsSystemService {

}
