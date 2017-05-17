package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayTypeMapper;
import com.sinotopia.pay.dao.model.PayType;
import com.sinotopia.pay.dao.model.PayTypeExample;

/**
* 降级实现PayTypeService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayTypeServiceMock extends BaseServiceMock<PayTypeMapper, PayType, PayTypeExample> implements PayTypeService {

}
