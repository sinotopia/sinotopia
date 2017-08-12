package com.hkfs.fundamental.common.cache;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author brucezee 2013-3-24 下午6:37:58
 * @param <K, V>
 */
public class LRULinkedHashMapCache<K, V> extends LinkedHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_MAX_CAPACITY = 160;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private final int maxCapacity;
	private final Lock lock = new ReentrantLock();

	public LRULinkedHashMapCache(int maxCapacity) {
		super(maxCapacity, DEFAULT_LOAD_FACTOR, true);
		this.maxCapacity = maxCapacity;
	}
	
	public LRULinkedHashMapCache() {
		super(DEFAULT_MAX_CAPACITY, DEFAULT_LOAD_FACTOR, true);
		this.maxCapacity = DEFAULT_MAX_CAPACITY;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return size() > maxCapacity;
	}

	@Override
	public V get(Object key) {
		try {
			lock.lock();
			return super.get(key);
		}
		finally {
			lock.unlock();
		}
	}

	@Override
	public V put(K key, V value) {
		try {
			lock.lock();
			return super.put(key, value);
		}
		finally {
			lock.unlock();
		}
	}
	
	@Override 
	public void clear() { 
		try { 
			lock.lock(); 
			super.clear(); 
		}
		finally { 
			lock.unlock(); 
		} 
	}

	@Override 
	public boolean containsValue(Object value) { 
		try { 
			lock.lock(); 
			return super.containsValue(value); 
		}
		finally { 
			lock.unlock(); 
		}
	} 
	
	@Override 
	public boolean containsKey(Object key) { 
		try { 
			lock.lock(); 
			return super.containsKey(key); 
		}
		finally { 
			lock.unlock(); 
		} 
	} 

	@Override
	public boolean isEmpty() {
		try {
			lock.lock();
			return super.isEmpty();
		} 
		finally {
			lock.unlock();
		}
	}

	@Override
	public V remove(Object key) {
		try {
			lock.lock();
			return super.remove(key);
		} 
		finally {
			lock.unlock();
		}
	}

	@Override
	public int size() {
		try {
			lock.lock();
			return super.size();
		}
		finally {
			lock.unlock();
		}
	}
}
