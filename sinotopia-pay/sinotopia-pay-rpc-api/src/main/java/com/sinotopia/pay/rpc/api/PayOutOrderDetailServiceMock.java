package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayOutOrderDetailMapper;
import com.sinotopia.pay.dao.model.PayOutOrderDetail;
import com.sinotopia.pay.dao.model.PayOutOrderDetailExample;

/**
* 降级实现PayOutOrderDetailService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayOutOrderDetailServiceMock extends BaseServiceMock<PayOutOrderDetailMapper, PayOutOrderDetail, PayOutOrderDetailExample> implements PayOutOrderDetailService {

}
