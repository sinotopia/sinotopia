package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayInOrderDetailMapper;
import com.sinotopia.pay.dao.model.PayInOrderDetail;
import com.sinotopia.pay.dao.model.PayInOrderDetailExample;

/**
* 降级实现PayInOrderDetailService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayInOrderDetailServiceMock extends BaseServiceMock<PayInOrderDetailMapper, PayInOrderDetail, PayInOrderDetailExample> implements PayInOrderDetailService {

}
