package com.sgc.game.server;

import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ggame.network.ISession;

public class SessionManager {
	public static Map<SocketChannel, ISession> mapSessions = new HashMap<>();
	private static SessionManager instance = new SessionManager();

	public static SessionManager getInstance() {
		return instance;
	}

	public int getSessionTotal() {
		return mapSessions.size();
	}

	@Override
	public Object clone() {
		return ((Vector<ISession>) mapSessions.values()).clone();
	}

	public void addSession(SocketChannel channel, ISession session) {
		mapSessions.put(channel, session);
	}

	public void remove(SocketChannel channel) {
		mapSessions.remove(channel);
	}

	public void closeSession(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			Socket socket = channel.socket();
			SocketAddress remoteAddr = socket.getRemoteSocketAddress();
			System.out.println("Connection closed by client: " + remoteAddr);
			mapSessions.get(channel).shutDown();
			channel.close();
			key.cancel();
			mapSessions.remove(channel);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
