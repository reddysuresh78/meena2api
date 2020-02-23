package com.serverless;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.npl.pdf.NPLTester;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		 
//		LOG.info("received : queryStringParameters {}", input.get("queryStringParameters"));
//		LOG.info("received : body {}", input.get("body"));
		BirthChart npl = null;
		try {
			npl = NPLTester.generateNPL(input.get("body"));
		} catch (Exception e) {
			LOG.error("Error received: {}", e);
			e.printStackTrace();
		}
		
		Map<String, Object> body = new HashMap<>();
		body.put("NPL"  , npl);
 
		Response responseBody = new Response("Response to your request!!!!", body);
		Map<String, String> headers = new HashMap<>();
		headers.put("X-Powered-By", "AWS Lambda & serverless");
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Credentials",  "true");
		   
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(headers)
				.build();
	}
}
