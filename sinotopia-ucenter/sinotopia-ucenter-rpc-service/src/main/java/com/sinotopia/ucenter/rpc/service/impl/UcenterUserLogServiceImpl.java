package com.sinotopia.ucenter.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.ucenter.dao.mapper.UcenterUserLogMapper;
import com.sinotopia.ucenter.dao.model.UcenterUserLog;
import com.sinotopia.ucenter.dao.model.UcenterUserLogExample;
import com.sinotopia.ucenter.rpc.api.UcenterUserLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UcenterUserLogService实现
 * Created by shuzheng on 2017/4/27.
 */
@Service
@Transactional
@BaseService
public class UcenterUserLogServiceImpl extends BaseServiceImpl<UcenterUserLogMapper, UcenterUserLog, UcenterUserLogExample> implements UcenterUserLogService {

    private static Logger _log = LoggerFactory.getLogger(UcenterUserLogServiceImpl.class);

    @Autowired
    UcenterUserLogMapper ucenterUserLogMapper;

}