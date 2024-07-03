package org.clicksend.internal;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.clicksend.internal.connection.ClickSendConnection;
import org.clicksend.internal.connection.provider.ClickSendConnectionProvider;
import org.clicksend.internal.error.exception.ClickSendSmsException;
import org.clicksend.internal.operation.ClickSendMule4sOperations;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.core.api.config.ConfigurationException;
import org.mule.runtime.core.api.context.MuleContextFactory;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;

@RunWith(value = BlockJUnit4ClassRunner.class)
public class OperationTest {

    private HttpService httpService;
    private HttpClient httpClient;

	
	@Test
    public void smsTest() throws IOException, ClickSendSmsException, TimeoutException
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations(httpService, httpClient);
		ClickSendConnectionProvider configuration = new ClickSendConnectionProvider();
		ClickSendConnection connection = new ClickSendConnection();
		SMSParameters smsParams = new SMSParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		smsParams.setFrom("+61411111111");
		smsParams.setTo("+61411111111");
		smsParams.setMessage("test message, please ignore");
		smsParams.setCustomString("test message, please ignore");
		
		Result<String, HttpResponseAttributes> r = op.sendSMS(configuration, connection, smsParams);
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
    }
	
	@Test
    public void mmsWithFilePathTest() throws Exception
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations(httpService, httpClient);
		ClickSendConnectionProvider configuration = new ClickSendConnectionProvider();
		ClickSendConnection connection = new ClickSendConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFilePath("src/test/resources/Mercedes.jpg");
		
		Result<String, HttpResponseAttributes> r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
		
    }
	
	@Test
    public void mmsWithoutFilePathTest() 
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations(null, null);
		ClickSendConnectionProvider configuration = new ClickSendConnectionProvider();
		ClickSendConnection connection = new ClickSendConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFilePath("src/test/resources/Mercedes1.jpg");
		
		Result<String, HttpResponseAttributes> r = null;
		try {
			r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		} catch (Exception e) {
			
			Assert.assertEquals("Failed to Upload File.", e.getMessage());
			//assertThrows(IllegalArgumentException.class, null);
		}
		
    }
	
	@Test
    public void mmsWithoutFileURLTest() 
    {
		ClickSendMule4sOperations op = new ClickSendMule4sOperations(null, null);
		ClickSendConnectionProvider configuration = new ClickSendConnectionProvider();
		ClickSendConnection connection = new ClickSendConnection();
		MMSParameters mmsParams = new MMSParameters();
		MMSMediaParameters mmsMediaParams = new MMSMediaParameters();
		
		configuration.setUserId("clicksendtest@gmail.com");
		configuration.setPassword("10065E57-5648-766E-4026-477775D83E99");
		
		mmsParams.setFrom("+61411111111");
		mmsParams.setTo("+61411111111");
		mmsParams.setMessage("test message, please ignore");
		mmsParams.setCustomString("test message, please ignore");
		mmsParams.setSubject("test message");
		mmsMediaParams.setFileURL(null);
		
		Result<String, HttpResponseAttributes> r = null;
		try {
			r = op.sendMMS(configuration, connection, mmsParams, mmsMediaParams);
		} catch (Exception e) {
			
			Assert.assertEquals("Either File Path or File URL parameter must be provided.", e.getMessage());
			//assertThrows(IllegalArgumentException.class, null);
		}
		
    }
}
