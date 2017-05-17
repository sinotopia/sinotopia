package com.sinotopia.upms.rpc.service.impl;

import com.sinotopia.common.annotation.BaseService;
import com.sinotopia.common.base.BaseServiceImpl;
import com.sinotopia.upms.dao.mapper.UpmsSystemMapper;
import com.sinotopia.upms.dao.model.UpmsSystem;
import com.sinotopia.upms.dao.model.UpmsSystemExample;
import com.sinotopia.upms.rpc.api.UpmsSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* UpmsSystemService实现
* Created by shuzheng on 2017/3/20.
*/
@Service
@Transactional
@BaseService
public class UpmsSystemServiceImpl extends BaseServiceImpl<UpmsSystemMapper, UpmsSystem, UpmsSystemExample> implements UpmsSystemService {

    private static Logger _log = LoggerFactory.getLogger(UpmsSystemServiceImpl.class);

    @Autowired
    UpmsSystemMapper upmsSystemMapper;

}