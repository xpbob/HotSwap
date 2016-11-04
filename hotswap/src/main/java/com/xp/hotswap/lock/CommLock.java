package com.xp.hotswap.lock;

public class CommLock {
	public static final Object INIT_LOCK = new Object();
	public static final Object BEAN_LOCK = new Object();
	public static volatile boolean initFlag = false;
}
