package com.sinotopia.fundamental.zookeeper.seckill;

import com.sinotopia.fundamental.zookeeper.ZooKeeperClient;
import com.sinotopia.fundamental.zookeeper.bean.ResultContainer;

/**
 * Created by pc on 2016/4/26.
 * 秒杀处理
 */
public interface SecKillHandler<T> {

    public String process(String lockedPath, String data, ResultContainer<T> resultContainer,
                        ZooKeeperClient client);
}
