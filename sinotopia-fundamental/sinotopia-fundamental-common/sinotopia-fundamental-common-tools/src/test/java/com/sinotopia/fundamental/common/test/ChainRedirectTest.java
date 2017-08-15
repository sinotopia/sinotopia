package com.sinotopia.fundamental.common.test;

import com.sinotopia.fundamental.common.hanger.Chain;
import com.sinotopia.fundamental.common.hanger.Executor;
import com.sinotopia.fundamental.common.hanger.exception.ExceptionHandler;

/**
 * Created by zhoubing on 2016/12/7.
 */
public class ChainRedirectTest {
    public static void main(String[] args) {
        Chain chain = new Chain();
        //设置异常处理类
        chain.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handle(String name, Exception e) {
                System.out.println("Executor: " + name + " error!");
                e.printStackTrace();
            }
        });
        //添加执行器step1
        chain.addExecutor("step1", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("step 1");
                return true;
            }
        });
        //添加执行器step2
        chain.addExecutor("step2", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("step 2");
                chain.redirect("method2");//跳过method1
//                chain.redirect("chain2");//跳过method1 method2 继续chain2执行
                return true;
            }
        });
        chain.addExecutor("method1", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("method 1");
                return true;
            }
        });
        chain.addExecutor("method2", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("method 2");
                return true;
            }
        });

        //另外一段执行链
        Chain requestChain = new Chain();
        requestChain.addExecutor("chain2 method1", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("chain2 method 1");
                return true;
            }
        });
        requestChain.addExecutor("chain2 method2", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("chain2 method 2");
                return true;
            }
        });
        chain.addExecutor("chain2", requestChain);

        chain.execute();
    }
}
