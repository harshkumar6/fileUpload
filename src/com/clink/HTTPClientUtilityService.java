package com.clink;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class HTTPClientUtilityService {

	public String postBodyRequest(String resourceUrl, Map<String, ?> requestBody, Map<String, String> headers)
			throws URISyntaxException {
		String responseString = null;
		HttpClient httpclient = HttpClients.createDefault();
		ObjectNode node = new ObjectNode(new JsonNodeFactory(true));
		List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
		requestBody.entrySet().stream().forEach(s -> {
			nameValuePairs.add(new BasicNameValuePair(s.getKey(), s.getValue().toString()));
		});
		HttpPost request = new HttpPost(resourceUrl);
		try {
			headers.entrySet().stream().forEach(s -> {
				request.addHeader(s.getKey(), s.getValue().toString());
			});
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			org.apache.http.HttpResponse response = httpclient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity);
			node.put("status", status);
			//logger.debug("response is: {}", responseString);
		} catch (Exception e) {
			//logger.error("error occurred while sending email {}", e);
		}
		return responseString;
	}
	
	public String postBodyRequestAsString(String resourceUrl, Map<String, ?> requestBody, Map<String, String> headers)
			throws URISyntaxException {
		//logger.debug("inside postbody request {}", requestBody);
		String responseString = null;
		HttpClient httpclient = HttpClients.createDefault();
		ObjectNode node = new ObjectNode(new JsonNodeFactory(true));
		List<NameValuePair> nameValuePairs = new LinkedList<NameValuePair>();
		requestBody.entrySet().stream().forEach(s -> {
			nameValuePairs.add(new BasicNameValuePair(s.getKey(), s.getValue().toString()));
		});
		HttpPost request = new HttpPost(resourceUrl);
		try {
			headers.entrySet().stream().forEach(s -> {
				request.addHeader(s.getKey(), s.getValue().toString());
			});
			ObjectMapper map = new ObjectMapper();
			String strObj = map.writeValueAsString(requestBody);
			StringEntity se = new StringEntity(strObj);
			se.setContentType("application/json");
			request.setEntity(se);
			org.apache.http.HttpResponse response = httpclient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity);
			node.put("status", status);
		//	logger.debug("response is: {}", responseString);
		} catch (Exception e) {
			//logger.error("error occurred while sending email {}", e);
		}
		return responseString;
	}
}
