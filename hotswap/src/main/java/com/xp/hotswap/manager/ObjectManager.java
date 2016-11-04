package com.xp.hotswap.manager;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.xp.hotswap.lock.CommLock;

public class ObjectManager {

	private Map<Class<?>, Class<?>> classMap = new ConcurrentHashMap<Class<?>, Class<?>>();
	private Map<Class<?>, Object> objMap = new ConcurrentHashMap<Class<?>, Object>();
	private Set<Class<?>> notLoad = new CopyOnWriteArraySet<Class<?>>();
	private static ObjectManager obj = new ObjectManager();

	private ObjectManager() {
	}

	public static ObjectManager getInstance() {
		return obj;
	}

	public Object getBeans(Class<?> interfaceObj) throws Exception {
		if (!CommLock.initFlag) {
			synchronized (CommLock.INIT_LOCK) {
				CommLock.INIT_LOCK.wait();

			}
		}
		if (objMap.containsKey(interfaceObj)) {
			return objMap.get(interfaceObj);
		}
		while (!classMap.containsKey(interfaceObj)) {
			synchronized (CommLock.BEAN_LOCK) {
				notLoad.add(interfaceObj);
				CommLock.BEAN_LOCK.wait();
			}
		}
		Object newInstance = classMap.get(interfaceObj).newInstance();
		objMap.put(interfaceObj, newInstance);
		return newInstance;

	}
	
	
	public Object getNewBeans(Class<?> interfaceObj) throws Exception {
		if (!CommLock.initFlag) {
			synchronized (CommLock.INIT_LOCK) {
				CommLock.INIT_LOCK.wait();

			}
		}
		while (!classMap.containsKey(interfaceObj)) {
			synchronized (CommLock.BEAN_LOCK) {
				notLoad.add(interfaceObj);
				CommLock.BEAN_LOCK.wait();
			}
		}
		Object newInstance = classMap.get(interfaceObj).newInstance();
		return newInstance;

	}

	public void add(Class<?> interfaceObj, Class<?> implObj) {
		classMap.put(interfaceObj, implObj);
	}

	public void update(Set<String> classUpdate) {
		for (String tmpClass : classUpdate) {
			try {
				Class<?> interfaceObj = Class.forName(tmpClass);
				Object newInstance = classMap.get(interfaceObj).newInstance();
				objMap.put(interfaceObj, newInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void remove(Set<String> classDeleted) {
		for (String tmpClass : classDeleted) {
			try {
				Class<?> className = Class.forName(tmpClass);
				classMap.remove(className);
				objMap.remove(className);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public Set<Class<?>> getNotLoad() {
		return notLoad;
	}

	public void setNotLoad(Set<Class<?>> notLoad) {
		this.notLoad = notLoad;
	}

}
