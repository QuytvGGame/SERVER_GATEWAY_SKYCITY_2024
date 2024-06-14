package com.ggame.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ggame.accountservice.AgentGame;
import com.ggame.accountservice.T;
import com.ggame.entity.player.User;
import com.ggame.handler.IProcessHandler;
import com.sgc.game.network.websocket.SessionWS;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.NetworkServer;
import com.sgc.game.server.SessionManager;



public class Session implements ISession {
	/**Sử dụng sessionWS trong trường hợp WebSocket **/
	com.sgc.game.network.websocket.SessionWS sessionWS; 
	private boolean isSessionWS=false;
	
	private boolean isHead;
	private int size;
	private ByteBuffer buffer = null;
	private static final int byteData = 4;
	private static final int ZERO = 0;
	private int statusDisconnect = -1;

	private int version;
	private long sessionId;
	private boolean connected;
	private SocketChannel socketChannel;
	private IProcessHandler commonHandler;
	private User player;
	private int providerId = -1;
	private int providerClient = 1;
	private static Log logger = LogFactory.getLog(Session.class);

	public boolean isSessionWS() {
		return isSessionWS;
	}

	public void setIsSessionWS(boolean isWS) {
		this.isSessionWS = isWS;
	}

	public SessionWS getSessionWS() {
		return sessionWS;
	}

	public void setSessionWS(SessionWS sessionWS) {
		this.sessionWS = sessionWS;
	}
	public int getProviderClient() {
		return providerClient;
	}

	public void setProviderClient(int providerClient) {
		this.providerClient = providerClient;
	}

	public int getProviderId() {
		return providerId;
	}

	public String getIP() {
		if (socketChannel != null) {
			String ip = "";
			try {
				ip = socketChannel.socket().getRemoteSocketAddress().toString();
				ip = ip.substring(0, ip.indexOf(":"));
			} catch (Exception e) {
				e.printStackTrace();
			}

			return ip;
		}
		return T.Empty;
	}

	public int getPORT() {
		if (socketChannel != null) {
			int port = 0;
			String address = socketChannel.socket().getRemoteSocketAddress().toString();
			port = Integer.parseInt(address.substring(address.indexOf(":"), address.length()));
			return port;
		}
		return T.ZERO;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}
	/**Dung để xử lý cho phần kết nối bằng WS**/
	public Session(SessionWS sessionWS, IProcessHandler commonHandler) {
		this.sessionWS=sessionWS;
		this.commonHandler = commonHandler;
		this.isSessionWS=true;
		sessionWS.setSessionSocket(this);
	}
	public Session(SocketChannel channel, IProcessHandler commonHandler) {
		try {
			 System.out.println("==============>Session->channel.getRemoteAddress()="+channel.getRemoteAddress());
			connected = true;
			setSocket(channel);
			this.commonHandler = commonHandler;
			this.isHead = true;

		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			logger.error("Session initialization erorr", ioEx);
		}
	}

	public void close() {
		try {
			connected = false;
			if (socketChannel != null) {
				socketChannel.close();
			}
		} catch (Exception e) {
		}
	}

	/** Send back message to client
	 * update: 2021/09/30 */

	public   void sendMessage(Message message) {
		try {
			String content = message.toString();
			byte[] arrData = content.getBytes();
			byte[] arrMsg = new byte[byteData + arrData.length];
			byte[] size = int2Bytes(arrData.length);
			System.arraycopy(size, 0, arrMsg, 0, byteData);
			System.arraycopy(arrData, 0, arrMsg, byteData, arrData.length);
			ByteBuffer buffer = ByteBuffer.wrap(arrMsg);
			
			if(!this.isSessionWS()) {
				if (!isConnected()) {
					return;
				}
				
				socketChannel.write(buffer);
			}
			else
			{
				System.out.println("=========WS sendMessage=>"+message.toString());
				javax.websocket.Session wss=this.getSessionWS().getSessionWebSocket();
				synchronized(wss) {
					wss.getBasicRemote().sendBinary(buffer);
					//wss.getAsyncRemote().sendBinary(buffer);
					//Thread.sleep(300);//TEST
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSocket(SocketChannel channel) throws IOException {
		this.socketChannel = channel;
		socketChannel.socket().setKeepAlive(true);
		socketChannel.socket().setSoTimeout(180 * 1000);
		socketChannel.socket().setSoLinger(true, 2);
		socketChannel.socket().setTcpNoDelay(true);
		socketChannel.socket().setSendBufferSize(1024 * 1024);
		socketChannel.socket().setReceiveBufferSize(2* 1024 * 1024);
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public void setUsers(User player) {
		this.player = player;
	}

	public User getUser() {
		return player;
	}

	public boolean isConnected() {
		return connected;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public synchronized void shutDown() {
		try {
			if (!out) {
				SessionManager.getInstance().remove(socketChannel);
				if (connected) {
					close();
				}
				commonHandler.onDisconnect(this);
			}
		} catch (Exception ex) {
		}
	}

	private boolean out;

	public synchronized void shutDownNow() {
		out = true;
		SessionManager.getInstance().remove(socketChannel);
		/**Thêm bỏ WS session*/
		if(sessionWS!=null)
		{
			sessionWS.onClose(sessionWS.getSessionWebSocket());
		}
		close();
	}

	@Override
	public void sendMessage(MessageOld message) {
	}

	
	@Override
	public void readMessage(SelectionKey key) {
		if (isHead) {
			ByteBuffer bufferSize = ByteBuffer.allocate(byteData);
			int numRead = statusDisconnect;
			try {
				numRead = socketChannel.read(bufferSize);
				
				if (numRead == statusDisconnect) {
					SessionManager.getInstance().closeSession(key);
					return;
				}
				if (numRead == byteData) {
					size = bytes2Int(bufferSize.array());
					isHead = false;
					buffer = ByteBuffer.allocate(size);
				} else {
					return;	
				}
			} catch (Exception e) {
				SessionManager.getInstance().closeSession(key);
				return;
			}
		}
		if (!isHead) {
			int numRead = statusDisconnect;
			try {
				numRead = socketChannel.read(buffer);
				if (numRead < size)
					return;
				if (numRead == statusDisconnect) {
					SessionManager.getInstance().closeSession(key);
					return;
				}

				byte[] data = new byte[size];

				System.arraycopy(buffer.array(), ZERO, data, ZERO, size);
				String strMessage = new String(data);
				Message message = new Message(strMessage);
//				if (message.getCMD() != CMDServer.PING_PONG)
//					AgentGame.log("Got: " + strMessage);
				this.commonHandler.serviceMessage(this, message);
				buffer.clear();
				size = ZERO;
				isHead = true;
			} catch (Exception e) {
				e.printStackTrace();
				SessionManager.getInstance().closeSession(key);
				return;
			}
		}

	}

	private static byte[] int2Bytes(int number) {
		byte[] res = new byte[byteData];
		for (int i = 3; i >= ZERO; i--) {
			res[i] = (byte) number;
			number >>= 8;
		}
		return res;
	}

	private static int bytes2Int(byte[] data) {
		int res = 0;
		for (int i = ZERO; i < byteData; i++) {
			res <<= 8;
			res |= (0xFF & data[i]);
		}
		return res;
	}
}
