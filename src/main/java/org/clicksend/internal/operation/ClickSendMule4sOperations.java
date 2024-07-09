/**
 * The usage of this connector is governed by the terms in the LICENSE.md file.
 */
package org.clicksend.internal.operation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import org.clicksend.api.HttpResponseAttributes;
import org.clicksend.internal.ClickSendAuthentication;
import org.clicksend.internal.MMSMediaParameters;
import org.clicksend.internal.MMSParameters;
import org.clicksend.internal.SMSParameters;
import org.clicksend.internal.config.ClickSendConfiguration;
import org.clicksend.internal.connection.ClickSendConnection;
import org.clicksend.internal.connection.provider.ClickSendConnectionProvider;
import org.clicksend.internal.error.exception.ClickSendMmsException;
import org.clicksend.internal.error.exception.ClickSendSmsException;
import org.clicksend.internal.error.provider.MessageErrorTypeProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.runtime.core.api.MuleContext;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.fixed.OutputJsonType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.entity.InputStreamHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickSendMule4sOperations {

	@SuppressWarnings("deprecation")
	@Inject
    private MuleContext muleContext;
	
	private static final String APPLICATION_JSON_UTF_8 = "application/json; utf-8";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String ERROR_WHILE_PREPARING_REQUEST_PAYLOAD = "Error While Preparing Request Payload.";
	private static final String APPLICATION_JSON = "application/json";
	private static final String ACCEPT = "Accept";
	private static final String UTF_8 = "utf-8";
	private static final String BASIC = "Basic ";
	private static final String OK = "OK";
	private static final Logger LOGGER = LoggerFactory.getLogger(ClickSendMule4sOperations.class);
	
	private final HttpService httpService;
	private HttpClient httpClient;
	
	
	public ClickSendMule4sOperations(HttpService httpService, HttpClient httpClient) {
        this.httpService = httpService;
        this.httpClient = httpClient;
    }
	
	/**
	 * Sends SMS
	 * @param configuration
	 * @param connection
	 * @param smsParams
	 * @return
	 * @throws IOException
	 * @throws ClickSendSmsException 
	 * @throws TimeoutException 
	 */
	@MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	@Alias("SendSMS")
	@DisplayName("Send SMS")
	@Summary("Send SMS to a number")
	@OutputJsonType(schema = "sms.json")
	@Throws(MessageErrorTypeProvider.class)
	public Result<String, HttpResponseAttributes> sendSMS(@Config ClickSendConfiguration configuration,
			@Connection ClickSendConnection connection,
			@ParameterGroup(name = "SMS Parameters") SMSParameters smsParams) throws IOException, TimeoutException, ModuleException {

		ClickSendConnectionProvider connectionProvider = new ClickSendConnectionProvider();
		connectionProvider.start();
		String username = "";//configuration.getUserId();
		String password = "";//configuration.getPassword();

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		JSONObject root = null;
		try {
			root = new JSONObject();
			JSONArray arr = new JSONArray();
			JSONObject messageObj = new JSONObject();
			messageObj.put("to", smsParams.To);
			messageObj.put("source", "mulesoft");
			messageObj.put("body", smsParams.Message);

			if (smsParams.CustomString != null && !smsParams.CustomString.isEmpty()) {
				messageObj.put("custom_string", smsParams.CustomString);
			}

			arr.put(messageObj);
			root.put("messages", arr);
		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
		}
		HttpEntity input = new InputStreamHttpEntity(new ByteArrayInputStream(root.toString().getBytes())); 
		HttpRequest conn = ClickSendConnectionProvider.getConnection(connection, auth, "/mms/send", input);
		HttpResponse os = httpClient.send(conn, connection.getTimeoutAsMilliseconds(), false, new ClickSendAuthentication(username,password));
		connectionProvider.stop();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(os.getEntity().getContent()))) {
			StringBuilder response = new StringBuilder();
			HttpResponseAttributes attributes = new HttpResponseAttributes();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			attributes.setStatusCode(200);
			attributes.setMessage(OK);
			return Result.<String, HttpResponseAttributes>builder() 
			        .output(response.toString()) 
			        .attributes(attributes) 
			        .build();
		} catch (Exception e) {
			LOGGER.error("Error While Reading Response Payload.");
			e.printStackTrace();
			throw new ClickSendSmsException(e.getMessage());
		}
	}

	/**
	 * Sends MMS
	 * @param configuration
	 * @param connection
	 * @param mmsParams
	 * @param mmsMediaParams
	 * @return
	 * @throws IllegalArgumentException
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 * @throws IOException
	 * @throws ClickSendMmsException
	 * @throws TimeoutException 
	 */
	@MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	@Alias("SendMMS")
	@DisplayName("Send MMS")
	@Summary("Send MMS to a number")
	@OutputJsonType(schema = "mms.json")
	@Throws(MessageErrorTypeProvider.class)
	public Result<String, HttpResponseAttributes> sendMMS(@Config ClickSendConnectionProvider configuration,
			@Connection ClickSendConnection connection,
			@ParameterGroup(name = "MMS Parameters") MMSParameters mmsParams,
			@ParameterGroup(name = "MMS Media Parameters") MMSMediaParameters mmsMediaParams) throws IllegalArgumentException, UnsupportedEncodingException, JSONException, IOException, TimeoutException, ModuleException {
		String username = configuration.getUserId();
		String password = configuration.getPassword();

		String url = mmsMediaParams.FileURL;

		if (mmsMediaParams.FilePath != null && !mmsMediaParams.FilePath.isEmpty()) {
			url = UploadFile(connection, mmsMediaParams.FilePath, username, password);
			if (url == null) {
				throw new FileNotFoundException("Failed to Upload File.");
			}
		}

		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("Either File Path or File URL parameter must be provided.");
		}

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

		JSONObject root = null;
		try {
			root = new JSONObject();
			JSONArray arr = new JSONArray();
			JSONObject messageObj = new JSONObject();
			messageObj.put("to", mmsParams.To);
			messageObj.put("source", "mulesoft");
			messageObj.put("subject", mmsParams.Subject);

			if (mmsParams.From != null && !mmsParams.From.isEmpty()) {
				messageObj.put("from", mmsParams.From);
			}

			messageObj.put("body", mmsParams.Message);

			if (mmsParams.CustomString != null && !mmsParams.CustomString.isEmpty()) {
				messageObj.put("custom_string", mmsParams.CustomString);
			}

			arr.put(messageObj);
			root.put("messages", arr);
			root.put("media_file", url);

		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
			throw e;
		}
		HttpEntity input = new InputStreamHttpEntity(new ByteArrayInputStream(root.toString().getBytes())); 
		HttpRequest conn = ClickSendConnectionProvider.getConnection(connection, auth, "/mms/send", input);
		
		HttpResponse os = httpClient.send(conn, connection.getTimeoutAsMilliseconds(), false, new ClickSendAuthentication(username,password));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(os.getEntity().getContent()))) {
			StringBuilder response = new StringBuilder();
			HttpResponseAttributes attributes = new HttpResponseAttributes();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			attributes.setStatusCode(200);
			attributes.setMessage(OK);
			return Result.<String, HttpResponseAttributes>builder() 
			        .output(response.toString()) 
			        .attributes(attributes) 
			        .build();
		} catch (FileNotFoundException e) {
			LOGGER.error("Error While Reading Payload From Response.");
			e.printStackTrace();
			throw new ClickSendMmsException(e.getMessage());
		}
	}

	private String UploadFile(ClickSendConnection connection, String filePath, String username, String password)
			throws UnsupportedEncodingException, IOException, JSONException, TimeoutException {
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get(filePath));
		} catch (Exception e) {
			LOGGER.error("Failed to Read File.");
			e.printStackTrace();
			return null;
		}

		byte[] encoded = Base64.getEncoder().encode(bytes);

		String auth = BASIC + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
		
		
		JSONObject root = null;
		try {
			root = new JSONObject();
			root.put("content", new String(encoded));
		} catch (JSONException e) {
			LOGGER.error(ERROR_WHILE_PREPARING_REQUEST_PAYLOAD);
			e.printStackTrace();
			throw e;
		}

		HttpEntity input = new InputStreamHttpEntity(new ByteArrayInputStream(root.toString().getBytes())); 
		HttpRequest conn = ClickSendConnectionProvider.getConnection(connection, auth, "/mms/send",input);
		ClickSendAuthentication clickSendAuthentication = new ClickSendAuthentication(username,password);
		clickSendAuthentication.authenticate(HttpRequest.builder());
		LOGGER.info("httpClient: ",httpClient);
		HttpResponse os = httpClient.send(conn, connection.getTimeoutAsMilliseconds(), false, clickSendAuthentication);

		try (BufferedReader br = new BufferedReader(new InputStreamReader(os.getEntity().getContent()))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			JSONObject resObj = new JSONObject(response.toString());
			String url = resObj.getJSONObject("data").getString("_url");
			return url;
		} catch (FileNotFoundException e) {
			LOGGER.error("Error While Reading Payload From Response:");
			e.printStackTrace();
			throw e;
		}
	}

}
