package org.clicksend.internal;

import org.clicksend.internal.connection.ClickSendConnection;
import org.clicksend.internal.connection.provider.ClickSendConnectionProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mule.runtime.api.connection.ConnectionException;

public class ConnectionProviderTest {

	@Test
    public void connectionTest() throws ConnectionException
    {
		ClickSendConnectionProvider c = new ClickSendConnectionProvider();
		Assert.assertNotNull(c.connect());
		Assert.assertNotNull(c.validate(c.connect()));
		c.disconnect(new ClickSendConnection());
		
    }
}
