package com.sinotopia.sample.rpc.api;

/**
 * 降级实现SampleService接口
 * Created by sinotopia on 2017/4/1.
 */
public class SampleServiceMock implements SampleService {

    @Override
    public String sayHello(String name) {
        return null;
    }

}
