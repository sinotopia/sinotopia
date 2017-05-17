package com.sinotopia.upms.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.upms.dao.mapper.UpmsSystemMapper;
import com.sinotopia.upms.dao.model.UpmsSystem;
import com.sinotopia.upms.dao.model.UpmsSystemExample;

/**
* 降级实现UpmsSystemService接口
* Created by shuzheng on 2017/3/20.
*/
public class UpmsSystemServiceMock extends BaseServiceMock<UpmsSystemMapper, UpmsSystem, UpmsSystemExample> implements UpmsSystemService {

}
