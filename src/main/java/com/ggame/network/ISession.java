package com.ggame.network;

import java.nio.channels.SelectionKey;

import com.ggame.entity.player.User;
import com.sgc.game.network.websocket.SessionWS;

public interface ISession {

	public SessionWS getSessionWS();
	
	public void close();

	public User getUser();

	public void sendMessage(MessageOld message);

	public void sendMessage(Message message);

	public int getProviderId();

	public void setVersion(int version);

	public boolean isConnected();

	public void setProviderId(int providerid);

	public void setUsers(User player);

	public void setSessionId(long sessionId);

	public void shutDown();

	public void shutDownNow();

	public String getIP();

	public int getPORT();

	public int getProviderClient();

	public int getVersion();

	public long getSessionId();

	public void setProviderClient(int providerID);

	public void readMessage(SelectionKey key);
}
