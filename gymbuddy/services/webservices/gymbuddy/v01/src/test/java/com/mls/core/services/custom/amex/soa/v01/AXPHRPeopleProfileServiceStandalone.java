package com.mls.core.services.custom.amex.soa.v01;

import org.apache.camel.spring.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AXPHRPeopleProfileServiceStandalone {

	protected static Logger log = LoggerFactory.getLogger(AXPHRPeopleProfileServiceStandalone.class);

	private Main main;

	private void boot() throws Exception { 
		main = new Main();
		AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:META-INF/spring/beans-properties.xml", "classpath:META-INF/spring/beans-*.xml");
		main = new Main();
		main.setApplicationContext(context);
		main.enableHangupSupport();
		main.run();
	}

	public static void main(String[] args) throws Exception {
		AXPHRPeopleProfileServiceStandalone instance = new AXPHRPeopleProfileServiceStandalone();
		instance.boot();
	}

}