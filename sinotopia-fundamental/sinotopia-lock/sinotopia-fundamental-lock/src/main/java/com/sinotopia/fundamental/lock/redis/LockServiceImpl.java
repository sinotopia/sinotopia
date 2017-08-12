package com.hkfs.fundamental.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LockServiceImpl implements LockService, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LockServiceImpl.class);
	/**
	 * redis 连接池
	 */
	private ShardedJedisPool shardedJedisPool;

	public ShardedJedisPool getShardedJedisPool() {
		return shardedJedisPool;
	}

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}


	protected ShardedJedis getResource() {
		return shardedJedisPool.getResource();
	}

	/**
	 * 销毁连接
	 * @param shardedJedis
	 */
	protected void returnBrokenResource(ShardedJedis shardedJedis) {
		if (shardedJedis != null) {
			try {
				shardedJedisPool.returnBrokenResource(shardedJedis);
			}
			catch (Exception e) {
				log(e);
			}
		}
	}

	/**
	 * 销毁连接
	 * @param shardedJedis
	 */
	protected void returnResource(ShardedJedis shardedJedis) {
		if (shardedJedis != null) {
			try {
				shardedJedisPool.returnResource(shardedJedis);
			}
			catch (Exception e) {
				log(e);
			}
		}
	}

	/**
	 * 默认过期时间 一天
	 */
	private static final int DEFAULT_EXPIRED_SECONDS = 60*60*24;//秒

	@Override
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, Object lockId) {
		return lock(key, tryLockMillis, autoUnlockMillis, lockId, null);
	}

	@Override
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, Object lockId, LockCallback callback) {
		Boolean success = false;
		try {
			success = doLock(key, tryLockMillis, autoUnlockMillis, lockId);
			if (success && callback != null) {
				callback.onSuccess(key, lockId);
			}
		}
		finally {
			if (success && callback != null) {
				unlock(key, lockId);
			}
		}
		return success;
	}

	@Override
	public Boolean unlock(String key, Object lockId) {
		ShardedJedis shardedJedis = null;
		String lockValue = null;
		try {  
			shardedJedis = getResource();  
			if(lockId != null) {
				String existingValue = shardedJedis.get(key);
				lockValue = processLockValue(key, lockId);
				if (!lockValue.equals(existingValue)){
					log("解锁失败：key[",key,"] try unlock value[",lockValue,"] existing lock value[",existingValue,"]");
					return false;
				}
			}
			shardedJedis.del(key);  
			log("解锁成功：key[",key,"]",(lockId!=null?("lockId[ "+lockId+" ] lockValue[ "+lockValue+" ]"):""));
			return true;
		}
		catch (JedisConnectionException je) {  
			log(je);
			returnBrokenResource(shardedJedis);  
		}
		catch (Exception e) {  
			log(e);
		}
		finally {  
			returnResource(shardedJedis);
		}
		log("解锁失败：key[",key,"]",(lockId!=null?("lockId[ "+lockId+" ]"):""));
		return false;
	}

	@Override
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis) {
		return lock(key, tryLockMillis, autoUnlockMillis, null, null);
	}

	@Override
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, LockCallback callback) {
		return lock(key, tryLockMillis, autoUnlockMillis, null, callback);
	}

	@Override
	public Boolean unlock(String key) {
		return unlock(key, null);
	}

	@Override
	@Deprecated
	public Boolean lock(List<String> keyList, Long tryLockMillis, Long autoUnlockMillis, Object lockId) {
		return doLock(keyList, tryLockMillis, autoUnlockMillis, lockId);
	}

	@Override
	@Deprecated
	public Boolean unlock(List<String> keyList, Object lockId) {
		ShardedJedis shardedJedis = null;
		List<String> lockValueList = null;
		try {  
			shardedJedis = getResource();
			if(lockId != null) {
				String lockValue = null;
				lockValueList = new LinkedList<String>();
				for (String key : keyList) {
					String existingValue = shardedJedis.get(key);
					lockValue = processLockValue(key, lockId);
					if (!lockValue.equals(existingValue)){
						log("解锁失败：key[",key,"] try unlock value[",lockValue,"] existing lock value[",existingValue,"]");
						return false;
					}
				}
			}
			
			for (String key : keyList) {
				shardedJedis.del(key);
			}
			log("解锁成功：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ] lockValue[ "+(lockValueList!=null?"":lockValueList)+" ]"):""));
			return true;
		}
		catch (JedisConnectionException je) {  
			log(je);
			returnBrokenResource(shardedJedis);  
		}
		catch (Exception e) {  
			log(e);
		}
		finally {  
			returnResource(shardedJedis);
		}
		log("解锁失败：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""));
		return false;
	}

	@Override
	@Deprecated
	public Boolean unlock(List<String> keyList) {
		return unlock(keyList, null);
	}
	
	private Integer millisecondsToSeconds(Long millis) {
		if (millis != null) {
			return (int) (millis/1000);
		}
		return null;
	}
	
	public Boolean doLock(String key, Long tryLockMillis, Long autoUnlockMillis, Object lockId) {
		ShardedJedis shardedJedis = null;
		
		long takesTime = 0;
		Integer expiredSeconds = millisecondsToSeconds(autoUnlockMillis);
		try {
			long startTime = System.nanoTime();
			shardedJedis = getResource();

			do {
				if (handleLock(shardedJedis, key, lockId, expiredSeconds)) {
					log("锁定成功：key[",key,"]",(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
					return true;
				}
				if (tryLockMillis == null || tryLockMillis <= 0) {
					break;
				}

				Thread.sleep(50);

				takesTime = System.nanoTime() - startTime;
			} while (takesTime <= TimeUnit.MILLISECONDS.toNanos(tryLockMillis));

			log("锁定超时：key[",key,"]",(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
			return false;
		}
		catch (JedisConnectionException je) {  
			log(je);
			returnBrokenResource(shardedJedis);  
		}
		catch (Exception e) {
			log(e);
		}
		finally {  
			returnResource(shardedJedis);  
		}
		log("锁定失败：key[",key,"]",(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
		return false;
	}
	
	private boolean handleLock(ShardedJedis shardedJedis, String key, Object lockId, Integer expiredSeconds){
		String lockValue = processLockValue(key, lockId);
		Long i = shardedJedis.setnx(key, lockValue);
		if (i != null && i == 1) {
			//设置过期时间
			shardedJedis.expire(key, (expiredSeconds == null ? DEFAULT_EXPIRED_SECONDS : expiredSeconds));
			return true;
		}
		return false;
	}
	
	private String processLockValue(String key, Object lockId) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		if (lockId != null) {
			sb.append("_").append(lockId);
		}
		return sb.toString();
	}

	@Deprecated
	public Boolean doLock(List<String> keyList, Long tryLockMillis, Long autoUnlockMillis, Object lockId) {
		ShardedJedis shardedJedis = null;
		
		long takesTime = 0;
		Integer expiredSeconds = millisecondsToSeconds(autoUnlockMillis);
		HashSet<String> lockedKeySet = new HashSet<String>(keyList.size());//已锁定的关键字
		boolean locked = false;
		try {
			long startTime = System.nanoTime();
			shardedJedis = getResource();
			List<String> tryLockKeyList = new LinkedList<String>();
			do {
				tryLockKeyList.clear();
				
				//构建pipeline，批量提交
				ShardedJedisPipeline pipeline = shardedJedis.pipelined();
				for (String key : keyList) {
					if (!lockedKeySet.contains(key)) {
						tryLockKeyList.add(key);
						pipeline.setnx(key, processLockValue(key, lockId));
					}
				}
				
				if (tryLockKeyList.isEmpty()) {
					//没有需要锁定的关键字
					locked = true;
					log("锁定成功：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
					return true;
				}
				
				List<Object> lockResultList = pipeline.syncAndReturnAll();
				int total = lockResultList.size();
				for (int i=0;i<total;i++) {
					Long lockResult = (Long) lockResultList.get(i);
					String key = tryLockKeyList.get(i);
					if (lockResult != null && lockResult == 1) {
						lockedKeySet.add(key);
						shardedJedis.expire(key, (expiredSeconds == null ? DEFAULT_EXPIRED_SECONDS : expiredSeconds));
					}
				}
				
				if (lockedKeySet.size() == keyList.size()) {
					locked = true;
					log("锁定成功：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
					return true;
				}
				
				if (tryLockMillis == null || tryLockMillis <= 0) {
					break;
				}
				
				Thread.sleep(50);
				
				takesTime = System.nanoTime() - startTime;
			} while (takesTime <= TimeUnit.MILLISECONDS.toNanos(tryLockMillis));
			
			log("锁定超时：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
			return false;
		}
		catch (JedisConnectionException je) {  
			log(je);
			returnBrokenResource(shardedJedis);  
		}
		catch (Exception e) {
			log(e);
		}
		finally {
			if (!locked) {//锁定失败，需要将已经锁定的关键字解锁
				for (String key : keyList) {
					if (lockedKeySet.contains(key)) {
						try {
							shardedJedis.del(key);
						} catch (Exception e2) {
							log(e2);
						}
					}
				}
			}
			returnResource(shardedJedis);  
		}
		log("锁定失败：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
		return false;
	}

	@Deprecated
	public Boolean doLock2(List<String> keyList, Long tryLockMillis, Long autoUnlockMillis, Object lockId) {
		ShardedJedis shardedJedis = null;
		
		long takesTime = 0;
		Integer expiredSeconds = millisecondsToSeconds(autoUnlockMillis);
		List<String> lockedKeyList = new LinkedList<String>();
		boolean locked = false;
		try {
			long startTime = System.nanoTime();
			shardedJedis = getResource();
			
			outer:for (String key : keyList) {
				takesTime = System.nanoTime() - startTime;
				if (takesTime > TimeUnit.MILLISECONDS.toNanos(tryLockMillis)) {
					log("锁定超时：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
					return false;
				}
				
				do {
					if (handleLock(shardedJedis, key, lockId, expiredSeconds)) {
						lockedKeyList.add(key);
						continue outer;
					}
					if (tryLockMillis == null || tryLockMillis <= 0) {
						log("锁定失败：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
						return false;
					}
					
					Thread.sleep(50);
					
					takesTime = System.nanoTime() - startTime;
				} while (takesTime <= TimeUnit.MILLISECONDS.toNanos(tryLockMillis));
				
				
				log("锁定超时：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
				return false;
			}
			
			locked = true;
			log("锁定成功：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
			return true;
		}
		catch (JedisConnectionException je) {  
			log(je);
			returnBrokenResource(shardedJedis);  
		}
		catch (Exception e) {
			log(e);
		}
		finally {
			if (!locked && lockedKeyList.size() > 0) {
				for (String key : lockedKeyList) {
					try {
						shardedJedis.del(key);
					} catch (Exception e2) {
						log(e2);
					}
				}
			}
			returnResource(shardedJedis);
		}
		log("锁定失败：keyList",keyList,(lockId!=null?("lockId[ "+lockId+" ]"):""),"takes[",TimeUnit.NANOSECONDS.toMillis(takesTime),"ms] tryLockMillis[",tryLockMillis,"ms] autoUnlockMillis[",autoUnlockMillis,"ms]");
		return false;
	}

	private void log(Throwable e) {
		logger.info(e.toString(), e);
	}
	private void log(Object...args) {
		StringBuilder sb = new StringBuilder();
		for (Object arg : args) {
			sb.append(arg).append(" ");
		}
		logger.info(sb.toString());
	}
}
