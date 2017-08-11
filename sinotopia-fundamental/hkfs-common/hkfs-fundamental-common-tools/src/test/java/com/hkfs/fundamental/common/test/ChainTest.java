package com.hkfs.fundamental.common.test;

import com.hkfs.fundamental.common.hanger.Chain;
import com.hkfs.fundamental.common.hanger.Executor;
import com.hkfs.fundamental.common.hanger.exception.ChainBreakException;
import com.hkfs.fundamental.common.hanger.exception.ExceptionHandler;
import com.hkfs.fundamental.common.hanger.loop.*;
import com.hkfs.fundamental.common.utils.HttpUtils;
import com.hkfs.fundamental.common.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubing on 2016/12/7.
 */
public class ChainTest {
    public static void main(String[] args) {
        Chain chain = new Chain();
        //设置异常处理类
        chain.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handle(String name, Exception e) {
                System.out.println("Executor: "+name+" error!");
                e.printStackTrace();
            }
        });

        //存放对象
        chain.put("user", new User());
        //添加执行器，修改user对象内容
        chain.addExecutor(new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("set user gender");
                User user = chain.get("user");
                user.setGender(1);
                return true;
            }
        });
        //添加执行器，修改用户姓名内容
        chain.addExecutor(new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("set user name");
                User user = chain.get("user");
                user.setName("Nick");
                return true;
            }
        });

        //存储变量
        chain.put("schoolName", "Zhejiang University");


        //另外一段执行链
        Chain requestChain = new Chain();
        //请求获取响应内容
        requestChain.addExecutor(new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("request baidu for response content");
                String data = null;
                try {
                    data = HttpUtils.get("http://www.baidu.com");
                }
                catch (Exception e) {
                    throw new ChainBreakException(e);//直接退出执行链
                }
                chain.put("content", data);//存储请求结果
                return true;
            }
        });
        //处理响应
        requestChain.addExecutor(new Executor() {
            @Override
            public boolean execute(Chain chain) {
                System.out.println("process baidu response content");

                //获取上一步的响应内容
                String content = chain.get("content");
                //处理内容
                String title = StrUtils.getMiddleText(content, "<title>", "</title>");

                //从父链中获取变量schoolName
                String schoolName = chain.parentGet("schoolName");

                //新内容
                String newContent = title + " " + schoolName;

                chain.getParent().put("newContent", newContent);

                chain.remove("content");//删除content
                return true;
            }
        });
        chain.addExecutor(requestChain);

        //添加循环
        chain.addExecutor(new Loop(){
            @Override
            public boolean isLoop(Chain chain) {//循环条件
                Integer i = chain.get("i");
                return i == null || i == 0 || i < 3;
            }

            @Override
            public boolean doLoop(Chain chain) {//执行循环内容，改变循环条件
                Integer i = (Integer) chain.get("i");
                if (i == null) {
                    i = 0;
                }
                System.out.println(i);

                chain.put("i", i+1);//更新i
                return true;
            }
        });

        //添加数字类型的循环for(int i = 0; i < 5; i++){}
        chain.addExecutor(new NumberLoop(){
            @Override
            public boolean doLoop(Chain chain, int i) {
                System.out.println("less than number loop "+i);
                return true;
            }
            @Override
            protected int getLimit(Chain chain) {//最大值
                return 5;
            }
            @Override
            protected int getStep(Chain chain) {//步长
                return 1;
            }
            @Override
            protected int getInitialValue(Chain chain) {//初始值
                return 0;
            }
        });

        //添加数字类型的循环for(int i = 5; i >= 0; i--){}
        chain.addExecutor(new NumberLoop(){
            @Override
            public boolean doLoop(Chain chain, int i) {
                System.out.println("greater than or equal to number loop "+i);
                return true;
            }
            @Override
            protected OptType getOptType(Chain chain) {
                return OptType.GREATER_THAN_OR_EQUAL_TO;//大于等于 >=
            }
            @Override
            protected int getLimit(Chain chain) {//最小值
                return 0;
            }
            @Override
            protected int getStep(Chain chain) {//步长
                return -1;
            }
            @Override
            protected int getInitialValue(Chain chain) {//初始值
                return 5;
            }
        });


        //遍历列表 从前往后的顺序
        List<String> list = new ArrayList<String>();
        list.add("1 Apple");
        list.add("2 Banana");
        list.add("3 Cheery");
        chain.put("list", list);
        chain.addExecutor(new ListLoop<String>() {
            @Override
            protected List<String> getList(Chain chain) {
                return chain.get("list");
            }
            @Override
            protected boolean doLoop(Chain chain, String object) {
                System.out.println(object);
                return true;
            }
        });

        //从后往前遍历
        chain.addExecutor(new ListLoop<String>() {
            @Override
            protected List<String> getList(Chain chain) {
                return chain.get("list");
            }
            @Override
            protected SeqType getSeqType(Chain chain) {
                return SeqType.DESC;
            }
            @Override
            protected boolean doLoop(Chain chain, String object) {
                System.out.println(object);
                return true;
            }
        });

        //遍历数组 从前往后遍历
        String[] array = new String[]{
                "7 Beijing",
                "8 Hangzhou",
                "9 Xiamen",
        };
        chain.put("array", array);
        chain.addExecutor(new ArrayLoop<String>(){
            @Override
            protected String[] getArray(Chain chain) {
                return chain.get("array");
            }
            //默认从前往后遍历
            @Override
            protected boolean doLoop(Chain chain, String object) {
                System.out.println(object);
                return true;
            }
        });

        //从后往前遍历
        chain.addExecutor(new ArrayLoop<String>(){
            @Override
            protected String[] getArray(Chain chain) {
                return chain.get("array");
            }
            @Override
            protected SeqType getSeqType(Chain chain) {
                return SeqType.DESC;//后从往前遍历
            }
            @Override
            protected boolean doLoop(Chain chain, String object) {
                System.out.println(object);
                return true;
            }
        });

        chain.execute();

        System.out.println(chain.getParams());
    }

    static class User {
        private String name;
        private Integer gender;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getGender() {
            return gender;
        }

        public void setGender(Integer gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return ("[name="+name+",gender="+gender+"]");
        }
    }
}
