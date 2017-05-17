package com.sinotopia.cms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.cms.dao.mapper.CmsMenuMapper;
import com.sinotopia.cms.dao.model.CmsMenu;
import com.sinotopia.cms.dao.model.CmsMenuExample;

/**
* 降级实现CmsMenuService接口
* Created by shuzheng on 2017/4/5.
*/
public class CmsMenuServiceMock extends BaseServiceMock<CmsMenuMapper, CmsMenu, CmsMenuExample> implements CmsMenuService {

}
