package com.sinotopia.fundamental;

import com.sinotopia.fundamental.lock.RedisLock1;
import com.sinotopia.fundamental.lock.redis.LockCallback;
import com.sinotopia.fundamental.lock.redis.LockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.ShardedJedisPool;

import java.io.IOException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

/**
 * Created by pc on 2016/4/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:applicationContext-*.xml"})
public class LockTest {

//    @Autowired
//    private ZooKeeperClient client;

    @Autowired
    private ShardedJedisPool shardedJedisPool;

//    @postconstruct
//    public void init(){
//        jedispoolconfig config = new jedispoolconfig();//jedis池配置
//        config.setmaxtotal(500);//最大活动的对象个数
//        config.setmaxidle(1000 * 60);//对象最大空闲时间
//        config.setmaxwaitmillis(1000 * 10);//获取对象时最大等待时间
//        config.settestonborrow(true);
//        config.settestonreturn(true);
//        config.settestwhileidle(true);
//        string host1 = "127.0.0.1";
//        int port1 = 6379;
//        list<jedisshardinfo> jdsinfolist = new arraylist<jedisshardinfo>(1);
//        jedisshardinfo info1 = new jedisshardinfo(host1, port1, 15000, 15000, 1);
//        jdsinfolist.add(info1);
//        shardedjedispool = new shardedjedispool(config, jdsinfolist);
//    }

    @Test
    public void testLockService() {
        System.out.println(shardedJedisPool);
    }

    @Test
    public void testLock(){
//        ZookeeperLock lock = new ZookeeperLock("/dzr/lock",client);
//        RedisLock lock = new RedisLock("dzr:lock");
        LockServiceImpl lockService = new LockServiceImpl();
        lockService.setShardedJedisPool(shardedJedisPool);
        RedisLock1 lock = new RedisLock1("dzr:lock",lockService);
//        try{
//            lock.lock();
//            System.out.println("获取到锁");
//        }catch (Exception e){
//
//        }finally {
//            lock.unlock();
//            System.out.println("释放锁");
//        }
        CyclicBarrier barrier = new CyclicBarrier(5);
        for(int i = 0 ;i < 5 ;i++){
            Mytask task = new Mytask(i,lock ,barrier);
            new Thread(task).start();
        }

        lockService.lock("afasdf", 1000L, 10000L, new LockCallback() {
            @Override
            public void onSuccess(String key, Object lockId) {
                System.out.println("success---------------- "+key+" "+lockId);
            }
        });

        try {
           System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public class Mytask implements Runnable{

        private int no;

        private CyclicBarrier barrier;

        private Lock lock;

        public Mytask(int no,Lock lock,CyclicBarrier barrier) {
            this.no = no;
            this.barrier = barrier;
            this.lock =  lock;
        }

        @Override
        public void run() {
            try {
                barrier.await();
                lock.lock();
                Thread.sleep(10000);
                System.out.println("no :" + this.no +" exe finished!");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
    }
}
