package com.sinotopia.demo.rpc.service.impl;

import com.sinotopia.sample.rpc.api.SampleService;

/**
 * 实现DemoService接口
 * Created by sinotopia on 2017/4/1.
 */
public class SampleServiceImpl implements SampleService {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

}