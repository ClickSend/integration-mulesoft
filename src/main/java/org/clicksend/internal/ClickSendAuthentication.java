/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.mule.runtime.http.api.client.auth.HttpAuthentication;
import org.mule.runtime.http.api.client.auth.HttpAuthenticationType;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;

public class ClickSendAuthentication implements HttpAuthentication {

	private final String username;
	private final String password;

	public ClickSendAuthentication(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public void authenticate(HttpRequestBuilder builder) {
		String auth = username + ":" + password;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
		builder.addHeader("Authorization", "Basic " + encodedAuth);
	}

	// If you need to implement asynchronous requests, you can override the
	// authenticate method for AsyncHttpRequestOptions
	public void authenticate() {
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpAuthenticationType getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPreemptive() {
		// TODO Auto-generated method stub
		return false;
	}
}
