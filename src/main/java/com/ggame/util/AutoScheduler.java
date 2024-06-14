package com.ggame.util;

public abstract class AutoScheduler {
	public long timeStart;
	public long timeout;
	public long userId;

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public AutoScheduler(long userId, long timeout) {
		this.timeout = timeout;
		this.userId = userId;
	}

	public boolean start = false;

	public void start() {
		// delete all timer of layer
		timeStart = System.currentTimeMillis();
		AutoSchedulerManager.getInstance().putAutoScheduler(this);
		start = true;
	}

	public void stop() {
		start = false;
	}

	public void removeAuto(String s) {
		start = false;
		AutoSchedulerManager.getInstance().remove(this);
	}

	public abstract void process();
	public abstract void timeDown();
	public long getUserId() {
		return userId;
	}

}
