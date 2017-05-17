package com.sinotopia.pay.rpc.api;

import com.sinotopia.common.base.BaseServiceMock;
import com.sinotopia.pay.dao.mapper.PayVendorMapper;
import com.sinotopia.pay.dao.model.PayVendor;
import com.sinotopia.pay.dao.model.PayVendorExample;

/**
* 降级实现PayVendorService接口
* Created by shuzheng on 2017/3/29.
*/
public class PayVendorServiceMock extends BaseServiceMock<PayVendorMapper, PayVendor, PayVendorExample> implements PayVendorService {

}
