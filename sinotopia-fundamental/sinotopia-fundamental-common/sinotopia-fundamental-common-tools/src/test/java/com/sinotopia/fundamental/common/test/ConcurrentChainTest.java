package com.sinotopia.fundamental.common.test;

import com.sinotopia.fundamental.common.hanger.Chain;
import com.sinotopia.fundamental.common.hanger.ConcurrentChain;
import com.sinotopia.fundamental.common.hanger.Executor;
import com.sinotopia.fundamental.common.hanger.exception.ExceptionHandler;
import com.sinotopia.fundamental.common.hanger.loop.NumberLoop;
import com.sinotopia.fundamental.common.utils.NumberUtils;
import com.sinotopia.fundamental.common.utils.ThreadUtils;

import java.util.concurrent.Executors;

/**
 * Created by zhoubing on 2016/12/7.
 */
public class ConcurrentChainTest {
    public static void main(String[] args) {
        ConcurrentChain chain = new ConcurrentChain();
        chain.setService(Executors.newCachedThreadPool());
        chain.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handle(String name, Exception e) {
                e.printStackTrace();
            }
        });


        chain.addExecutor("name1", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("name1 begin");
                ThreadUtils.sleep(3000);
                System.out.println("name1 stopped");
                return true;
            }
        });

        chain.addExecutor("executor2", new NumberLoop() {
            @Override
            protected boolean doLoop(Chain chain, int i) {
                System.out.println("executor2 "+i+" start...");
                ThreadUtils.sleep(NumberUtils.random(300, 3000));
                System.out.println("executor2 "+i+" finished...");
                if (i == 3) {
                    throw new IllegalArgumentException();
                }
                return true;
            }

            @Override
            protected int getLimit(Chain chain) {
                return 5;
            }
        });

        chain.addExecutor("looper3", new NumberLoop() {
            @Override
            protected boolean doLoop(Chain chain, int i) {
                System.out.println("looper3 "+i+" start");
                ThreadUtils.sleep(NumberUtils.random(300, 3000));
                System.out.println("looper3 "+i+" finished");
                return true;
            }

            @Override
            protected int getLimit(Chain chain) {
                return 5;
            }
        });

        chain.execute();

        chain.shutdownNow();

        System.out.println(chain.isSuccess());
    }
}
