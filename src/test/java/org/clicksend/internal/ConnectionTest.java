package org.clicksend.internal;

import java.net.HttpURLConnection;

import org.clicksend.internal.connection.ClickSendConnection;
import org.junit.Assert;
import org.junit.Test;

public class ConnectionTest {

	@Test
    public void connectionTest()
    {
		ClickSendConnection a = new ClickSendConnection();
		HttpURLConnection r = a.GetConnection("/sms/send");
		System.out.println(r.toString());  
		Assert.assertNotNull(r);
		
    }
	
	@Test
    public void invalidateTest()
    {
		ClickSendConnection a = new ClickSendConnection();
		HttpURLConnection r = a.GetConnection("/sms/send");
		a.invalidate();
		Assert.assertNotNull(r);
    }
	
//	@Test(expected = MalformedURLException.class)
//    public void getConnectionMalformedURLExceptionTest()
//    {
//		ClickSendMule4sConnection a = new ClickSendMule4sConnection();
//		HttpURLConnection r = a.GetConnection("*");
//		//Assert.assertEquals("Either File Path or File URL parameter must be provided.", e.getMessage());
//    }
}
