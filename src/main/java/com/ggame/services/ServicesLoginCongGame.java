package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.ggame.accountservice.AgentGame;
import com.ggame.accountservice.AgentJava;
import com.ggame.accountservice.IDText;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.LoginType;
import com.ggame.entity.ServerInfoBase;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;
import config.GameId;

public class ServicesLoginCongGame implements HttpHandler {
	@Override
	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		
		Headers headers = he.getResponseHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");

		StringBuilder str = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
		}

		String response = "";
		Gson callback = new Gson();
		LoginAccountResponse message = new LoginAccountResponse();

		try {
			Gson gson = new Gson();
			LoginAccountRequest data = gson.fromJson(str.toString(), LoginAccountRequest.class);

			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				int gameId = data.getGameidInt();
				String gameIdStr = data.getGameid();

				LinkConfigInfo config = GameManager.getInstance().getConfigInfo(gameId);
				if (config == null) {
					SetBaoTri(message, callback, he);
					return;
				}

				InetAddress inet = he.getRemoteAddress().getAddress();
				String ip = inet.getHostAddress();

//				AgentGame.logError("---ServicesLoginCongGame--- LOGIN--- " + ", userName = " + data.getUser()
//						+ ", JSON = " + data.toJson());
				if (config.getLoginIp() == T.TRUE) {
					if (!GameManager.getInstance().checkIpLogin(ip)) {
						SetBaoTri(message, callback, he);
						return;
					}
				}

				boolean isCheckUpdate = false;
				switch (gameId) {
				case GameId.THANH_CHIEN:
					if (data.getVersion() < Config.VERSION_GAME_THANH_CHIEN) {
						isCheckUpdate = true;
						message.setUpdateVersion(true);
						message.setLinkUpdate(
								"https://play.google.com/store/apps/details?id=com.kynguyenthanhchien.tamquoc&hl=vi-VN");

						message.setErrorCode(ErrorServices.SUCCESS);
						if (GameManager.getInstance().mapLinkConfig.containsKey(gameId)) {
							message.setMessage(GameManager.getInstance().mapLinkConfig.get(gameId).getThongBaoUpdate());
						} else {
							message.setMessage(
									"Chúa công vui lòng cập nhật phiên bản mới nhất để trải nghiệm những tính năng mới!");
						}
					}
					break;
				case GameId.BABYLON:
					if (data.getVersion() < Config.VERSION_GAME_BABYLON) {
						isCheckUpdate = true;
						message.setUpdateVersion(true);
						if (data.getPlatform() == 1) {
							message.setLinkUpdate(
									"https://play.google.com/store/apps/details?id=sgc.happyfarm&hl=en-VN");

							if (data.getProviderId() == 5) {
								// link babylon
								message.setLinkUpdate(
										"https://play.google.com/store/apps/details?id=com.vuontreobabylon.gamenongtrai2020&hl=vi&gl=US");
							}
						} else {
							message.setLinkUpdate("https://apps.apple.com/us/app/halloween-nong-trai/id6478906944");
						}

						message.setErrorCode(ErrorServices.SUCCESS);
						if (GameManager.getInstance().mapLinkConfig.containsKey(gameId)) {
//							message.setMessage(
//									GameManager.getInstance().mapLinkConfig.get(gameId).getThongBaoUpdate());
							message.setMessage(
									"Chủ vườn vui lòng cập nhật phiên bản mới nhất để trải nghiệm những tính năng mới");
						} else {
//							message.setMessage(
//									GameManager.getInstance().getText(data.language, IDText.THONG_BAO_UPDATE));
							message.setMessage(
									"Chủ vườn vui lòng cập nhật phiên bản mới nhất để trải nghiệm những tính năng mới");
						}
					}
					break;
				case GameId.CO_TUONG:
					if (data.getVersion() < Config.VERSION_GAME_CO_TUONG) {
						isCheckUpdate = true;
						message.setUpdateVersion(true);
						message.setLinkUpdate("https://play.google.com/store/apps/details?id=sgc.happyfarm&hl=en-VN");

						message.setErrorCode(ErrorServices.SUCCESS);
						if (GameManager.getInstance().mapLinkConfig.containsKey(gameId)) {
							message.setMessage(GameManager.getInstance().mapLinkConfig.get(gameId).getThongBaoUpdate());
						} else {
//							message.setMessage(
//									GameManager.getInstance().getText(data.language, IDText.THONG_BAO_UPDATE));
							message.setMessage(
									"Chủ vườn vui lòng cập nhật phiên bản mới nhất để trải nghiệm những tính năng mới");
						}
					}
					break;
				}

				if (!isCheckUpdate) {
					// không cần Update
					User user = null;
					if (data.getLoginType() == LoginType.NORMAL) {
						// Chơi bình thường
						user = PlayerManager.getInstance().getUsersByUserName(data.getUser());
					} else if (data.getLoginType() == LoginType.PLAYNOW) {
						// Chơi ngay
						if (PlayerManager.getInstance().isExitPlayNow(data.getImei())) {
							// đã có tài khoản chơi ngay
							user = PlayerManager.getInstance().getUsersByIMEI(data.getImei());
						} else {
							// chưa có tài khoản chơi ngay, tạo tài khoản ngẫu nhiên cho người chơi
							inet = he.getRemoteAddress().getAddress();
							user = new User();
							long userIdGen = PlayerManager.getInstance().getMaxUserID() + 1;
							user.setUserId(userIdGen);
							String userName = AgentJava.RandomString(Config.LENGTH_MAX_USERNAME);
							user.setUserName(userName);
							String password = AgentJava.RandomString(Config.LENGTH_MAX_PASSWORD);
							user.setPassWord(password);
							user.setPlatform(data.getPlatform());
							user.setIme(data.getImei());
							user.setGameId(gameIdStr);
							user.setType(LoginType.PLAYNOW);
							user.setProviderId(data.getProviderId());
							user.setKeyHash(userIdGen + AgentJava.generateKeyhash());
							user.setEmail("");
							user.setIp(data.getServerIp());
							PlayerManager.getInstance().CreateAccount(user);
						}
					}

					if (user == null) {
						message.setErrorCode(ErrorServices.FAIL);
						if (gameId != GameId.BABYLON) {
							message.setMessage("Tài khoản không tồn tại, vui lòng kiểm tra lại");
						} else {
//							message.setMessage(GameManager.getInstance().getText(data.language, IDText.LOGIN_ERROR_1));
							message.setMessage("Tài khoản không tồn tại, vui lòng kiểm tra lại");
						}
					} else {
						Boolean isLock = false;
						if (GameManager.getInstance().mapLockAccount.containsKey(user.getUserName())) {
							LockAccountRequest dataAccount = GameManager.getInstance().mapLockAccount
									.get(user.getUserName());

							if (dataAccount.getStatus() == T.ONE) {
								int gameId2 = Integer.parseInt(dataAccount.getGame_id());
								if (gameId == gameId2) {
									isLock = true;
								}
							}
						}

						if (!isLock) {
							// Tài khoản không khóa, chơi game bình thường
							if (data.getLoginType() == LoginType.NORMAL) {
								if (!PlayerManager.getInstance().checkInfoUsers(data.getUser(), data.getPassword())) {
									message.setErrorCode(ErrorServices.FAIL);
									if (gameId != GameId.BABYLON) {
										message.setMessage("Tài khoản hoặc mật khẩu không đúng xin vui lòng nhập lại");
									} else {
										message.setMessage(
												GameManager.getInstance().getText(data.language, IDText.LOGIN_ERROR_2));
									}
								} else {
									message.setErrorCode(ErrorServices.SUCCESS);
									if (gameId != GameId.BABYLON) {
										message.setMessage("Đăng nhập thành công");
									} else {
										message.setMessage("Đăng nhập thành công");
//										message.setMessage(
//												GameManager.getInstance().getText(data.language, IDText.LOGIN_SUCCESS));
									}

									message.setUserId(user.getUserId());
									message.setUserName(user.getUserName());
									message.setUpdateVersion(false);
									message.setLinkUpdate("");

									if (config.getLoginIpGame() == T.TRUE) {
										// Có check IP game
										if (!GameManager.getInstance().checkIpLogin(ip)) {
											// Không nằm trong danh sách IP Game
											Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
											mapServerInfo = GameManager.getInstance()
													.getMapServerInfoCheckIp(gameIdStr);
											Collection<ServerInfoBase> values = mapServerInfo.values();
											List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
											message.setServerInfo(lst);

											Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
											mapServerPlayerData = GameManager.getInstance()
													.getMapServerPlayer2CheckIP(gameIdStr, user.getUserName());
											Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
											List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
											message.setServerPlayerData(lst2);
										} else {
											// Nằm trong danh sách IP game
											Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
											mapServerInfo = GameManager.getInstance().getMapServerInfo(gameIdStr);
											Collection<ServerInfoBase> values = mapServerInfo.values();
											List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
											message.setServerInfo(lst);

											Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
											mapServerPlayerData = GameManager.getInstance()
													.getMapServerPlayer2(gameIdStr, user.getUserName());
											Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
											List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
											message.setServerPlayerData(lst2);
										}

									} else {
										// Không check IP vào Bình thường
										Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
										mapServerInfo = GameManager.getInstance().getMapServerInfo(gameIdStr);
										Collection<ServerInfoBase> values = mapServerInfo.values();
										List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
										message.setServerInfo(lst);

										Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
										mapServerPlayerData = GameManager.getInstance().getMapServerPlayer2(gameIdStr,
												data.getUser());
										Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
										List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
										message.setServerPlayerData(lst2);
									}
								}
							} else if (data.getLoginType() == LoginType.PLAYNOW) {
								// Đăng nhập bằng chơi ngay
								message.setErrorCode(ErrorServices.SUCCESS);
								if (gameId != GameId.BABYLON) {
									message.setMessage("Đăng nhập thành công");
								} else {
//									message.setMessage(
//											GameManager.getInstance().getText(data.language, IDText.LOGIN_SUCCESS));
									message.setMessage("Đăng nhập thành công");
								}

								message.setUserId(user.getUserId());
								message.setUserName(user.getUserName());
								message.setUpdateVersion(false);
								message.setLinkUpdate("");

								if (config.getLoginIpGame() == T.TRUE) {
									// Có check IP game
									if (!GameManager.getInstance().checkIpLogin(ip)) {
										// Không nằm trong danh sách IP Game
										Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
										mapServerInfo = GameManager.getInstance().getMapServerInfoCheckIp(gameIdStr);
										Collection<ServerInfoBase> values = mapServerInfo.values();
										List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
										message.setServerInfo(lst);

										Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
										mapServerPlayerData = GameManager.getInstance()
												.getMapServerPlayer2CheckIP(gameIdStr, user.getUserName());
										Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
										List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
										message.setServerPlayerData(lst2);
									} else {
										// Nằm trong danh sách IP game
										Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
										mapServerInfo = GameManager.getInstance().getMapServerInfo(gameIdStr);
										Collection<ServerInfoBase> values = mapServerInfo.values();
										List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
										message.setServerInfo(lst);

										Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
										mapServerPlayerData = GameManager.getInstance().getMapServerPlayer2(gameIdStr,
												user.getUserName());
										Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
										List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
										message.setServerPlayerData(lst2);
									}
								} else {
									// Không check IP vào Bình thường
									Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
									mapServerInfo = GameManager.getInstance().getMapServerInfo(gameIdStr);
									Collection<ServerInfoBase> values = mapServerInfo.values();
									List<ServerInfoBase> lst = new ArrayList<ServerInfoBase>(values);
									message.setServerInfo(lst);

									Map<Long, ServerPlayerData> mapServerPlayerData = new HashMap<>();
									mapServerPlayerData = GameManager.getInstance().getMapServerPlayer2(gameIdStr,
											user.getUserName());
									Collection<ServerPlayerData> values2 = mapServerPlayerData.values();
									List<ServerPlayerData> lst2 = new ArrayList<ServerPlayerData>(values2);
									message.setServerPlayerData(lst2);
								}
							}
						} else {
							message.setErrorCode(ErrorServices.FAIL);
							if (gameId != GameId.BABYLON) {
								message.setMessage(
										"Tài khoản đang bị khóa, vui lòng liên hệ với Admin để được hỗ trợ.");
							} else {
								message.setMessage(
										"Tài khoản đang bị khóa, vui lòng liên hệ với Admin để được hỗ trợ.");
							}
						}

						if (user != null) {
							if (user.getIp() != null) {
								if (user.getIp().length() < 3) {
									DatabaseAccount.updateIpUser(user.getUserName(), line, gameIdStr);
								}
							}
						}
						
						try {
							// Truyền qua cổng Web
							SessionLogRequest request = new SessionLogRequest();
							request.username = data.getUser();
							request.time_login = getDateTimeCurrent();
							request.type_device = data.getPlatform() - 1;
							request.type_login = 1;
							Gson gsonResponse = new Gson();
							StringEntity postingString = new StringEntity(gsonResponse.toJson(request), "UTF-8");
							Http.sendPostJson(Config.urlPostSessionLog, postingString);
						} catch (Exception e) {
							DatabaseAccount.logToFile(e);
						}
					}
				}
			}

			response = callback.toJson(message);
		} catch (Exception e) {
			AgentGame.logError("1- NULL ---ServicesLoginCongGame--- LOGIN--- ");
			message.setErrorCode(ErrorServices.FAIL);
			message.setMessage("NULL");
			response = callback.toJson(message);
			e.printStackTrace();
			LogsManager.logToFile(e);
		}

		OutputStream os = he.getResponseBody();
		byte[] bs = response.getBytes("UTF-8");
		he.sendResponseHeaders(200, 0);
		try (BufferedOutputStream out = new BufferedOutputStream(he.getResponseBody())) {
			try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
				byte[] buffer = new byte[2];
				int count;
				while ((count = bis.read(buffer)) != -1) {
					os.write(buffer, 0, count);
				}
				os.close();
			}
		}
	}

	public static String getDateTimeCurrent() {
		String timeCurrent = "";
		try {
			timeCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			return timeCurrent;
		} catch (Exception e) {
			e.printStackTrace();
			return timeCurrent;
		}
	}

	private void SetBaoTri(LoginAccountResponse message, Gson callback, HttpExchange he) {
		try {
//			String content = "🔥🔥BQT Kỷ Nguyên Thành Chiến xin thông báo sẽ tiến hành bảo trì server để chuẩn bị cập nhật phiên bản mới với các tính năng hấp dẫn, nâng cấp hệ thống, mang đến trải nghiệm mượt mà hơn cho quý Thành Chủ.\r\n"
//			+ "⏰Thời gian bảo trì: từ 11h00p ngày 15/07 đến hết 23h59p ngày 19/07/2023 \r\n"
//			+ "✍️Trong thời gian bảo trì các Thành Chủ sẽ không thể đăng nhập và thực hiện các hoat động trong game.\r\n"
//			+ "💥BQT Kỷ Nguyên Thành Chiến, trân trọng kính báo!";

			String response = "";
			String content = "🔊🔊 THÔNG BÁO🔊🔊\r\n"
					+ "🎉BQT game chiến thuật Kỷ Nguyên Thành Chiến xin thông báo chính thức mở lại SEVER 1 - BIG UPDATE TÍNH NĂNG MỚI siêu hấp dẫn 😍\r\n"
					+ "--------------🌟🌟🌟------------------\r\n" + "⏰Thời gian: 10h00p ngày 26/07/2023";
			message.setErrorCode(ErrorServices.FAIL);
			message.setMessage(content);
			response = callback.toJson(message);
			OutputStream os = he.getResponseBody();
			byte[] bs = response.getBytes("UTF-8");
			he.sendResponseHeaders(200, 0);
			try (BufferedOutputStream out = new BufferedOutputStream(he.getResponseBody())) {
				try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
					byte[] buffer = new byte[2];
					int count;
					while ((count = bis.read(buffer)) != -1) {
						os.write(buffer, 0, count);
					}
					os.close();
				}
			}
		} catch (Exception e) {
			LogsManager.logToFile(e);
		}
	}
}
