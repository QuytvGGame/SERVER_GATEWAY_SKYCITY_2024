package com.sgc.game.server;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class RejectedExecution implements RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor threadPool) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(500);
			threadPool.remove(r);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
