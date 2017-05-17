package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayOutOrderMapper;
import com.sinotopia.pay.dao.model.PayOutOrder;
import com.sinotopia.pay.dao.model.PayOutOrderExample;

/**
* 降级实现PayOutOrderService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayOutOrderServiceMock extends BaseServiceMock<PayOutOrderMapper, PayOutOrder, PayOutOrderExample> implements PayOutOrderService {

}
