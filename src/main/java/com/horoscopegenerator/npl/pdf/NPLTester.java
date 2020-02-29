package com.horoscopegenerator.npl.pdf;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.horoscopegenerator.BirthChart;
import com.horoscopegenerator.House;
import com.horoscopegenerator.NativeDetails;
import com.horoscopegenerator.PlanetInfo;
import com.horoscopegenerator.Raasi;

public class NPLTester {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	

	private static final Logger LOG = LogManager.getLogger(NPLTester.class);
	

	public static void main(String[] args) throws Exception {
		generateNPL("{\r\n" + 
				"    \"params\": {\r\n" + 
				"        \"longitude\": 78.4203776,\r\n" + 
				"        \"latitude\": 17.416192,\r\n" + 
				"        \"address\": \"Jubilee Hills,Hyderabad\",\r\n" + 
				"        \"date\": \"2020-02-29\",\r\n" + 
				"        \"time\": \"12:13\"\r\n" + 
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
		
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm:ss");
	 	String formatted = format1.format(bc.getSunriseTime().getTime());
		System.out.println(formatted);
		bc.setSunrise(formatted);
		
		adjustPlanetOrder(bc.getRaasiChakra().getOrderedHouses());
		 
		 
		String jsonStr = objectMapper.writeValueAsString(bc);
		System.out.println(jsonStr);

		return bc;

	}

	private static void adjustPlanetOrder(List<House> orderedHouses) {
 
		for(House house: orderedHouses) {
			ArrayList<PlanetInfo> sortedPlanets = new ArrayList<PlanetInfo>();
			 
          for (int i = 0; i < house.getPlanets().size(); i++) {
        	  int insertAt = sortedPlanets.size();
        	  PlanetInfo planetInfo = house.getPlanets().get(i);

        	  for (int j = 0; j < sortedPlanets.size(); j++) {
        		  if(house.getRaasi()== Raasi.DHANUS || house.getRaasi()== Raasi.MAKARAM || 
        				  house.getRaasi()== Raasi.KUMBHAM || house.getRaasi()== Raasi.MEENAM ) {
	        		  if (((PlanetInfo) sortedPlanets.get(j)).longitude <= planetInfo.longitude) {
	        			  insertAt = j;
	        			  break;
	        		  }
        		  }else {
	        		  if (((PlanetInfo) sortedPlanets.get(j)).longitude >= planetInfo.longitude) {
	        			  insertAt = j;
	        			  break;
	        		  }
        		  }
        		  
        	  }
        	  sortedPlanets.add(insertAt, planetInfo);
          }
          house.setPlanets(sortedPlanets);
		}
		
	}


	private static void populateDefaults(NativeDetails nd) {
		double longitude = 78.48; // + 29/60.0; //78.48;
		double latitude = 17.37; // + 23/60.0; //17.37;
  
		nd.setDateOfBirth(23 + "-" + 2 + "-" + 2020);
		nd.setTimeOfBirth(19 + ":40");
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
				Date inputDate=new SimpleDateFormat("yyyy-MM-dd").parse(date.substring(0, 10));
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
