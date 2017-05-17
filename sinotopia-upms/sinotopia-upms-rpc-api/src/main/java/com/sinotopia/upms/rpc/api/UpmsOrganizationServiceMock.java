package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.upms.dao.mapper.UpmsOrganizationMapper;
import com.sinotopia.upms.dao.model.UpmsOrganization;
import com.sinotopia.upms.dao.model.UpmsOrganizationExample;

/**
* 降级实现UpmsOrganizationService接口
* Created by shuzheng on 2017/3/20.
*/
public class UpmsOrganizationServiceMock extends BaseServiceMock<UpmsOrganizationMapper, UpmsOrganization, UpmsOrganizationExample> implements UpmsOrganizationService {

}
