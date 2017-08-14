package com.hkfs.fundamental.zookeeper.task;

import com.hkfs.fundamental.config.FundamentalConfigProvider;
import com.hkfs.fundamental.zookeeper.ZooKeeperClient;
import com.hkfs.fundamental.zookeeper.constants.PropertiesKey;
import org.apache.curator.framework.recipes.leader.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 任务基类
 * @Author dzr
 * @Date 2016/04/27
 */
public abstract class SimpleLeaderTakeTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLeaderTakeTask.class);
    
    private static final String TASK_LEADER_COMPETITION_PATH_BASE = FundamentalConfigProvider.getString(PropertiesKey.TASK_LEADER_COMPETITION_PATH_BASE);
    
    @Resource
    protected ZooKeeperClient zkClient;
    
    protected String taskTypeId;
    
    protected String taskLeaderCompetitionPath;
    
    private String localHostIp;

    private ReentrantLock lock = new ReentrantLock();

    @PostConstruct
    public void init() {
    	try {
    		localHostIp = getLocalhostIP();
    	}catch(Exception e){
    		localHostIp = "127.0.0.1";
    	}
        taskTypeId = getClass().getName();
        taskLeaderCompetitionPath = getTaskLeaderCompetitionPath(taskTypeId);
        // 先初始化竞争
        isThisNodeLeader();
    }

    public static String getLocalhostIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException("get localhost IP failed", e);
        }
    }

    @PreDestroy
    public void destroy(){
        zkClient.closeLeaderLatch(taskLeaderCompetitionPath);
    }
    
    protected String getTaskLeaderCompetitionPath(String taskTypeId) {
        return ZooKeeperClient.buildFullPath(TASK_LEADER_COMPETITION_PATH_BASE, taskTypeId);
    }
    
    protected boolean isThisNodeLeader() {
        return zkClient.thisNodeIsLeader(taskLeaderCompetitionPath, localHostIp);
    }

    protected void printCurrentLeader(){
        Participant participant = zkClient.getLeaderOfThisNode(taskLeaderCompetitionPath, localHostIp);
        if(participant != null){
            LOGGER.info(" Current Leader:" + participant.getId() +" isLeader:" +participant.isLeader());
        }else {
            LOGGER.info(" Current Leader is null");
        }
    }
    public abstract boolean isTaskenable();
    
    public void execute() {
        if (!isTaskenable()) {
            return;
        }
//        printCurrentLeader();
        if (!isThisNodeLeader()) {
            LOGGER.info(taskTypeId + " Not Leader");
            printCurrentLeader();
            return;
        }
        if (lock.isLocked()) {
            LOGGER.warn(taskTypeId + " is Locked ...");
            return;
        }
        /*ExecutorService executorService = ExecutorUtils.getCachedThreadPool("hkfsTask");
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    LOGGER.info(taskTypeId+" Start Execute ...");
                    lock.lock();
                    doTask();
                    LOGGER.info(taskTypeId+" End Execute ...");
                } catch (Exception e) {
                    LOGGER.error("execute task failed: " + taskTypeId, e);
                } finally {
                    lock.unlock();
                }
            }
        });*/
        try {
            LOGGER.info(taskTypeId+" Start Execute ...");
            lock.lock();
            doTask();
            LOGGER.info(taskTypeId+" End Execute ...");
        } catch (Exception e) {
            LOGGER.error("execute task failed: " + taskTypeId, e);
        } finally {
            lock.unlock();
        }
    }
    
    public abstract void doTask();
    
}
