package com.sgc.game.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.galaxy.framework.server.util.AppServer;
import com.ggame.accountservice.T;
import com.ggame.database.AdminCommand;
import com.ggame.database.DatabaseAccount;
import com.ggame.database.pool.DBConfiguration;
import com.ggame.entity.player.User;
import com.ggame.handler.CommonHandler;
import com.ggame.logs.CCULog;
import com.ggame.network.Session;
import com.ggame.services.ServiceGGame;
import com.ggame.util.AutoScheduler;
import com.ggame.util.AutoSchedulerManager;

import config.Config;

public class NetworkServer implements Runnable {
	private static final Log log = LogFactory.getLog("");
	public static final String DB_CONGGAME = "DB_CONGGAME";
	public static boolean MAINTERNANT = false;

	public static int GAME_ID = 1;
	public static String PATH_EXCEPTION;
	public final static String spcSyntax = "|";
	public static boolean shutdown = false;
	public static String configfileDir;
	// private boolean shutdowning = false;
	public static SimpleDateFormat timeFormatter;
	public static String eventDateFomat = "yyyyMMdd";
	private long timeshutdown = 0;
	private int NUMTHPOOL = 200;
	private ThreadPoolExecutor threadPool = null;
	private CommonHandler comonhandler;

	public CommonHandler getComonhandler() {
		return comonhandler;
	}

	public void setComonhandler(CommonHandler comonhandler) {
		this.comonhandler = comonhandler;
	}

	public static boolean testCCu = false;
	public static boolean inEvent = false;

	public static boolean testPvp = true;

	private Selector selector;
	private InetSocketAddress listenAddress;
	private String IP;
	private int LISTEN_PORT;
	private static NetworkServer instance;

	public static void setInstance(NetworkServer instance) {
		NetworkServer.instance = instance;
	}

	public static NetworkServer getInstance() {
		if (instance == null) {
			instance = new NetworkServer();
		}
		return instance;
	}

	static {
		try {
		} catch (Exception e) {
			LogsManager.logToFile(e);
		}
	}
//	private void reload() throws IOException {
//		log.info("-----------------RELOAD ALL CONFIG------------------");
//		try {
//			Config.loadProperties(configfileDir + "/conf.cfg");
//			log.info("-----------------RELOAD SUCCESSFULL------------------");
//
//		} catch (Exception e) {
//			logToFile(e);
//		}
//		System.gc();
//	}

	private void loadData() throws IOException {
		comonhandler = new CommonHandler();
		timeFormatter = new SimpleDateFormat("HH:mm");
		// creating the ThreadPoolExecutor
		threadPool = new ThreadPoolExecutor(NUMTHPOOL, // core thread pool size
				NUMTHPOOL, // maximum thread pool size
				5L, // time to wait before resizing pool
				TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(), new RejectedExecution());
		threadPool.allowCoreThreadTimeOut(true);

		String DefaultDatabase = AppServer.getParam("DefaultDatabase");
		String rootDB = "Connection." + DefaultDatabase;
		DBConfiguration.DB_MAIN_DRIVER = AppServer.getParam(rootDB + ".DriverClass");
		DBConfiguration.DB_MAIN_CONNECTION_URL = AppServer.getParam(rootDB + ".Url");
		DBConfiguration.DB_MAIN_USER_NAME = AppServer.getParam(rootDB + ".UserName");
		DBConfiguration.DB_MAIN_PASSWORD = AppServer.getParam(rootDB + ".Password");

		DBConfiguration.DB_LOG_DRIVER = AppServer.getParam("Connection.DB_LOGS.DriverClass");
		DBConfiguration.DB_LOG_CONNECTION_URL = AppServer.getParam("Connection.DB_LOGS.Url");
		DBConfiguration.DB_LOG_USER_NAME = AppServer.getParam("Connection.DB_LOGS.UserName");
		DBConfiguration.DB_LOG_PASSWORD = AppServer.getParam("Connection.DB_LOGS.Password");

		AutoSchedulerManager.getInstance().threadPool = threadPool;
		GameManager.load();
		PlayerManager.getInstance().load();
		IP = AppServer.getParam("Server.IP");
		LISTEN_PORT = Integer.parseInt(AppServer.getParam("Server.Port"));
		PATH_EXCEPTION = AppServer.getParam("Server.Exception");
		Config.loadProperties("conf.cfg");
		System.gc();
	}

	private void runAutoSchedulerThread() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				log.info("Running AutoSchedulerThread !" + "    " + Thread.currentThread().getName());
				long timeDelayCheck = 1000L;
				while (!shutdown) {
					List<AutoScheduler> list = new ArrayList<>(
							AutoSchedulerManager.getInstance().getAutoSchedulerList());
					for (AutoScheduler auto : list) {
						auto.timeDown();
						if (!auto.start)
							auto.removeAuto("!auto.start");
						else if ((System.currentTimeMillis() - auto.timeStart) > auto.timeout) {
							try {
								auto.stop();
								auto.removeAuto("timeout");
								auto.process();
							} catch (Exception ex) {
								ex.printStackTrace();
								continue;
							} finally {

							}
						}
					}
					try {
						Thread.sleep(timeDelayCheck);
					} catch (InterruptedException e) {
						logToFile(e);
//						runAutoSchedulerThread();
					}
				}
			}
		});
	}

	private void RunAdminMonitorThread() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
//				log.info("Running AdminMonitorThread !" + "    " + Thread.currentThread().getName());
				Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
				Vector<AdminCommand> vtAdminCommand = new Vector<AdminCommand>();
				int countGC = 0;
				while (!shutdown) {
					try {
						DatabaseAccount.LoadAdminCommand(vtAdminCommand);
						if (vtAdminCommand.size() > T.ZERO) {
							for (AdminCommand adminCommand : vtAdminCommand) {
								if (adminCommand.status.equals("s")) {
									switch (adminCommand.command) {
									case "shutdown":
										break;
									case "alert":
										break;
									case "reload":
										GameManager.reLoad();
										break;
									case "reload_server_info":
										DatabaseAccount.loadServerInfo();
										break;
									case "reload_country_info":
										DatabaseAccount.loadCountryInfo();
										break;
									case "reload_server_data":
										DatabaseAccount.loadServerPlayerData();
										break;
									case "reload_links_config":
										DatabaseAccount.loadLinksConfig();
										break;
									case "reload_giftcode":
										DatabaseAccount.loadGiftCode();
										break;
									case "reload_giftcode_data":
										DatabaseAccount.loadGiftCodeData();
										break;
									case "reload_users_lock_data":
										DatabaseAccount.loadUsersLockData();
										break;
									case "reload_token_fcm_data":
										DatabaseAccount.loadTokenFCMData();
										break;
									case "reload_users_information_setting":
										DatabaseAccount.loadUsersInformationSetting();
										break;
									case "reload_email":
										DatabaseAccount.loadEmail();
										break;
									case "reloadIpLogin":
										DatabaseAccount.loadIpLogin();
										break;
									}
								}
								DatabaseAccount.SetAdminCommand(adminCommand);
							}
						}

						log.info("Running AdminMonitorThread !");
						Thread.sleep(LogsManager.sleepAdminModitor);
						if (countGC >= 360) {
							System.gc();
							countGC = 0;
						} else {
							countGC++;
						}
					} catch (Exception e) {
						logToFile(e);
					}
				}
			}
		});
	}

	private void runSessionMonitorThread() {
		threadPool.execute(new Runnable() {

			@Override
			public void run() {
				log.info("Running SessionMonitorThread !" + "    " + Thread.currentThread().getName());
				Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
				StringBuilder stringBuilder = new StringBuilder();
				SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while (!shutdown) {
					try {
						String[] str = datetimeFormatter.format(Calendar.getInstance().getTime()).split(" ");
						CCULog ccuLog = new CCULog();
						ccuLog.date = str[0];
						ccuLog.time = str[1];
						stringBuilder.delete(0, stringBuilder.length());
						stringBuilder.append("ChienThuatMoi: Memmory:")
								.append(Runtime.getRuntime().freeMemory() / 1024 / 1024).append("/")
								.append(Runtime.getRuntime().totalMemory() / 1024 / 1024).append("/")
								.append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(". Sessions:")
								.append(SessionManager.getInstance().getSessionTotal()).append(" Player ")
								.append(PlayerManager.getInstance().vtUsersOnlines.size()).append(" ThreadAuto : ")
								.append(AutoSchedulerManager.getInstance().getAutoSchedulerList().size());

						log.info(stringBuilder);

						System.out.println(String.format(
								"[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
								threadPool.getPoolSize(), threadPool.getCorePoolSize(), threadPool.getActiveCount(),
								threadPool.getCompletedTaskCount(), threadPool.getTaskCount(), threadPool.isShutdown(),
								threadPool.isTerminated()));
						Thread.sleep(60000);
					} catch (Exception e) {
						logToFile(e);
					}
				}
			}
		});
	}

	private void updateAllPlayer() {
		try {
			ArrayList<User> listPlayer = new ArrayList<User>();
			listPlayer.addAll(PlayerManager.getInstance().usersCachedAll.values());
//			DatabaseAccess.updatePlayer(listPlayer);
		} catch (Exception e) {
			logToFile(e);
		}
	}

	private void logToFile(Throwable e) {
		LogsManager.logToFile(e);
	}

	public void clonePlayerOnline() {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				log.info("Running Clone Player Online !" + "    " + Thread.currentThread().getName());
				Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
				while (!shutdown) {
					try {

						PlayerManager.getInstance().vtUsersOnlines = new ArrayList<User>(
								PlayerManager.getInstance().vtUsers);
						Thread.sleep(5000);
					} catch (Exception e) {
						logToFile(e);
					}
				}
			}
		});
	}

	public void shutdown() {
		try {
			shutdown = true;
			Thread.sleep(20000);
			threadPool.shutdown();
			Thread.sleep(10000);
			log.info("=========== Server close ===========");
			Runtime.getRuntime().exit(0);
		} catch (Exception e) {
			logToFile(e);
		}
	}

	public static void main(String[] args) throws Exception {
		new Thread(new NetworkServer()).start();
	}

	private void startThreadGroup() {
		try {
			MAINTERNANT = true;
			loadData();
			System.gc();
			runAutoSchedulerThread();
			RunAdminMonitorThread();
			runSessionMonitorThread();
			clonePlayerOnline();
			MAINTERNANT = false;
		} catch (Exception e) {
			logToFile(e);
			return;

		}
	}

	@Override
	public void run() {
		log.info("********* Server Cổng Game Bong Da Mobile *********");
		startThreadGroup();
		try {
			System.out.println(IP + "=======" + LISTEN_PORT);
			listenAddress = new InetSocketAddress(IP, LISTEN_PORT);
			startServer();

		} catch (Exception e) {
			logToFile(e);
			return;
		}
	}

	// create server channel
	private void startServer() throws IOException {
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();

		serverChannel.configureBlocking(false);

		// retrieve server socket and bind to port
		serverChannel.socket().bind(listenAddress);

		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		serverChannel.socket().setReceiveBufferSize(2 * 1024 * 1024);
		System.out.println("Server started...");
		ServiceGGame serviceGGame = new ServiceGGame();
		serviceGGame.createService();

		while (!shutdown) {
			// wait for events
			this.selector.select();
			// work on selected keys
			Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

			while (keys.hasNext()) {
				SelectionKey key = (SelectionKey) keys.next();

				// this is necessary to prevent the same key from coming up
				// again the next time around.
				keys.remove();
				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					this.accept(key);
				} else if (key.isReadable()) {
					this.read(key);
				}
			}
		}
	}

	// accept a connection made to this channel's socket
	private void accept(SelectionKey key) throws IOException {
		ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverChannel.accept();
		channel.configureBlocking(false);
		Socket socket = channel.socket();
		SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		System.out.println("Connected to: " + remoteAddr);

		// register channel with selector for further IO
		SessionManager.getInstance().addSession(channel, new Session(channel, comonhandler));
		channel.register(this.selector, SelectionKey.OP_READ);
	}

	// read from the socket channel
	private void read(SelectionKey key) throws IOException {
		SessionManager.mapSessions.get((SocketChannel) key.channel()).readMessage(key);
	}

}
