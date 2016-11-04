package com.xp.hotswap.manager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.xp.hotswap.lock.CommLock;
import com.xp.hotswap.lock.CommonString;

public class FileManager implements Runnable {

	private File file;
	private Long checkTime = 3000L;
	private Map<String, Long> fileLoaded = new HashMap<String, Long>();

	public FileManager(File file, Long checkTime) {
		this.file = file;
		if (checkTime > 0) {
			this.checkTime = checkTime;
		}
	}

	public void check() throws Exception {
		File[] listFiles = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory()) {
					return true;
				}
				return false;
			}
		});

		for (File tmpFile : listFiles) {
			File pro = new File(tmpFile, CommonString.CONFIG_FILE);
			Map<String, String> filePro = new HashMap<String, String>();
			if (pro.exists()) {
				try (InputStream in = new FileInputStream(pro)) {
					Properties p = new Properties();
					p.load(in);
					Set<String> stringPropertyNames = p.stringPropertyNames();
					for (String key : stringPropertyNames) {
						filePro.put(key, p.getProperty(key));
					}
				}
			} else {
				continue;
			}
			if (filePro.get(CommonString.DELETE_FLAG) != null) {
				Boolean isDelete = Boolean.valueOf(filePro.get(CommonString.DELETE_FLAG));
				filePro.remove(CommonString.DELETE_FLAG);
				if (isDelete) {
					//delete file
					ClassLoaderManager.getInstance().deleteFile(tmpFile, filePro);
					fileLoaded.remove(tmpFile.getName());
					continue;
				}
			}
			if (!fileLoaded.containsKey(tmpFile.getName())) {
				// add new file
				ClassLoaderManager.getInstance().AddFile(tmpFile, filePro);
				fileLoaded.put(tmpFile.getName(), pro.lastModified());
			} else {
				Long modifTime = fileLoaded.get(tmpFile.getName());
				// System.out.println(modifTime+":"+tmpFile.lastModified());
				if (modifTime != pro.lastModified()) {
					/*
					 * update no support delete
					 */
					ClassLoaderManager.getInstance().updateFile(tmpFile, filePro);
				} else {
					// no change
					continue;
				}
			}

		}
		/*
		 * notify lock when init sussess
		 */
		if (!CommLock.initFlag) {
			synchronized (CommLock.INIT_LOCK) {
				CommLock.INIT_LOCK.notifyAll();
			}
			CommLock.initFlag = true;
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				check();
				/*
				 * check file time
				 */
				Thread.sleep(checkTime);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

}
