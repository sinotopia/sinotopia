package com.sinotopia.demo.rpc.service.impl;

import com.sinotopia.demo.rpc.api.DemoService;

/**
 * 实现DemoService接口
 * Created by shuzheng on 2017/4/1.
 */
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

}