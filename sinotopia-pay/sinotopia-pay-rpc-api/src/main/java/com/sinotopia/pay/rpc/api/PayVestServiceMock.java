package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayVestMapper;
import com.sinotopia.pay.dao.model.PayVest;
import com.sinotopia.pay.dao.model.PayVestExample;

/**
* 降级实现PayVestService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayVestServiceMock extends BaseServiceMock<PayVestMapper, PayVest, PayVestExample> implements PayVestService {

}
