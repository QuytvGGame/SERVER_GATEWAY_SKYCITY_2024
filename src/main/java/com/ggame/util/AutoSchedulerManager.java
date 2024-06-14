package com.ggame.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class AutoSchedulerManager {

	private static AutoSchedulerManager instance = new AutoSchedulerManager();
	public ThreadPoolExecutor threadPool;

	public static AutoSchedulerManager getInstance() {
		return instance;
	}

	private List<AutoScheduler> autoSchedulerList = new ArrayList<>();

	public void putAutoScheduler(AutoScheduler auto) {
		autoSchedulerList.add(auto);
	}

	public List<AutoScheduler> getAutoSchedulerList() {
		return autoSchedulerList;
	}

	public void setAutoSchedulerList(List<AutoScheduler> autoSchedulerList) {
		this.autoSchedulerList = autoSchedulerList;
	}

	public synchronized void remove(AutoScheduler a) {
		autoSchedulerList.remove(a);
	}

}
