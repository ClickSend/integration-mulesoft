/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.connection.provider;

import java.net.ProtocolException;

import javax.inject.Inject;

import org.clicksend.internal.connection.ClickSendConnection;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.lifecycle.Startable;
import org.mule.runtime.api.lifecycle.Stoppable;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.http.api.client.proxy.ProxyConfig;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * This class (as it's name implies) provides connection instances and the
 * funcionality to disconnect and validate those connections.
 * <p>
 * All connection related parameters (values required in order to create a
 * connection) must be declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares
 * that connections resolved by this provider will be pooled and reused. There
 * are other implementations like {@link CachedConnectionProvider} which lazily
 * creates and caches connections or simply {@link ConnectionProvider} if you
 * want a new connection each time something requires one.
 */
public class ClickSendConnectionProvider implements CachedConnectionProvider<ClickSendConnection>, Startable, Stoppable,
		PoolingConnectionProvider<ClickSendConnection> {

	private static final String APPLICATION_JSON_UTF_8 = "application/json; utf-8";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String APPLICATION_JSON = "application/json";
	private static final String ACCEPT = "Accept";
	private static final String UTF_8 = "utf-8";
	private static final String BASIC = "Basic ";
	private static final String BASE_URL = "https://rest.clicksend.com/v3";
	private static final Logger LOGGER = LoggerFactory.getLogger(ClickSendConnectionProvider.class);


	@RefName
	private String configName;

	@Parameter
	@Placement(tab = "DEFAULT_TAB")
	@DisplayName(value = "Username/Email")
	@Order(value = 1)
	@Example(value = "abc@xyz.com")
	private String userId;

	@Parameter
	@Placement(tab = "DEFAULT_TAB")
	@DisplayName(value = "Password")
	@Order(value = 2)
	@Password
	private String password;

	@Inject
	private HttpService httpService;
	private HttpClient httpClient;

	@Parameter
	@Optional
	@Placement(tab = "Proxy")
	private ProxyConfig proxyConfig;

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public ClickSendConnection connect() throws ConnectionException {
		// TODO Auto-generated method stub
		return new ClickSendConnection();
	}

	@Override
	public void disconnect(ClickSendConnection connection) {
		try {
			connection.invalidate();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Error while disconnecting: " + e.getMessage(), e);
			}
			e.printStackTrace();
			LOGGER.error("Error while disconnecting: " + e.getMessage(), e);
		}
	}

	@Override
	public ConnectionValidationResult validate(ClickSendConnection connection) {
		return ConnectionValidationResult.success();
	}

	public static HttpRequest getConnection(ClickSendConnection connection, String auth, String endpoint,
			HttpEntity input) throws ProtocolException {
//		HttpURLConnection conn = connection.GetConnection(endpoint);
//		conn.setRequestMethod("POST");
//		conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON_UTF_8);
//		conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
//		conn.setRequestProperty(AUTHORIZATION, auth);
//		conn.setDoOutput(true);
		HttpRequest getRequest = HttpRequest.builder().method(HttpConstants.Method.POST)
				.addHeader(ACCEPT, APPLICATION_JSON).addHeader(CONTENT_TYPE, APPLICATION_JSON_UTF_8)
				.addHeader(AUTHORIZATION, auth).entity(input).uri(BASE_URL + endpoint).build();

		return getRequest;
	}

	@Override
	public void start() {
		HttpClientConfiguration httpClientConfiguration = new HttpClientConfiguration.Builder().setName(configName)
				.setStreaming(true).setProxyConfig(proxyConfig).build();
		httpClient = httpService.getClientFactory().create(httpClientConfiguration);
		httpClient.start();
		LOGGER.info("HTTP Client started successfully");
	}
	
	@Override
    public void stop() {
        if (httpClient != null) {
            httpClient.stop();
            LOGGER.info("HTTP Client stopped successfully");
        }
    }


}
