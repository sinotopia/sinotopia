package com.sinotopia.fundamental.common.test;

import com.sinotopia.fundamental.common.hanger.Chain;
import com.sinotopia.fundamental.common.hanger.Executor;

/**
 */
public class IgnoreChainTest {

    public static void main(String[] args) {
        Chain chain = new Chain();
        chain.addExecutor("hello", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("hello executed...");
                return true;
            }
        });

        chain.addExecutor("world", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("i'm the world...");
                chain.ignore("nobody");
                return true;
            }
        });

        chain.addExecutor("nobody", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("i'm nobody...");
                return true;
            }
        });

        chain.addExecutor("last", new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("finished...");
                return true;
            }
        });

        chain.execute();
    }
}
