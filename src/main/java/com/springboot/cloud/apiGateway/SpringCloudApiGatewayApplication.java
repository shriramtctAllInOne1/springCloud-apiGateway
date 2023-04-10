package com.springboot.cloud.apiGateway;

import com.springboot.cloud.apiGateway.config.RefreshableRoutesLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class SpringCloudApiGatewayApplication {

	@Autowired
	RefreshableRoutesLocator refreshableRoutesLocator;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudApiGatewayApplication.class, args);
	}


	@EventListener(ApplicationReadyEvent.class)
	public void contextRefreshEvent(){
		refreshableRoutesLocator.clearRoutes();
		refreshableRoutesLocator.setTimeouts();
		refreshableRoutesLocator.addRoutes();
		refreshableRoutesLocator.buildRoute();
	}
}
