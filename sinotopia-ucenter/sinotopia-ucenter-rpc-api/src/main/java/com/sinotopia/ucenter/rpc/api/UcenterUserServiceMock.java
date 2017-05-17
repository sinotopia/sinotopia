package com.sinotopia.ucenter.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.ucenter.dao.mapper.UcenterUserMapper;
import com.sinotopia.ucenter.dao.model.UcenterUser;
import com.sinotopia.ucenter.dao.model.UcenterUserExample;

/**
* 降级实现UcenterUserService接口
* Created by shuzheng on 2017/4/27.
*/
public class UcenterUserServiceMock extends BaseServiceMock<UcenterUserMapper, UcenterUser, UcenterUserExample> implements UcenterUserService {

}
