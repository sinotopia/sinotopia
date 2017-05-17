package com.sinotopia.ucenter.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.ucenter.dao.mapper.UcenterUserLogMapper;
import com.sinotopia.ucenter.dao.model.UcenterUserLog;
import com.sinotopia.ucenter.dao.model.UcenterUserLogExample;

/**
* 降级实现UcenterUserLogService接口
* Created by shuzheng on 2017/4/27.
*/
public class UcenterUserLogServiceMock extends BaseServiceMock<UcenterUserLogMapper, UcenterUserLog, UcenterUserLogExample> implements UcenterUserLogService {

}
