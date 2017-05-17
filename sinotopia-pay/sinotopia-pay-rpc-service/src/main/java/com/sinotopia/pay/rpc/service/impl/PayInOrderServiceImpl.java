package com.sinotopia.pay.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.pay.dao.mapper.PayInOrderMapper;
import com.sinotopia.pay.dao.model.PayInOrder;
import com.sinotopia.pay.dao.model.PayInOrderExample;
import com.sinotopia.pay.rpc.api.PayInOrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* PayInOrderService实现
* Created by shuzheng on 2017/3/29.
*/
@Service
@Transactional
@BaseService
public class PayInOrderServiceImpl extends BaseServiceImpl<PayInOrderMapper, PayInOrder, PayInOrderExample> implements PayInOrderService {

    private static Logger _log = LoggerFactory.getLogger(PayInOrderServiceImpl.class);

    @Autowired
    PayInOrderMapper payInOrderMapper;

}