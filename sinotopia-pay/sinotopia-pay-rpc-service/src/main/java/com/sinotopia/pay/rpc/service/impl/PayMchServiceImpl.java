package com.sinotopia.pay.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.pay.dao.mapper.PayMchMapper;
import com.sinotopia.pay.dao.model.PayMch;
import com.sinotopia.pay.dao.model.PayMchExample;
import com.sinotopia.pay.rpc.api.PayMchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* PayMchService实现
* Created by shuzheng on 2017/3/29.
*/
@Service
@Transactional
@BaseService
public class PayMchServiceImpl extends BaseServiceImpl<PayMchMapper, PayMch, PayMchExample> implements PayMchService {

    private static Logger _log = LoggerFactory.getLogger(PayMchServiceImpl.class);

    @Autowired
    PayMchMapper payMchMapper;

}