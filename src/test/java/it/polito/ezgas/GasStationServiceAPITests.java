package it.polito.ezgas;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;

import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.impl.GasStationServiceimpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)

public class GasStationServiceAPITests {

	@Autowired
	private GasStationRepository gasStationRepository;
	
	@Autowired
	private UserRepository userRepository;

	private GasStationServiceimpl gasStationService;
	
	private GasStationConverter gasStationConverter;
	
	private GasStation gasStation;
	private GasStationDto gasStationDto;
	private User user;
	private IdPw credentials;
	
	@Before
	public void setUp() {
		gasStationConverter = new GasStationConverter();
		gasStationService = new GasStationServiceimpl(gasStationRepository, gasStationConverter, userRepository);
		gasStation = new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		gasStationDto = new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true,
				"Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		user = new User("nome", "password", "email", 0);
		credentials = new IdPw("user", "pwd");
	}
	
	@After
	public void destroy(){
		gasStationRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	@Test
	public void TC1_getGasStationById() {
		// try to get a gas station with invalid id
		
	}

	@Test
	public void TC2_getGasStationById() {
		// try to retrieve a gas station with a non existing id
		
	}

	@Test
	public void TC3_getGasStationById() {
		// try to retrieve a gas station with an existing id
		
	}

	@Test
	public void TC1_saveGasStation() {
		// try to save a gas station which has no fuels -> ERROR?
		
	}

	@Test
	public void TC2_saveGasStation() throws PriceException, GPSDataException {
		// saving without errors (all prices and no ID)

	}

	@Test
	public void TC5_saveGasStation() {
		// try to update an already existing gas station
		
	}

	@Test
	public void TC1_getAllGasStations() {
		// try to retrieve an empty list
		
	}

	@Test
	public void TC2_getAllGasStations() {
		// try to retrieve a list not empty
		
	}

	@Test
	public void TC1_deleteGasStation() {
		// try to delete a gas station with a negtive id (exception)
		
	}

	@Test
	public void TC2_deleteGasStation() {
		// try to delete an existing gas station
		
	}

	@Test
	public void TC3_deleteGasStation() {
		// try to delete a non existing gas station
		
	}

	@Test
	public void TC1_getGasStationsByGasolineType() {
		// try to get a gas station with invalid fuel type
		
	}

	@Test
	public void TC2_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Diesel)
		
	}

	@Test
	public void TC3_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Super)
		
	}

	@Test
	public void TC4_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Methane)
		
	}

	@Test
	public void TC5_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Gas)
		
	}

	@Test
	public void TC6_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (SuperPlus)
		
	}

	@Test
	public void TC1_getGasStationsByProximity() {
		// valid coordinates
		
	}

	@Test
	public void TC2_getGasStationsByProximity() {
		// invalid coordinates
		
	}

	// one test for each fuel type;
	@Test
	public void TC1_getGasStationsWithCoordinates() {
		// null fuel type and null car sharing -> all gas stations
		
	}

	@Test
	public void TC2_getGasStationsWithCoordinates() {
		// Select ANY fuel type and car sharing (Enjoy) -> one gas station matches (just
		// inserted)
		
	}

	@Test
	public void TC3_getGasStationsWithCoordinates() {
		// ANY fuel type and car sharing (Car2GO) -> no gas station matches
		
	}

	@Test
	public void TC4_getGasStationsWithCoordinates() {
		// INVALID fuel type and ANY car sharing
		
	}

	@Test
	public void TC5_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel YES) and ANY car sharing
		
	}

	@Test
	public void TC6_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel NO) and ANY car sharing
		
	}

	@Test
	public void TC7_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->super YES) and ANY car sharing
		
	}

	@Test
	public void TC8_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->super NO) and ANY car sharing
		
	}

	@Test
	public void TC9_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->methane YES) and ANY car sharing
		
	}

	@Test
	public void TC10_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->methane NO) and ANY car sharing
		
	}

	@Test
	public void TC11_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->gas YES) and ANY car sharing
		
	}

	@Test
	public void TC12_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel NO) and ANY car sharing
		
	}

	@Test
	public void TC13_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->superplus YES) and ANY car sharing
		
	}

	@Test
	public void TC14_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->superplus NO) and ANY car sharing
		
	}

	@Test
	public void TC1_getGasStationsWithoutCoordinates() {
		// null fuel type and null car sharing
		
	}

	@Test
	public void TC2_getGasStationsWithoutCoordinates() {
		// null fuel type and SET car sharing
		
	}

	@Test
	public void TC3_getGasStationsWithoutCoordinates() {
		// SET fuel type and SET car sharing
		
	}

	@Test
	public void TC4_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		
	}

	@Test
	public void TC5_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		
	}

	@Test
	public void TC6_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		
	}

	@Test
	public void TC7_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		
	}

	@Test
	public void TC8_getGasStationsWithoutCoordinates() {
		// SET fuel type and SET car sharing
		
	}

	@Test
	public void TC1_getGasStationByCarSharing() {
		
	}

	@Test
	public void TC1_setReport() {
		// existing user sets all prices
		
	}

	@Test
	public void TC2_setReport() {
		// invalid user
		
	}

	@Test
	public void TC4_setReport() {
		// non existing user
		
	}

	@Test
	public void TC5_setReport() {
		// non-esisting gas station
		
	}

}
