package com.sinotopia.fundamental.zookeeper.seckill;

import com.sinotopia.fundamental.config.FundamentalConfigProvider;
import com.sinotopia.fundamental.zookeeper.ZooKeeperClient;
import com.sinotopia.fundamental.zookeeper.bean.ResultContainer;
import com.sinotopia.fundamental.zookeeper.callback.ZooKeeperLockUseDataCallback;
import com.sinotopia.fundamental.zookeeper.constants.PropertiesKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class SecKillService {

	@Resource
	protected ZooKeeperClient zkClient;

	private static final Integer LOCK_TIMEOUT = 5000;

	private static final String BIZ_SECKILL_BASE = FundamentalConfigProvider.getString(PropertiesKey.SECKILL_BASE_PATH);

	private static final String BIZ_SECKILL_PATH = BIZ_SECKILL_BASE+"/";

	private static Logger LOG = LoggerFactory.getLogger(SecKillService.class);

	public void setZkClient(ZooKeeperClient zkClient) {
		this.zkClient = zkClient;
	}

	/**
	 * 秒杀路径
	 * @param secKillPath
	 * @return
	 */
	public <T> T secKill(final String secKillPath, final SecKillHandler<T> handler) {
		T result = zkClient.lockAndWork(BIZ_SECKILL_PATH+secKillPath, LOCK_TIMEOUT, TimeUnit.SECONDS,
				new ZooKeeperLockUseDataCallback<T>() {
					@Override
					public String useData(String lockedPath, String data, ResultContainer<T> resultContainer,
							ZooKeeperClient client) {
						return handler.process(lockedPath, data, resultContainer,client);
					}
				});
		return result;
	}
	
	/**
	 * 活动初始化
	 */
	public void initSecKillData(String secKillPath,String secKillData) {
		String path = BIZ_SECKILL_PATH+secKillPath;
		LOG.info("Create Seckill Activity Path:"+path);
		if (!zkClient.exists(path)) {
		    zkClient.setData(path, secKillData);
		}
	}

	/**
	 * 清除秒杀数据
	 */
    public void clearSecKillData() {
        try {
            String path = BIZ_SECKILL_BASE;
            List<String> subPaths = zkClient.getChildren(path);
            for (String sub :  subPaths) {
                zkClient.delete(BIZ_SECKILL_PATH+sub);
            }
        }catch(Exception e) {
            LOG.error(e.toString(), e);
        }
    }

	/**
	 * 清除某个秒杀数据
	 * @param secKillPath
	 */
	public void clearSecKillData(String secKillPath){
		String path = BIZ_SECKILL_PATH+secKillPath;
		LOG.info("Create Seckill Activity Path:"+path);
		if (!zkClient.exists(path)) {
			zkClient.delete(path);
		}
	}
}
