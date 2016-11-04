package com.xp.hotswap;

import java.io.File;

import org.junit.Test;

import com.xp.Hello;
import com.xp.hotswap.builder.ApplicationContext;
import com.xp.hotswap.builder.HotswapBuilder;
import com.xp.hotswap.manager.FileManager;
import com.xp.hotswap.manager.ObjectManager;

/**
 * Unit test for simple App.
 */
public class AppTest {
	@Test
	public void show() {
		new HotswapBuilder().setCheckFileTime(3000L).setPlugFilePath("D:/hhh").isGCToDelteFile(true).startHotswap();
		Hello hello = null;

		try {
			hello = (Hello) ApplicationContext.getNewBeans(Hello.class);
			hello.show();
			hello = null;
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void hh() {
		File f = new File("D:\\hhh\\hhhg\\config.properties");
		System.out.println(f.lastModified());
	}

}
