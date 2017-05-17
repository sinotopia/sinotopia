package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.upms.dao.mapper.UpmsUserOrganizationMapper;
import com.sinotopia.upms.dao.model.UpmsUserOrganization;
import com.sinotopia.upms.dao.model.UpmsUserOrganizationExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* 降级实现UpmsUserOrganizationService接口
* Created by shuzheng on 2017/3/20.
*/
public class UpmsUserOrganizationServiceMock extends BaseServiceMock<UpmsUserOrganizationMapper, UpmsUserOrganization, UpmsUserOrganizationExample> implements UpmsUserOrganizationService {

    private static Logger _log = LoggerFactory.getLogger(UpmsUserOrganizationServiceMock.class);

    @Override
    public int organization(String[] organizationIds, int id) {
        _log.info("UpmsUserOrganizationServiceMock => organization");
        return 0;
    }

}
