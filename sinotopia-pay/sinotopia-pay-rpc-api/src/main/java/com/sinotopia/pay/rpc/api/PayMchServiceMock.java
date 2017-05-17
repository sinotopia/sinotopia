package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayMchMapper;
import com.sinotopia.pay.dao.model.PayMch;
import com.sinotopia.pay.dao.model.PayMchExample;

/**
* 降级实现PayMchService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayMchServiceMock extends BaseServiceMock<PayMchMapper, PayMch, PayMchExample> implements PayMchService {

}
