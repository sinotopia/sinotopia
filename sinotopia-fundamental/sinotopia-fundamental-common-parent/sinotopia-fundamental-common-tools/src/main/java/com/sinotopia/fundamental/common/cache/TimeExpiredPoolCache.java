package com.sinotopia.fundamental.common.cache;

import com.sinotopia.fundamental.common.utils.ThreadUtils;
import com.sinotopia.fundamental.common.utils.TimeUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class TimeExpiredPoolCache {
	private long defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * 5;//默认5分钟
	
	/**
	 * 构造一个默认指定过期的时间分钟数的缓存对象
	 */
	public TimeExpiredPoolCache(int defaultCachedMinutes) {
		if (defaultCachedMinutes <= 0) {
			throw new IllegalArgumentException("illegal default cached miniutes value : "+defaultCachedMinutes);
		}
		this.defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * defaultCachedMinutes;
	}
	/**
	 * 构造一个默认5分钟过期的时间缓存对象
	 */
	public TimeExpiredPoolCache(){}
	
	/**
	 * 设置默认的缓存时间，单位（分）
	 * @param defaultCacheMinutes 缓存时间（分）
	 */
	public void setDefaultCacheMinutes(int defaultCacheMinutes) {
		this.defaultCachedMillis = TimeUtils.MILLIS_OF_MINUTE * defaultCacheMinutes;
	}
	
	/**
	 * 设置默认的缓存时间，单位（毫秒）
	 * @param defaultCachedMillis 缓存时间（毫秒）
	 */
	public void setDefaultCachedMillis(long defaultCachedMillis) {
		this.defaultCachedMillis = defaultCachedMillis;
	}
	
	/**
	 * 设置默认的缓存时间，单位（小时）
	 * @param defaultCachedHour 缓存时间（小时）
	 */
	public void setDefaultCachedHour(long defaultCachedHour) {
		this.defaultCachedMillis = TimeUtils.MILLIS_OF_HOUR * defaultCachedHour;
	}
	
	private static class DataWrapper {
		private Object data;
		private long expiredTime;
		private DataRenewer dataRenewer;
		private long cachedMillis;
		
		private DataWrapper(Object data, long cachedMillis) {
			this(data, cachedMillis, null);
		}
		private DataWrapper(Object data, long cachedMillis, DataRenewer dataRenewer) {
			this.update(data, cachedMillis, dataRenewer);
		}
		public void update(Object data, long cachedMillis, DataRenewer dataRenewer) {
			this.data = data;
			this.cachedMillis = cachedMillis;
			this.dataRenewer = dataRenewer;
			this.updateExpiredTime();
		}
		public void updateExpiredTime() {
			this.expiredTime = System.currentTimeMillis() + cachedMillis;
		}
		public void updateData(Object data) {
			this.data = data;
			this.updateExpiredTime();
		}
		public boolean isExpired() {
			if (this.expiredTime > 0) {
				return System.currentTimeMillis() > this.expiredTime;
			}
			return true;
		}
	}
	
	public interface DataRenewer {
		public Object renewData();
	}
	
	private ConcurrentHashMap<String, DataWrapper> pool = new ConcurrentHashMap<String, DataWrapper>();
	
	public int getPoolSize() {
		return pool.size();
	}
	
	/**
	 * 缓存一个对象
	 * @param key 对象的唯一标示符
	 * @param data 数据对象
	 * @param cachedMillis 缓存过期时间
	 * @param dataRenewer 数据刷新器
	 */
	public Object put(String key, Object data, long cachedMillis, DataRenewer dataRenewer) {
		DataWrapper dataWrapper = pool.get(key);
		
		if (data == null && dataRenewer != null) {
			data = dataRenewer.renewData();
		}
		
		if (dataWrapper != null) {
			//更新
			dataWrapper.update(data, cachedMillis, dataRenewer);
		}
		else {
			dataWrapper = new DataWrapper(data, cachedMillis, dataRenewer);
			pool.put(key, dataWrapper);
		}
		return data;
	}
	/**
	 * 缓存一个对象，使用默认的过期时间
	 * @param key 对象的唯一标示符
	 * @param data 数据对象
	 * @param dataRenewer 数据刷新器
	 */
	public Object put(String key, Object data, DataRenewer dataRenewer) {
		return put(key, data, defaultCachedMillis, dataRenewer);
	}
	/**
	 * 缓存一个对象，使用默认的过期时间，使用DataRenewer初始化获取数据
	 * @param key 对象的唯一标示符
	 * @param dataRenewer 数据刷新器
	 */
	public Object put(String key, DataRenewer dataRenewer) {
		return put(key, null, defaultCachedMillis, dataRenewer);
	}
	/**
	 * 缓存一个对象，使用默认的过期时间
	 * @param key 对象的唯一标示符
	 * @param data 数据对象
	 */
	public Object put(String key, Object data) {
		return put(key, data, null);
	}
	/**
	 * 缓存一个对象
	 * @param key 对象的唯一标示符
	 * @param data 数据对象
	 * @param cachedMillis 缓存时间毫秒数
	 */
	public Object put(String key, Object data, long cachedMillis) {
		return put(key, data, cachedMillis, null);
	}
	
	/**
	 * 获取缓存的数据，如果数据过期则刷新数据返回最新数据并缓存
	 * @param key 对象的唯一标示符
	 * @return 缓存的对象
	 */
	public Object get(String key) {
		DataWrapper dataWrapper = pool.get(key);
		if (dataWrapper == null) {
			return null;
		}
		if (dataWrapper.isExpired()) {
			DataRenewer dataRenewer = dataWrapper.dataRenewer;
			if (dataRenewer != null) {
				Object renewData = dataRenewer.renewData();
				dataWrapper.updateData(renewData);
				return renewData;
			}
			else {
				pool.remove(key);//清除过期的缓存
				return null;
			}
		}
		return dataWrapper.data;
	}
	
	/**
	 * 获取缓存的数据，如果数据没有初始化过则刷新数据返回最新数据并缓存
	 * @param key 对象的唯一标示符
	 * @param dataRenewer 数据刷新器
	 * @return 缓存的对象
	 */
	public Object get(String key, DataRenewer dataRenewer) {
		return get(key, defaultCachedMillis, dataRenewer);
	}
	
	/**
	 * 获取缓存的数据，如果数据没有初始化过则刷新数据返回最新数据并缓存
	 * @param key 对象的唯一标示符
	 * @param cachedMillis 缓存毫秒数
	 * @param dataRenewer 数据刷新器
	 * @return 缓存的对象
	 */
	public Object get(String key, long cachedMillis, DataRenewer dataRenewer) {
		Object data = get(key);
		if (data != null) {
			return data;
		}
		return put(key, null, cachedMillis, dataRenewer);
	}
	
	/**
	 * 清除缓存
	 */
	public void clear() {
		this.pool.clear();
	}

	/**
	 * 清除指定缓存
	 * @param key 缓存key
	 */
	public void clear(String key) {
		this.pool.remove(key);
	}

	/**
	 * 清除过期的缓存
	 */
	public void clearExpiredCaches() {
		Iterator<Entry<String, DataWrapper>> it = pool.entrySet().iterator();
		Entry<String, DataWrapper> entry;
		List<String> expiredKeyList = new LinkedList<String>();
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getValue().isExpired()) {
				expiredKeyList.add(entry.getKey());
			}
		}
		for (String key : expiredKeyList) {
			pool.remove(key);
		}
	}
	
	public static void main(String[] args) throws Exception {
		TimeExpiredPoolCache cache = new TimeExpiredPoolCache();
		cache.put("bruce", new Boy("bruce"), 3000);
		cache.put("lily", new Boy("lily"), 5000, new DataRenewer() {
			@Override
			public Object renewData() {
				return new Boy("lily");
			}
		});
		
		
		for (int i=0;i<10;i++) {
			ThreadUtils.sleep(1000);
			Boy boy3 = (Boy) cache.get("xiaoming", 5000, new DataRenewer() {
				@Override
				public Object renewData() {
					System.out.println("NEW");
					return new Boy("xiaoming");
				}
			});
			System.out.println(boy3);
			cache.put(""+i, new Boy(""+i));
		}
		System.out.println(cache.getPoolSize());
		cache.clearExpiredCaches();
		System.out.println(cache.getPoolSize());
		for (int i=0;i<10;i++) {
			ThreadUtils.sleep(1000);
			Boy boy = (Boy) cache.get("lily");
			System.out.println(boy);
		}
		cache.clearExpiredCaches();
		System.out.println(cache.getPoolSize());
	}
	
	static class Boy {
		public String name;
		public Boy(String name){
			this.name = name;
		}
		@Override
		public String toString() {
			return name+" "+super.toString();
		}
	}
}
