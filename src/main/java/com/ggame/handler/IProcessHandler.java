package com.ggame.handler;

import com.ggame.network.ISession;
import com.ggame.network.Message;

public interface IProcessHandler {
	public  void serviceMessage(ISession conn,Message message);
	public  void onDisconnect(ISession conn);
}
