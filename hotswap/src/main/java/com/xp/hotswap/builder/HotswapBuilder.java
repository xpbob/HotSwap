package com.xp.hotswap.builder;

import java.io.File;

import com.xp.hotswap.lock.CommonConfig;
import com.xp.hotswap.manager.FileManager;

public class HotswapBuilder {

	private String plugFile;
	private Long checkTime = 3000L;

	public HotswapBuilder setPlugFilePath(String plugFilePath) {
		this.plugFile = plugFilePath;
		return this;
	}

	public HotswapBuilder isGCToDelteFile(boolean flag) {
		CommonConfig.useGcDeleteFile = flag;
		return this;
	}

	public HotswapBuilder setCheckFileTime(Long checkTime) {
		this.checkTime = checkTime;
		return this;
	}

	public void startHotswap() {
		if (plugFile == null) {
			return;
		}
		File file = new File(plugFile);
		if (file.exists()) {
			new Thread(new FileManager(file, checkTime)).start();
		}
	}
}
