package com.ggame.handler;

import com.ggame.accountservice.AgentJava;
import com.ggame.accountservice.CMDServer;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.ggame.network.ISession;
import com.ggame.network.Message;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.NetworkServer;
import com.sgc.game.server.PlayerManager;

public class CommonHandler implements IProcessHandler {
	BaseHandler baseHandler;

	public CommonHandler() {
		baseHandler = new BaseHandler();
	}

	@Override
	public void serviceMessage(ISession session, Message message) {
		try {
			int cmd = message.getCMD();
			
			
			System.out.println("========== CommonHandler ======= serviceMessage, cmd = " + cmd);
			
			// Phan luong cmd de xu ly nhanh hon
			switch (cmd) {
			case CMDServer.CONG_INSTALL_GAME:
				baseHandler.onInstallGame(session, message);
				break;
			case CMDServer.CONG_DISCONNECT:
				baseHandler.onDisconnectServer(session);
				break;
			case CMDServer.CONG_REGISTER:
				baseHandler.onRegister(session, message);
				break;
			case CMDServer.CONG_LOGIN_NORMAL:
				baseHandler.onLoginNormal(session, message);
				break;		
			case CMDServer.CONG_SERVER_INFO:
				baseHandler.onServerInfo(session, message);
				break;
			case CMDServer.CONG_UPDATE_PROFILE:
				baseHandler.onUpdateProfile(session, message);
				break;
			case CMDServer.CONG_CHANGE_PASSWORD:
				baseHandler.onChangePassword(session, message);
				break;
				
			case CMDServer.CONG_VALIDATE_TOKEN_TC:
				baseHandler.onCongValidateTokenTC(session, message);
				break;
			case CMDServer.CONG_INSTALL_GAME_TC:
				baseHandler.onCongInstallGameTC(session, message);
				break;
			case CMDServer.CONG_REGISTER_TC:
				baseHandler.onCongRegisterTC(session, message);
				break;
			case CMDServer.CONG_LOGIN_NORMAL_TC:
				baseHandler.onCongLoginNormalTC(session, message);
				break;
				
				
				
			default:
				AgentJava.logError("chua xu ly " + cmd);
			}
		} catch (

		Throwable e) {
			LogsManager.logToFile(e);
		}
	}

	@Override
	public void onDisconnect(ISession conn) {
		User player = conn.getUser();
		if (player != null) {
			try {
				User client = (User) player.clone();
				client.disconnect();
				client.logOut();
				PlayerManager.getInstance().putUserOutCached(client);
				PlayerManager.getInstance().putUserCached(client, false);
			} catch (Exception e) {
				DatabaseAccount.logToFile("Player Logout", NetworkServer.PATH_EXCEPTION);
			} finally {
				PlayerManager.getInstance().removeUsers(conn.getUser().getUserName());
			}
		}
	}

}
