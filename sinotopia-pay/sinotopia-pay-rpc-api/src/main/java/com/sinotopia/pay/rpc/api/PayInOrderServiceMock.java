package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayInOrderMapper;
import com.sinotopia.pay.dao.model.PayInOrder;
import com.sinotopia.pay.dao.model.PayInOrderExample;

/**
* 降级实现PayInOrderService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayInOrderServiceMock extends BaseServiceMock<PayInOrderMapper, PayInOrder, PayInOrderExample> implements PayInOrderService {

}
