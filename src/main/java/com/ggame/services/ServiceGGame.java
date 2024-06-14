package com.ggame.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.sun.net.httpserver.HttpServer;

import config.Config;

public class ServiceGGame {	
//	public static int portService = 5656;
	public void createService() {
		HttpServer server;
		try {
			System.out.println("================= Vao Service GGame ====================== ");
			server = HttpServer.create(new InetSocketAddress(Config.portService), 0);
			server.createContext("/api", new ServicesAPI());
			server.createContext("/checkLogin", new ServicesCheckLogin());		
			server.createContext("/updateAccountPlayNow", new ServicesUpdateAccountPlayNow());		
			server.createContext("/registerWeb", new ServicesRegisterWeb());
			server.createContext("/getCountry", new ServicesGetCountry());
			server.createContext("/getText", new ServicesGetText());
			server.createContext("/getInfoAccount", new ServiceGetInfoAccount());
			server.createContext("/updateInfoAccount", new ServiceUpdateInfoAccount());
			
			server.createContext("/addmoney", new ServicesAddMoney());
			server.createContext("/registerServerGame", new ServicesRegisterAccountServerGame());
			server.createContext("/changeDisplayerName", new ServicesChangeDisplayerName());
			server.createContext("/changePassword", new ServicesChangePassword());
			server.createContext("/updateEmail", new ServicesUpdateEmail());
			server.createContext("/registerAccount", new ServicesRegisterAccountCongGame());
			server.createContext("/loginAccount", new ServicesLoginCongGame());
			server.createContext("/giftcode", new ServicesGiftCode());
			server.createContext("/getGiftcode", new ServicesGetGiftCode());
			server.createContext("/urlLockAccount", new ServicesLockAccount());
			server.createContext("/pushnotification", new ServicesPushnotification());
			server.createContext("/updateTokenFCM", new ServicesUpdateTokenFCM());
			server.createContext("/getThongBao", new ServicesGetThongBao());
			server.createContext("/updateThongBao", new ServicesUpdateThongBao());
			LinkedBlockingQueue queue = new LinkedBlockingQueue<>();
			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 30, TimeUnit.SECONDS, queue);
			server.setExecutor(threadPoolExecutor);
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
