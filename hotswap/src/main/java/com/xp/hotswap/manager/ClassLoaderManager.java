package com.xp.hotswap.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.xp.hotswap.classloader.HotClassloader;
import com.xp.hotswap.lock.CommLock;
import com.xp.hotswap.task.FileDeleteTask;

public class ClassLoaderManager {

	private Executor threadPool = Executors.newFixedThreadPool(5);

	private Map<String,ClassLoader> classLoaderMap =new HashMap<String,ClassLoader>();
	
	private static ClassLoaderManager obj = new ClassLoaderManager();

	public static ClassLoaderManager getInstance() {
		return obj;
	}

	private ClassLoaderManager() {
	}

	public void AddFile(File file, Map<String, String> classFind) {
		File[] files = file.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});
		URL[] url = new URL[files.length];
		int len = 0;
		for (File tmpFile : files) {
			try {
				url[len++] = tmpFile.toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		ClassLoader hotClassLoader = new HotClassloader(url);
		loadClass(hotClassLoader, classFind);
		classLoaderMap.put(file.getName(), hotClassLoader);
	}

	public void deleteFile(File file, Map<String, String> classDelete) {
		classLoaderMap.remove(file.getName());
		ObjectManager.getInstance().remove(classDelete.keySet());
		if (file.exists()) {
			threadPool.execute(new FileDeleteTask(file));
		}
	}

	public void updateFile(File file, Map<String, String> classUpdate) {
		loadClass(classLoaderMap.get(file.getName()), classUpdate);
		ObjectManager.getInstance().update(classUpdate.keySet());
	}
	
	public void loadClass(ClassLoader loader, Map<String, String> classFind) {
		Set<Entry<String, String>> entrySet = classFind.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String objImpl = entry.getValue();
			String objInterface = entry.getKey();
			try {
				Class<?> loadInterface = loader.loadClass(objInterface);
				Class<?> loadImpl = loader.loadClass(objImpl);
				ObjectManager.getInstance().add(loadInterface, loadImpl);
				if (ObjectManager.getInstance().getNotLoad().contains(loadInterface)) {
					synchronized (CommLock.BEAN_LOCK) {
						CommLock.BEAN_LOCK.notifyAll();
					}
					ObjectManager.getInstance().getNotLoad().remove(loadInterface);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
	}
}
