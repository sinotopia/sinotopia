package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayPayMapper;
import com.sinotopia.pay.dao.model.PayPay;
import com.sinotopia.pay.dao.model.PayPayExample;

/**
* 降级实现PayPayService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayPayServiceMock extends BaseServiceMock<PayPayMapper, PayPay, PayPayExample> implements PayPayService {

}
