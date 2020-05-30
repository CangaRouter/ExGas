package it.polito.ezgas.controllertests;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import exception.GPSDataException;
import exception.PriceException;
import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.GasStationService;
import it.polito.ezgas.service.impl.GasStationServiceimpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestController {
	
	@Autowired
	private GasStationRepository gsRepository;
	@Autowired
	private UserRepository userRepository;

	private GasStationServiceimpl gsService;
	private GasStationConverter gsConverter;
	
	private GasStationDto gsDto;
	
	static Integer newGsId;
	
	/*@Before
	public void setUp() throws PriceException, GPSDataException{
		//gsConverter = new GasStationConverter();
		//gsService = new GasStationServiceimpl(gsRepository, gsConverter, userRepository);
		gsDto = new GasStationDto(null, "Repsol", "Corso Vittorio Emanuele II 169 Turin Piemont Italy", 
				true, true, false, false, true, "Enjoy", 45.07, 7.66, 1.26, 1.34, -1, -1, 1.17, 
				0, "29052020", 0.0);
		//gsService.saveGasStation(gsDto);
	}*/
			
	
	//TEST ON /gasstation
	@Test
	public void TC01_TestsaveGasStation() throws ClientProtocolException, IOException{  // /saveGasStation
		CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/gasstation/saveGasStation");
     
                
        String json = "{\"gasStationName\":\"APItest\","
                + "\"gasStationAddress\":\"Via Attilio Spinelli, 102 Latiano BR 72022\","
                + "\"carSharing\":\"Enjoy\","
                + "\"hasDiesel\":\"true\","
                + "\"hasSuper\":\"true\","
                + "\"hasSuperPlus\":\"true\","
                + "\"hasGas\":\"true\","
                + "\"hasMethane\":\"true\","
                + "\"lat\":40.5555662,"
                + "\"lon\":17.715888,"
                + "\"dieselPrice\":-1,"
                + "\"superPrice\":-1,"
                + "\"superPlusPrice\":-1,"
                + "\"gasPrice\":-1,"
                + "\"methanePrice\":-1,"
                + "\"reportUser\":-1,"
                + "\"reportTimestamp\":\"2020-05-29\"}";
        
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
     
        CloseableHttpResponse response = client.execute(httpPost);
        assert(response.getStatusLine().getStatusCode()==200);
        
        String jsonFromResponse=EntityUtils.toString(response.getEntity());
        Boolean thrown=false;
        JSONObject obj;
      try {
        obj = new JSONObject(jsonFromResponse);
          newGsId=new Integer(obj.getInt("gasStationId"));
      } catch (JSONException e) {
        // TODO Auto-generated catch block
    	  thrown=true;
        e.printStackTrace();
      }
      assert(thrown==false);
      client.close();
	}
	
	@Test
	public void TC02_testgetAllGasStations() throws ClientProtocolException, IOException { //getAllGasStations
			
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/getAllGasStations");
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		assert(response.getStatusLine().getStatusCode()==200);
		System.out.println("--------> Resp: "+response.toString());
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto[] gsArray=mapper.readValue(jsonFromResponse, GasStationDto[].class);
		System.out.println(gsArray);
		for(GasStationDto gs:gsArray) {
			System.out.println(gs.getGasStationId()+" "+gs.getGasStationName());
		}
		assert(gsArray.length>0);
		//assert(jsonFromResponse.contains("Corso Vittorio Emanuele II"));
		
	}
	
	@Test
	public void TC03_TestgetGasStationById() throws ClientProtocolException, IOException{  // /getGasStation
		assert(newGsId!=null);
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/getGasStation/"+newGsId.toString());
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		
		assert(response.getStatusLine().getStatusCode()==200);
		
		System.out.println("--------> Resp: "+response.toString());
		
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto gs=mapper.readValue(jsonFromResponse, GasStationDto.class);
		System.out.println(gs.getGasStationId());
		
		assert(gs.getGasStationId().equals(newGsId));
		
	}
	
	@Test
	public void TC04_TestgetGasStationByProximity() throws ClientProtocolException, IOException{ // /searchGasStationByProximity
		

		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/searchGasStationByProximity/40.5555662/17.715888/");
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		
		assert(response.getStatusLine().getStatusCode()==200);
		
		System.out.println("--------> Resp: "+response.toString());
		
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto[] gs=mapper.readValue(jsonFromResponse, GasStationDto[].class);
		
		
		assert(gs.length>0);
	}
	
	
	
	/*
	@Test
	public void TC05_TestsearchGasStationsByNeighborhood() throws ClientProtocolException, IOException{ // /searchGasStationByNeighborhood
	
	}*/
	
	@Test
	public void TC06_TestsearchGasStationsByGasolineType() throws ClientProtocolException, IOException{ // /searchGasStationByGasolineType
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation//searchGasStationByGasolineType/diesel");
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		
		assert(response.getStatusLine().getStatusCode()==200);
		
		System.out.println("--------> Resp: "+response.toString());
		
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto[] gs=mapper.readValue(jsonFromResponse, GasStationDto[].class);
		
		
		assert(gs.length>0);
	}
	
	@Test
	public void TC07_TestgetGasStationsWithCoordinates() throws ClientProtocolException, IOException{ // /getGasStationsWithCoordinates
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/getGasStationsWithCoordinates/40.5555662/17.715888/null/Enjoy");
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		
		assert(response.getStatusLine().getStatusCode()==200);
		
		System.out.println("--------> Resp: "+response.toString());
		
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto[] gs=mapper.readValue(jsonFromResponse, GasStationDto[].class);
		
		
		assert(gs.length>0);
	}
	/*
	@Test
	public void TC08_TestgetGasStationsWithoutCoordinates() throws ClientProtocolException, IOException{ // getGasStationsWithoutCoordinates
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/getGasStationsWithoutCoordinates/null/Enjoy");
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		System.out.println("--------> Resp getAll: "+response.getEntity().toString());
		
		assert(response.getStatusLine().getStatusCode()==200);
		
		System.out.println("--------> Resp: "+response.toString());
		
		String jsonFromResponse = EntityUtils.toString(response.getEntity());
		ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		GasStationDto[] gs=mapper.readValue(jsonFromResponse, GasStationDto[].class);
		
		
		assert(gs.length>0);
	}
	*/
	@Test
	public void TC09_TestsetReport() throws ClientProtocolException, IOException{ // /setGasStationReport
		 assert(newGsId!=null);
	      CloseableHttpClient client = HttpClients.createDefault();
	      HttpPost httpPost = new HttpPost("http://localhost:8080/gasstation/setGasStationReport/"+newGsId.toString()+"/1.1/1.2/1.3/1.4/1.5/1");
	   
	      httpPost.setHeader("Accept", "application/json");
	      httpPost.setHeader("Content-type", "application/json");
	   
	      CloseableHttpResponse response = client.execute(httpPost);
	      assert(response.getStatusLine().getStatusCode()==200);
	        
	        client.close();
	}
	
	@Test
	public void TC10_TestdeleteGasStation() throws ClientProtocolException, IOException{ // /deleteGasStation,
		assert(newGsId!=null);
		HttpUriRequest request = new HttpDelete("http://localhost:8080/gasstation/deleteGasStation/"+newGsId.toString());
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		assert(response.getStatusLine().getStatusCode()==200);
	}
	
	//TEST ON /user
	
	@Test
	public void TestgetUserById() throws ClientProtocolException, IOException{ // /getUser
	
	}
	
	@Test
	public void TestgetAllUsers() throws ClientProtocolException, IOException{ // /getAllUsers
	
	}
	
	@Test
	public void TestsaveUser() throws ClientProtocolException, IOException{ // /saveUser
	
	}
	
	@Test
	public void TestdeleteUser() throws ClientProtocolException, IOException{ // /deleteUser
	
	}
	
	@Test
	public void TestincreaseUserReputation() throws ClientProtocolException, IOException{ // /increaseUserReputation
	
	}
	
	@Test
	public void TestdecreaseUserReputation() throws ClientProtocolException, IOException{ // /decreaseUserReputation
		
	}
	
	@Test
	public void Testlogin() throws ClientProtocolException, IOException{ // /login
		
	}
}
