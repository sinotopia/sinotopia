package com.hkfs.fundamental.zookeeper;

import com.alibaba.fastjson.JSON;
import com.hkfs.fundamental.zookeeper.bean.ResultContainer;
import com.hkfs.fundamental.zookeeper.seckill.SecKillHandler;
import com.hkfs.fundamental.zookeeper.seckill.SecKillService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by pc on 2016/4/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext-zookeeper.xml"})
public class ZookeeperTest {

    @Autowired
    private ZooKeeperClient zooKeeperClient;

    private SecKillService secKillService;

    private String secKillPath = "redpacket";

    @PostConstruct
    public void init(){
        Assert.assertNotNull(zooKeeperClient);
        secKillService = new SecKillService();
        secKillService.setZkClient(zooKeeperClient);

        //初始化活动数据
        SeckKillActivity activity = new SeckKillActivity(secKillPath,10,10);
        String jsonString = JSON.toJSONString(activity);
        secKillService.initSecKillData(secKillPath,jsonString);
        System.out.println(jsonString);
    }
//    @Test
    public void testLockAndWork(){
        try {
            boolean success = secKillService.secKill(secKillPath, new SecKillHandler<Boolean>() {
                @Override
                public String process(String lockedPath, String data, ResultContainer<Boolean> resultContainer, ZooKeeperClient client) {
                    SeckKillActivity activity = JSON.parseObject(data,SeckKillActivity.class);
                    if(activity.getRemainNum() > 0){
                        activity.setRemainNum(activity.getRemainNum() - 1);
                        System.out.println(Thread.currentThread().getName() + "成功秒杀一个，还剩余" + activity.getRemainNum() + "个");
                        resultContainer.setResult(Boolean.TRUE);
                    }else{
                        System.out.println(Thread.currentThread().getName() + "秒杀失败，还剩余" + activity.getRemainNum() + "个");
                        resultContainer.setResult(Boolean.FALSE);
                    }
                    return JSON.toJSONString(activity);
                }
            });
        }catch (Exception e){
            System.out.println("秒杀出现异常！");
        }

    }

    @Test
    public void testSecKill(){
        CyclicBarrier barrier = new CyclicBarrier(20);
        for(int i = 0 ;i < 20 ;i++){
            Mytask task = new Mytask(i ,barrier);
            new Thread(task).start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
}


public class Mytask implements Runnable{

    private int no;

    private CyclicBarrier barrier;


    public Mytask(int no,CyclicBarrier barrier) {
        this.no = no;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        try {
            barrier.await();
            boolean success = secKillService.secKill(secKillPath, new SecKillHandler<Boolean>() {
                @Override
                public String process(String lockedPath, String data, ResultContainer<Boolean> resultContainer, ZooKeeperClient client) {
                    SeckKillActivity activity = JSON.parseObject(data,SeckKillActivity.class);
                    if(activity.getRemainNum() > 0){
                        activity.setRemainNum(activity.getRemainNum() - 1);
                        System.out.println(Thread.currentThread().getName() + Mytask.this.no + ":成功秒杀一个，还剩余" + activity.getRemainNum() + "个");
                        resultContainer.setResult(Boolean.TRUE);
                    }else{
                        System.out.println(Thread.currentThread().getName() +  Mytask.this.no + "秒杀失败，还剩余" + activity.getRemainNum() + "个");
                        resultContainer.setResult(Boolean.FALSE);
                    }
                    return JSON.toJSONString(activity);
                }
            });
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName()+this.no + ":秒杀出现异常！");
        }
    }
}
    @PreDestroy
    public void destroy(){
        secKillService.clearSecKillData(secKillPath);
    }

    public static class SeckKillActivity{

        public SeckKillActivity(){}

        private String no;

        private Integer totalNum;

        private Integer remainNum;

        public SeckKillActivity(String no, Integer totalNum, Integer remainNum) {
            this.no = no;
            this.totalNum = totalNum;
            this.remainNum = remainNum;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public Integer getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(Integer totalNum) {
            this.totalNum = totalNum;
        }

        public Integer getRemainNum() {
            return remainNum;
        }

        public void setRemainNum(Integer remainNum) {
            this.remainNum = remainNum;
        }
    }
}
