package com.sgc.game.network.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sgc.game.server.NetworkServer;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class,MongoAutoConfiguration.class})
public class SocketsSpringApplication {

	public static void main(String[] args) {
//		SpringApplication application = new SpringApplication(SocketsSpringApplication.class);
//		application.addListeners(new ApplicationPidFileWriter("./springbootapp.pid"));
//		application.run();
		SpringApplication.run(SocketsSpringApplication.class, args);
		 System.out.println("===========0=======START SERVER=====");
		 new Thread(NetworkServer.getInstance()).start();
		
	}

}

