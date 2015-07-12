package com.gzucm.youyin.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类,用于启动、停用，管理线程池
 * @author 李先华
 */
public class ThreadPoolManager {
	
	/** 线程池 */
	private ExecutorService service;

	private ThreadPoolManager() {
		int num = Runtime.getRuntime().availableProcessors(); //获取java虚拟机可用的处理器个数
		service = Executors.newFixedThreadPool(num * 4); //创建一个可重用固定线程数的线程池
	}

	private static final ThreadPoolManager manager = new ThreadPoolManager();

	public static ThreadPoolManager getInstance() {
		return manager;
	}

	public void addTask(Runnable runnable) { //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
		service.execute(runnable); //将线程放入池中进行执行
	}
}
