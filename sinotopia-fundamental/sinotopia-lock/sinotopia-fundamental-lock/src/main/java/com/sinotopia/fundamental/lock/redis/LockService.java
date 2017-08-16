package com.sinotopia.fundamental.lock.redis;

import java.util.List;

public interface LockService {
	/**
	 * 默认尝试锁定的时间毫秒数
	 */
	public static final long DEFAULT_TRY_LOCK_MILLIS = 10000L;
	/**
	 * 默认锁成功后自动解锁的时间好描述
	 */
	public static final long DEFAULT_AUTO_UNLOCK_MILLIS = 20000L;
	
	/**
	 * 根据一个关键字进行锁定，指定尝试时间和自动解锁时间，并配以一个锁id用于排他解锁
	 * @param key 锁定的关键字
	 * @param tryLockMillis 尝试锁定的时间，单位毫秒
	 * @param autoUnlockMillis 锁成功后自动解锁的时间，单位毫秒
	 * @param lockId 锁id，锁定和解锁只能是同一个lockId
	 * @return 成功返回true，失败或超时返回false。
	 */
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, Object lockId);
	/**
	 * 根据一个关键字进行锁定，指定尝试时间和自动解锁时间，并配以一个锁id用于排他解锁
	 * @param key 锁定的关键字
	 * @param tryLockMillis 尝试锁定的时间，单位毫秒
	 * @param autoUnlockMillis 锁成功后自动解锁的时间，单位毫秒
	 * @param lockId 锁id，锁定和解锁只能是同一个lockId
	 * @param callback 锁定成功后的回调
	 * @return 成功返回true，失败或超时返回false。
	 */
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, Object lockId, LockCallback callback);
	/**
	 * 根据一个关键字进行解锁
	 * @param key 锁定的关键字
	 * @param lockId 锁id，锁定和解锁只能是同一个lockId
	 * @return 成功返回true，失败返回false。
	 */
	public Boolean unlock(String key, Object lockId);

	/**
	 * 根据一个关键字进行锁定，指定尝试时间和自动解锁时间
	 * @param key 锁定的关键字
	 * @param tryLockMillis 尝试锁定的时间，单位毫秒
	 * @param autoUnlockMillis 锁成功后自动解锁的时间，单位毫秒
	 * @return 成功返回true，失败或超时返回false。
	 */
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis);
	/**
	 * 根据一个关键字进行锁定，指定尝试时间和自动解锁时间
	 * @param key 锁定的关键字
	 * @param tryLockMillis 尝试锁定的时间，单位毫秒
	 * @param autoUnlockMillis 锁成功后自动解锁的时间，单位毫秒
	 * @param callback 锁定成功后的回调
	 * @return 成功返回true，失败或超时返回false。
	 */
	public Boolean lock(String key, Long tryLockMillis, Long autoUnlockMillis, LockCallback callback);
	/**
	 * 根据一个关键字进行解锁
	 * @param key 锁定的关键字
	 * @return 成功返回true，失败返回false。
	 */
	public Boolean unlock(String key);
	
	/**
	 * 根据多个关键字进行锁定，指定尝试时间和自动解锁时间
	 * @param keyList 锁定的多个关键字
	 * @param tryLockMillis 尝试锁定的时间，单位毫秒
	 * @param autoUnlockMillis 锁成功后自动解锁的时间，单位毫秒
	 * @param lockId 锁id，锁定和解锁只能是同一个lockId
	 * @return 成功返回true，失败或超时返回false。
	 */
	@Deprecated
	public Boolean lock(List<String> keyList, Long tryLockMillis, Long autoUnlockMillis, Object lockId);
	
	/**
	 * 根据一个关键字进行解锁
	 * @param keyList 锁定的多个关键字
	 * @param lockId 锁id，锁定和解锁只能是同一个lockId
	 * @return 成功返回true，失败返回false。
	 */
	@Deprecated
	public Boolean unlock(List<String> keyList, Object lockId);
	
	/**
	 * 根据一个关键字进行解锁
	 * @param keyList 锁定的多个关键字
	 * @return 成功返回true，失败返回false。
	 */
	@Deprecated
	public Boolean unlock(List<String> keyList);
}
