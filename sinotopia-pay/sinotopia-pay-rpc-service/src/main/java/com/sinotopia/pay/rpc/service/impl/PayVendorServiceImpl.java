package com.sinotopia.pay.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.pay.dao.mapper.PayVendorMapper;
import com.sinotopia.pay.dao.model.PayVendor;
import com.sinotopia.pay.dao.model.PayVendorExample;
import com.sinotopia.pay.rpc.api.PayVendorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PayVendorService实现
 * Created by shuzheng on 2017/3/29.
 */
@Service
@Transactional
@BaseService
public class PayVendorServiceImpl extends BaseServiceImpl<PayVendorMapper, PayVendor, PayVendorExample> implements PayVendorService {

    private static Logger _log = LoggerFactory.getLogger(PayVendorServiceImpl.class);

    @Autowired
    PayVendorMapper payVendorMapper;

}