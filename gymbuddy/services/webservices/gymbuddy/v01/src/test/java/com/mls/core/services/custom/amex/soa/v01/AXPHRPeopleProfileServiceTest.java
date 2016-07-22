package com.mls.core.services.custom.amex.soa.v01;

import com.mls.common.GSONJAXRSProvider;
import com.mls.common.MlsLoggingInInterceptor;
import com.mls.common.MlsLoggingOutInterceptor;
import com.mls.common.test.categories.SystemIntegrationTest;
import com.mls.core.services.custom.amex.soa.api.v01.AXPHRPeopleProfileService;
import com.mls.core.services.custom.amex.soa.api.v01.wsdl.PeopleProfileType;
import org.apache.camel.ServiceStatus;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:META-INF/spring/beans-properties.xml", "classpath:META-INF/spring/beans-*.xml", "classpath:META-INF/spring/beans-mls-api-service-webservices-base.xml", "classpath:META-INF/spring/beans-mls-api-service-webservices-custom-amex-soa.xml", "classpath:META-INF/spring/beans-mls-clients-custom-amex-soa-AXPHRPeopleService.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@DisableJmx(false)
@Category(SystemIntegrationTest.class)
public class AXPHRPeopleProfileServiceTest extends CamelTestSupport {

	static String baseUrl = null;

	static Properties properties = null;

	@BeforeClass
	public static void setupClass() throws Exception {

		properties = new Properties();
		URL propertyFileUrl = getFileUrlByName("mls-services.properties");
		if(propertyFileUrl != null) {
			properties.load(new FileInputStream(new File(propertyFileUrl.toURI())));
			baseUrl = properties.getProperty("com.mls.service.api.base.url");
		}

	}

	@AfterClass
	public static void afterClass() throws Exception {
		properties = null;
	}

	@Test
	public void testAmexSoaPeopleProfile() throws Exception {
		Assert.assertEquals(ServiceStatus.Started, context.getStatus());
		Assert.assertNotNull(baseUrl);
		String serviceUrl = properties.getProperty("com.mls.service.custom.amex.soa.v01.rest.url");
		Assert.assertNotNull(serviceUrl);
		String restUrl = baseUrl + serviceUrl;

		AXPHRPeopleProfileService axPHRPeopleProfileService = getRestService(AXPHRPeopleProfileService.class, restUrl);
		Assert.assertNotNull(axPHRPeopleProfileService);

		PeopleProfileType peopleProfileType = axPHRPeopleProfileService.getPeopleProfileByPeopleIdAndSystemId("DEV", "7022555");
		Assert.assertNotNull(peopleProfileType);
	}

	private static <T> T getRestService(Class<T> clazz, String url) throws Exception {
		T serviceRest = null;
		if(serviceRest == null) {
			List providers = new ArrayList<Object>();
			providers.add(new GSONJAXRSProvider());
			//providers.add(new GsonClientExceptionResponseMapper());
			serviceRest = (T)JAXRSClientFactory.create(url, clazz, providers);

			WebClient.getConfig(serviceRest).getHttpConduit().getClient().setReceiveTimeout(0L);
			WebClient.getConfig(serviceRest).getHttpConduit().getClient().setAllowChunking(true);
			WebClient.getConfig(serviceRest).getHttpConduit().getClient().setChunkingThreshold(4096);
			WebClient.getConfig(serviceRest).getHttpConduit().getClient().setChunkLength(-1);
			WebClient.getConfig(serviceRest).getOutInterceptors().add(new MlsLoggingOutInterceptor());
			WebClient.getConfig(serviceRest).getInInterceptors().add(new MlsLoggingInInterceptor());
			WebClient.client(serviceRest).type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		}
		return serviceRest;

	}

	private static URL getFileUrlByName(String fileName) {
		URL url = AXPHRPeopleProfileServiceTest.class.getResource(fileName);
		if(url == null)
			url = AXPHRPeopleProfileServiceTest.class.getClassLoader().getResource(fileName);
		if(url == null)
			url = ClassLoader.getSystemResource(fileName);
		if(url == null)
			url = ClassLoader.getSystemClassLoader().getResource(fileName);
		if(url == null)
			url = Thread.currentThread().getClass().getResource(fileName);
		if(url == null)
			url = Thread.currentThread().getContextClassLoader().getResource(fileName);
		return url;
	}
}