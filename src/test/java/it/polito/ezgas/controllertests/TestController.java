package it.polito.ezgas.controllertests;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import exception.GPSDataException;
import exception.PriceException;
import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.GasStationService;
import it.polito.ezgas.service.impl.GasStationServiceimpl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TestController {
	
	@Autowired
	private GasStationRepository gsRepository;
	@Autowired
	private UserRepository userRepository;

	private GasStationServiceimpl gsService;
	private GasStationConverter gsConverter;
	
	private GasStationDto gsDto;
	
/*	
	@Before
	public void setUp() throws PriceException, GPSDataException{
		gsConverter = new GasStationConverter();
		gsService = new GasStationServiceimpl(gsRepository, gsConverter, userRepository);
		gsDto = new GasStationDto(null, "Repsol", "Corso Vittorio Emanuele II 169 Turin Piemont Italy", 
				true, true, false, false, true, "Enjoy", 45.07, 7.66, 1.26, 1.34, -1, -1, 1.17, 
				0, "29052020", 0.0);
		gsService.saveGasStation(gsDto);
	}
			*/
	
	//TEST ON /gasstation
	@Test
	public void testgetAllGasStations() throws ClientProtocolException, IOException { //getAllGasStations
		
		HttpUriRequest request = new HttpGet("http://localhost:8080/gasstation/getAllGasStations");
		
		HttpResponse response = HttpClientBuilder.create().build().execute(request);
		
		assert(response.getStatusLine().getStatusCode()==200);
		//String jsonFromResponse = EntityUtils.toString(response.getEntity());
		
		//assert(jsonFromResponse.contains("Corso Vittorio Emanuele II"));
	}
	
	@Test
	public void TestgetGasStationById() throws ClientProtocolException, IOException{  // /getGasStation
		
	}
	
	@Test
	public void TestgetGasStationByProximity() throws ClientProtocolException, IOException{ // /searchGasStationByProximity
		
	}
	
	@Test
	public void TestsaveGasStation() throws ClientProtocolException, IOException{  // /saveGasStation
		
	}
	
	@Test
	public void TestdeleteGasStation() throws ClientProtocolException, IOException{ // /deleteGasStation,
	
	}
	
	@Test
	public void TestsearchGasStationsByNeighborhood() throws ClientProtocolException, IOException{ // /searchGasStationByNeighborhood
	
	}
	
	@Test
	public void TestsearchGasStationsByGasolineType() throws ClientProtocolException, IOException{ // /searchGasStationByGasolineType
	
	}
	
	@Test
	public void TestgetGasStationsWithCoordinates() throws ClientProtocolException, IOException{ // /getGasStationsWithCoordinates
	
	}
	
	@Test
	public void TestgetGasStationsWithoutCoordinates() throws ClientProtocolException, IOException{ // getGasStationsWithoutCoordinates
	
	}
	
	@Test
	public void TestsetReport() throws ClientProtocolException, IOException{ // /setGasStationReport
	
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
