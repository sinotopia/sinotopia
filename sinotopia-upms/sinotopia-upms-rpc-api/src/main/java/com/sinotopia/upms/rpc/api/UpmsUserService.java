package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseService;
import com.sinotopia.upms.dao.model.UpmsUser;
import com.sinotopia.upms.dao.model.UpmsUserExample;

/**
* UpmsUserService接口
* Created by shuzheng on 2017/3/20.
*/
public interface UpmsUserService extends BaseService<UpmsUser, UpmsUserExample> {

    UpmsUser createUser(UpmsUser upmsUser);

}