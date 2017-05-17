package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.upms.dao.mapper.UpmsLogMapper;
import com.sinotopia.upms.dao.model.UpmsLog;
import com.sinotopia.upms.dao.model.UpmsLogExample;

/**
* 降级实现UpmsLogService接口
* Created by shuzheng on 2017/3/20.
*/
public class UpmsLogServiceMock extends BaseServiceMock<UpmsLogMapper, UpmsLog, UpmsLogExample> implements UpmsLogService {

}
