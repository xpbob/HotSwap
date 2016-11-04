package com.xp.hotswap.builder;

import com.xp.hotswap.manager.ObjectManager;

public class ApplicationContext {
	public static Object getBeans(Class<?> interfaceObj) throws Exception {
		return ObjectManager.getInstance().getBeans(interfaceObj);
	}
	public static Object getNewBeans(Class<?> interfaceObj) throws Exception {
		return ObjectManager.getInstance().getNewBeans(interfaceObj);
	}
}
