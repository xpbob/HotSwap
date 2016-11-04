package com.xp.hotswap.task;

import java.io.File;

import com.xp.hotswap.lock.CommonConfig;

public class FileDeleteTask implements Runnable {

	private File file;

	public FileDeleteTask(File file) {
		super();
		this.file = file;
	}

	@Override
	public void run() {
		while (file.exists()) {
			deleteFile(file);
			if (CommonConfig.useGcDeleteFile) {
				System.gc();
			}
		}

	}

	public void deleteFile(File filePath) {
		File[] listFiles = filePath.listFiles();
		for (File tmpFile : listFiles) {
			if (tmpFile.isDirectory()) {
				deleteFile(tmpFile);
			}
			tmpFile.delete();
		}
		filePath.delete();
	}

}
