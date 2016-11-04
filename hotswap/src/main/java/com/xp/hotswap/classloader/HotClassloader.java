package com.xp.hotswap.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class HotClassloader extends URLClassLoader {

	public HotClassloader(URL[] urls) {
		super(urls);
	}

	Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> classLoaded = classMap.get(name);
		if (classLoaded != null) {
			return classLoaded;
		}
		Class<?> findClass = null;
		try {
			findClass = findClass(name);
		} catch (Exception e) {

		}
		if (findClass != null) {
			classMap.put(name, findClass);
			return findClass;
		}
		return super.loadClass(name);
	}

	@Override
	protected Package getPackage(String name) {
		return null;
	}



}
