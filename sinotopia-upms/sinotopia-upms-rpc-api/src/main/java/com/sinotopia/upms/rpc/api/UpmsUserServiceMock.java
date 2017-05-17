package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.upms.dao.mapper.UpmsUserMapper;
import com.sinotopia.upms.dao.model.UpmsUser;
import com.sinotopia.upms.dao.model.UpmsUserExample;

/**
* 降级实现UpmsUserService接口
* Created by shuzheng on 2017/3/20.
*/
public class UpmsUserServiceMock extends BaseServiceMock<UpmsUserMapper, UpmsUser, UpmsUserExample> implements UpmsUserService {

    @Override
    public UpmsUser createUser(UpmsUser upmsUser) {
        return null;
    }

}
