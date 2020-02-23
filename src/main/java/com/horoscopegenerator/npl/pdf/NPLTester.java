package com.horoscopegenerator.npl.pdf;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.NativeDetails;

public class NPLTester {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	

	private static final Logger LOG = LogManager.getLogger(NPLTester.class);
	

	public static void main(String[] args) throws Exception {
		generateNPL("{\r\n" + 
				"    \"params\": {\r\n" + 
				"        \"longitude\": 78.4203776,\r\n" + 
				"        \"latitude\": 17.416192,\r\n" + 
				"        \"address\": \"Jubilee Hills,Hyderabad\",\r\n" + 
				"        \"date\": \"2-23-2020\",\r\n" + 
				"        \"time\": \"19:13\"\r\n" + 
				"    },\r\n" + 
				"    \"timeout\": 5000\r\n" + 
				"}");
	}
	
	
	public static BirthChart generateNPL(Object queryParams) throws Exception {
		 
		NativeDetails nd = new NativeDetails();
		nd.setTimeZoneId("Asia/Calcutta");
		nd.setOffset(-19800000);
		
		if(queryParams != null) {
	        
			populateNativeDetails(queryParams, nd);
		}else {
			populateDefaults(nd);
		}
		 
		NPLGeneratorForGivenTime2019 generator = new NPLGeneratorForGivenTime2019();
		 
		System.out.println("Input: " + nd.toString());
		BirthChart bc = generator.generate(nd, true, true);
		bc.getNavamsaChakra().getHouses().clear();
		bc.getNavamsaChakra().getOrderedHouses().clear();
		bc.getNavamsaChakra().getPlanetInfo().clear();
		bc.getRaasiChakra().getHouses().clear();
		bc.getRaasiChakra().getPlanetInfo().clear();
		bc.getRaasiChakra().setLagna(null);
		bc.setDasaLengthsInMillis(null);
		String jsonStr = objectMapper.writeValueAsString(bc);
		System.out.println(jsonStr);

		return bc;

	}

	private static void populateDefaults(NativeDetails nd) {
		double longitude = 78.48; // + 29/60.0; //78.48;
		double latitude = 17.37; // + 23/60.0; //17.37;
  
		nd.setDateOfBirth(15 + "-" + 2 + "-" + 2020);
		nd.setTimeOfBirth(18 + ":53");
		nd.setLatitude(latitude);
		nd.setLongitude(longitude);
		nd.setPlaceOfBirth("Hyderabad");
	 
	}


	private static void populateNativeDetails(Object queryParams, NativeDetails nd) {
		ObjectMapper objectMapper = new ObjectMapper();
 
		try {
			 
		    JsonNode jsonNode = objectMapper.readValue(queryParams.toString(), JsonNode.class);
		      
			jsonNode = jsonNode.get("params");
		        
		    String date = getStringValue(jsonNode, "date");
		    String time = getStringValue(jsonNode, "time");
		    
		    String place = getStringValue(jsonNode, "address");
		    double longitude = getDoubleValue(jsonNode, "longitude");
		    double latitude = getDoubleValue(jsonNode, "latitude");
		    
		    String outputDate= "";
		    
		    try {
				Date inputDate=new SimpleDateFormat("MM-dd-yyyy").parse(date);
				outputDate=new SimpleDateFormat("dd-MM-yyyy").format(inputDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    nd.setDateOfBirth(outputDate);
			nd.setTimeOfBirth(time);
			nd.setLatitude(  latitude) ;
			nd.setLongitude(  longitude) ;
			nd.setPlaceOfBirth(place);
			nd.setTimeZoneId("Asia/Calcutta");
			nd.setOffset(-19800000);
			nd.setFilePath("c:\\temp\\NPL_SPEC1-APR-2019.pdf");
			 	     

		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
	}
 
	private static String getStringValue(JsonNode jsonNode, String name) {
		  JsonNode valueNode = jsonNode.get(name);
		 
		  String value = valueNode.textValue();
//		  LOG.info("value node " + name + ":" + value );
		  return value;
 	}
	

	private static double getDoubleValue(JsonNode jsonNode, String name) {
		  JsonNode valueNode = jsonNode.get(name);
		 
		  double value = valueNode.asDouble();
//		  LOG.info("value node " + name + ":" + value );
		  return value;
 	}
	 
}
