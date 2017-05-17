package com.sinotopia.pay.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.pay.dao.mapper.PayOutOrderDetailMapper;
import com.sinotopia.pay.dao.model.PayOutOrderDetail;
import com.sinotopia.pay.dao.model.PayOutOrderDetailExample;
import com.sinotopia.pay.rpc.api.PayOutOrderDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PayOutOrderDetailService实现
 * Created by shuzheng on 2017/3/29.
 */
@Service
@Transactional
@BaseService
public class PayOutOrderDetailServiceImpl extends BaseServiceImpl<PayOutOrderDetailMapper, PayOutOrderDetail, PayOutOrderDetailExample> implements PayOutOrderDetailService {

    private static Logger _log = LoggerFactory.getLogger(PayOutOrderDetailServiceImpl.class);

    @Autowired
    PayOutOrderDetailMapper payOutOrderDetailMapper;

}