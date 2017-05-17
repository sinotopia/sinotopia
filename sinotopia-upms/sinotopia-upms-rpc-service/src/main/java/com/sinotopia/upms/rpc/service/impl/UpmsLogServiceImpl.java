package com.sinotopia.upms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.upms.dao.mapper.UpmsLogMapper;
import com.sinotopia.upms.dao.model.UpmsLog;
import com.sinotopia.upms.dao.model.UpmsLogExample;
import com.sinotopia.upms.rpc.api.UpmsLogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* UpmsLogService实现
* Created by shuzheng on 2017/3/20.
*/
@Service
@Transactional
@BaseService
public class UpmsLogServiceImpl extends BaseServiceImpl<UpmsLogMapper, UpmsLog, UpmsLogExample> implements UpmsLogService {

    private static Logger _log = LoggerFactory.getLogger(UpmsLogServiceImpl.class);

    @Autowired
    UpmsLogMapper upmsLogMapper;

}