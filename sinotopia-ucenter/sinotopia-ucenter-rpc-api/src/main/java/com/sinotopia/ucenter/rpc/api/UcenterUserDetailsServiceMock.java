package com.sinotopia.ucenter.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.ucenter.dao.mapper.UcenterUserDetailsMapper;
import com.sinotopia.ucenter.dao.model.UcenterUserDetails;
import com.sinotopia.ucenter.dao.model.UcenterUserDetailsExample;

/**
* 降级实现UcenterUserDetailsService接口
* Created by shuzheng on 2017/4/27.
*/
public class UcenterUserDetailsServiceMock extends BaseServiceMock<UcenterUserDetailsMapper, UcenterUserDetails, UcenterUserDetailsExample> implements UcenterUserDetailsService {

}
