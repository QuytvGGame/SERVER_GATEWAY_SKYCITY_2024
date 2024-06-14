package com.sgc.game.network.websocket;


import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ggame.entity.player.User;
import com.ggame.handler.CommonHandler;
import com.ggame.handler.IProcessHandler;
import com.ggame.network.Message;
import com.sgc.game.server.NetworkServer;
import com.sgc.game.server.PlayerManager;
import com.sgc.game.server.SessionManager;

import javax.websocket.Session;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by The Geeky Asian on 1/2/2019.
 */
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class SessionWS extends BinaryWebSocketHandler implements SubProtocolCapable{
	private boolean isHead;
	private int size;
	private ByteBuffer buffer = null;
	private static final int byteData = 4;
	private static final int ZERO = 0;
	private int statusDisconnect = -1;
	
	
	private long sessionId;
	private boolean connected;
	private Session sessionWebSocket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private IProcessHandler commonHandler;
	private com.ggame.network.Session sessionSocket;
    
	public static Set<SessionWS> listeners = new CopyOnWriteArraySet<>();
    private byte[] key = null;
	private byte curR, curW;
	public SessionWS() {
		
	}
//	public SessionWS(Session sessionSocket, InterfaceProcess commonHandler) {
//		try {
//			connected = true;
//			//setSocket(socket);
//			this.sessionSocket=sessionSocket;
//			this.commonHandler = commonHandler;
//			//threadPool.execute(this);
//		} catch (Exception ioEx) {
//			ioEx.printStackTrace();
//			//logger.error("Session initialization erorr", ioEx);
//		}
//	}
	public com.ggame.network.Session getSessionSocket() {
		return sessionSocket;
	}
	public void setSessionSocket(com.ggame.network.Session sessionSocket) {
		this.sessionSocket = sessionSocket;
	}
	public Session getSessionWebSocket() {
		return sessionWebSocket;
	}
	public void setSessionWebSocket(Session sessionSocket) {
		this.sessionWebSocket = sessionSocket;
	}
    @OnOpen
    public void onOpen(Session session) throws InterruptedException {
        this.sessionWebSocket = session;
        listeners.add(this);
        System.out.println("===========1====onOpen========session ID="+session.getId());
//        log.info(String.format("==========New session connected! Connected listeners: %s", listeners.size()));
        this.isHead = true;
    }

    @OnMessage //Allows the client to send message to the socket.
    public void onMessage(String msg,Session session) {
      
//    	Message message;
//		if (connected) {
//			message = readMessage();
//			commonHandler.serviceMessage(this, message);
//		}
    	
    	// broadcast(message);
        System.out.println("===========2============msg="+msg);
    }
    @OnMessage
    public void handleBinaryMessage(InputStream input, Session session) {
        try {
	        System.out.println("===========7.1===handleBinaryMessage========= session id="+session.getId()+ " IP="+session.getUserProperties().get("javax.websocket.endpoint.remoteAddress"));
	        DataInputStream ips=new DataInputStream(input);
	        Message message=readMessage(ips);
	        int cmd=message.getCmd();
	        System.out.println("===========7.2=  handleBinaryMessage==> CMD="+cmd+"session id="+session.getId()+ " IP="+session.getUserProperties().get("javax.websocket.endpoint.remoteAddress"));
	        this.sessionWebSocket=session;
	       // World world = NetworkServer.getInstance().getWorld();
	//        if(commonHandler!=null)
	        commonHandler = (CommonHandler) NetworkServer.getInstance().getComonhandler();
	        if(commonHandler==null) {
	        	///System.out.println("==========commonHandler===NULLL");
	        	commonHandler = new CommonHandler();
	        }
	        User player=PlayerManager.getInstance().getPlayerByWSSession(session.getId());
	        if(player==null) {
		        sessionSocket=new com.ggame.network.Session(this, commonHandler);
//		        sessionSocket.setListener(new SessionListener() {
//	
//					@Override
//					public void remove(com.sgroup.babylon.network.Session conn) {
//						NetworkServer.getInstance().getSessionManager().remove(conn);
//						listeners.remove(sessionWebSocket);
//					}
//				});
		        SessionManager.getInstance().addSession(null,sessionSocket);
		        commonHandler.serviceMessage(sessionSocket, message);
	        }
	        else
	        	commonHandler.serviceMessage(player.getSession(), message);
        
        	
     } catch (Exception e) {
       e.printStackTrace();
     }
    }
    @OnClose
    public void onClose(Session session) {
    	  try {
    		  listeners.remove(this);
//    	        log.info(String.format("Session disconnected. Total connected listeners: %s", listeners.size()));
    	        System.out.println("============WEB SESSION CLOSED===========ID="+session.getId());
    	        
    	        commonHandler.onDisconnect(sessionSocket);
    	        
    	        if(sessionSocket!=null)
    	        	sessionSocket.close();
    	        
    	       // PlayerManager.getInstance().removePlayer(conn.getPlayer().getUserName());
    	        this.sessionWebSocket.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    	
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        //Error
    	System.out.println("===========4==ERR==========");
    	//throwable.printStackTrace();
    	
    }
    private byte readKey(byte b) {
		byte i = (byte) ((key[curR++] & 0xff) ^ (b & 0xff));
		if (curR >= key.length)
			curR %= key.length;
		return i;
	}

	private byte writeKey(byte b) {
		byte i = (byte) ((key[curW++] & 0xff) ^ (b & 0xff));
		if (curW >= key.length)
			curW %= key.length;
		return i;
	}

	public Message readMessage(DataInputStream dis) {
    	try {
    		byte[] data;
    		if (isHead) {
    			ByteBuffer bufferSize = ByteBuffer.allocate(byteData);
    			int numRead = statusDisconnect;
    			try {
    				byte[] lengthControl = new byte[byteData];
    				numRead=dis.read(lengthControl, 0,byteData);
    				
    				if (numRead == statusDisconnect) {
    					//SessionManager.getInstance().closeSession(key);
    					return null;
    				}
    				if (numRead == byteData) {
    					size = bytes2Int(lengthControl);
    					isHead = false;
    					buffer = ByteBuffer.allocate(size);
    				} else {
    					return null;	
    				}
    				isHead = false;
    			} catch (Exception e) {
    				//SessionManager.getInstance().closeSession(key);
    				e.printStackTrace();
    				return null;
    			}
    		}
    		if (!isHead) {
    			int numRead = statusDisconnect;
    			try {
    				//numRead = dis.read();
    				//if (numRead < size)
    				//	return null;
    				//if (numRead == statusDisconnect) {
    				//	//SessionManager.getInstance().closeSession(key);
    				//	return null;
    				//}
    				data = new byte[size];
    				dis.read(data, 0,size);
    				String strMessage = new String(data);
    				Message message = new Message(strMessage);

    				//this.commonHandler.serviceMessage(this.sessionSocket, message);
    				buffer.clear();
    				size = ZERO;
    				isHead = true;
    				return message;
    			} catch (Exception e) {
    				e.printStackTrace();
    				//SessionManager.getInstance().closeSession(key);
    				return null;
    			}
    		}
    		
    		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;	
	}
    public Message readMessage_(DataInputStream dis) {
    	try {
    			try {
    				byte data[];
    				size = dis.readUnsignedShort();
    				data = new byte[size];
    				dis.readFully(data);
    				System.arraycopy(buffer.array(), ZERO, data, ZERO, size);
    				String strMessage = new String(data);
    				Message message = new Message(strMessage);
    				this.commonHandler.serviceMessage(this.sessionSocket, message);
    				buffer.clear();
    				size = ZERO;
    				isHead = true;
    				return message;
    			} catch (Exception e) {
    				e.printStackTrace();
    				//SessionManager.getInstance().closeSession(key);
    				return null;
    			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;	
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
   
    public static void broadcast(String message) {
        for (SessionWS listener : listeners) {
            listener.sendMessage("FROM SV:"+message);
        }
        System.out.println("===========5============");
    }

    private void sendMessage(String message) {
        try {
            this.sessionWebSocket.getBasicRemote().sendText(message);
            System.out.println("===========6============");
        } catch (IOException e) {
//            log.error("Caught exception while sending message to Session Id: " + this.sessionWebSocket.getId(), e.getMessage(), e);
        }
    }
    
	public String decodeMessage(InputStream in) {
    	System.out.println("===========decodeMessage============");
        try { 
            in.skip(2);
            ArrayList<Byte> keys = new ArrayList<Byte>();
            for(int i = 0; i < 4; i++){
                int r = in.read();
              
                keys.add((byte) r);
            }

            ArrayList<Byte> encoded = new ArrayList<Byte>();
            for(int i = 0; i < in.available(); i++){
                int r = in.read();
               
                encoded.add((byte) r);
            }

            ArrayList<Byte> decoded = new ArrayList<Byte>();
            for(int i = 0; i < encoded.size(); i++){
                decoded.add((byte) (encoded.get(i) ^ keys.get(i & 0x3)));
               
            }

           
            String s = new String(toByteArray(decoded), "US-ASCII");
           

        } catch(IOException e) {
        	e.printStackTrace();

        }

        return null;
    }       

    public static byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }
	@Override
	public List<String> getSubProtocols() {
		// TODO Auto-generated method stub
		return null;
	}
	
}