package it.polito.ezgas;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
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
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "2020-05-03", 0.88);
		gasStationDto = new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true,
				"Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "2020-05-03", 0.88);
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
		Boolean thrown = false;
		try {
			gasStationService.getGasStationById(-1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC2_getGasStationById() {
		// try to retrieve a gas station with a non existing id
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationById(65), null);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationById() throws PriceException, GPSDataException {
		// try to retrieve a gas station with an existing id
		Boolean thrown = false;
		gasStationService.setUpdateDependability(true);
		GasStationDto res=gasStationService.saveGasStation(gasStationDto);
		assert(res!=null);
		try {
			assertNotNull(gasStationService.getGasStationById(res.getGasStationId()));
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
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
		assertEquals(gasStationService.getAllGasStations().isEmpty(), true);
	}

	@Test
	public void TC2_getAllGasStations() throws PriceException, GPSDataException {
		// try to retrieve a list not empty
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		assertEquals(gasStationService.getAllGasStations().isEmpty(), false);
	}

	@Test
	public void TC1_deleteGasStation() {
		// try to delete a gas station with a negative id (exception)
		
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
	public void TC1_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// null fuel type and null car sharing -> the returned list contains gasStationDto and gasStationDto2
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, true, true, true, true,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		GasStationDto gasStationDto3 = new GasStationDto(null, "Shell", "via Risorgimento", false, true, true, true, true,
				"Car2go", 20.0005, 35.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto3)!=null);
		Boolean thrown = false;
		try {
			List<GasStationDto> list=gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "null");
			assertEquals(list.isEmpty(),false);
			for(GasStationDto gs:list) {
				assert(gs.getGasStationName().equals(gasStationDto.getGasStationName())||gs.getGasStationName().equals(gasStationDto2.getGasStationName()));
				assert(!gs.getGasStationName().equals(gasStationDto3.getGasStationName()));
			}
			
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	
	}

	@Test
	public void TC2_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// Select ANY fuel type and car sharing (Enjoy) -> one gas station matches (gasStationDto)
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, true, true, true, true,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		GasStationDto gasStationDto3 = new GasStationDto(null, "Shell", "via Risorgimento", false, true, true, true, true,
				"Car2go", 20.0005, 35.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto3)!=null);
		Boolean thrown = false;
		try {
			List<GasStationDto> list=gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Enjoy");
			assertEquals(list.isEmpty(),false);
			for(GasStationDto gs:list) {
				System.out.println(gs);
			}
			assert(list.size()==1);
			for(GasStationDto gs:list) {
				assert(gs.getGasStationName().equals(gasStationDto.getGasStationName()));
			}
			
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// ANY fuel type and car sharing (Car2GO) -> no gas station matches
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		Boolean thrown = false;
		try {
			List<GasStationDto> list=gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Car2Go");
			assertEquals(list.isEmpty(),true);
			//assert(list.contains(gasStationDto));
			for(GasStationDto gs:list) {
				System.out.println(gs);
			}
			assert(list.isEmpty());
			
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC4_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// INVALID fuel type and ANY car sharing
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		try {
			gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "d1esel!!!!", "null");
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC5_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->diesel YES) and ANY car sharing
		//The repository has only gasStationDto and gasStationDto2  -> only gasStationDto is present in the returned list
		
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "diesel", "null").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC6_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->diesel NO) and ANY car sharing
		//The repository has only gasStationDto2 ->the returned list is empty
		
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "diesel", "null").isEmpty(),
					true);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC7_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->super YES) and ANY car sharing
		//The repository has only gasStationDto and gasStationDto2 -> only gasStationDto is present in the returned list
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "super", "null").size(),
					1);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC8_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->super NO) and ANY car sharing
		//The repository has only gasStationDto2 ->the returned list is empty
		
				gasStationService.setUpdateDependability(true);
				Boolean thrown = false;
				
				GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
						"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
				assert(gasStationService.saveGasStation(gasStationDto2)!=null);
				try {
					assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "super", "null").isEmpty(),
							true);
				} catch (GPSDataException e) {
					thrown = true;
				} catch (InvalidGasTypeException e) {
					thrown = true;
				}
				assertEquals(thrown, false);
	}

	@Test
	public void TC9_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->methane YES) and ANY car sharing
		//The repository has only gasStationDto and gasStationDto2 -> only gasStationDto is present in the returned list
				gasStationService.setUpdateDependability(true);
				Boolean thrown = false;
				assert(gasStationService.saveGasStation(gasStationDto)!=null);
				try {
					assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "methane", "null").size(),
							1);
				} catch (GPSDataException e) {
					thrown = true;
				} catch (InvalidGasTypeException e) {
					thrown = true;
				}
				assertEquals(thrown, false);
	}

	@Test
	public void TC10_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->methane NO) and ANY car sharing
		//The repository has only gasStationDto2 ->the returned list is empty
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "methane", "null").isEmpty(),
					true);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC11_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->gas YES) and ANY car sharing
		//The repository has only gasStationDto and gasStationDto2 -> only gasStationDto is present in the returned list
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "gas", "null").size(),
					1);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);

	}

	@Test
	public void TC12_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->gas NO) and ANY car sharing
		//The repository has only gasStationDto2 ->the returned list is empty
				gasStationService.setUpdateDependability(true);
				Boolean thrown = false;
				
				GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
						"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
				assert(gasStationService.saveGasStation(gasStationDto2)!=null);
				try {
					assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "gas", "null").isEmpty(),
							true);
				} catch (GPSDataException e) {
					thrown = true;
				} catch (InvalidGasTypeException e) {
					thrown = true;
				}
				assertEquals(thrown, false);
	}

	@Test
	public void TC13_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->superplus YES) and ANY car sharing
		//The repository has only gasStationDto and gasStationDto2 -> only gasStationDto is present in the returned list
				gasStationService.setUpdateDependability(true);
				Boolean thrown = false;
				assert(gasStationService.saveGasStation(gasStationDto)!=null);
				try {
					assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").size(),
							1);
				} catch (GPSDataException e) {
					thrown = true;
				} catch (InvalidGasTypeException e) {
					thrown = true;
				}
				assertEquals(thrown, false);
	}

	@Test
	public void TC14_getGasStationsWithCoordinates() throws PriceException, GPSDataException {
		// non-null fuel type (VALID->superplus NO) and ANY car sharing
		//The repository has only gasStationDto2 ->the returned list is empty
		gasStationService.setUpdateDependability(true);
		Boolean thrown = false;
		GasStationDto gasStationDto2 = new GasStationDto(null, "Agip", "corso Vittorio", false, false, false, false, false,
				"Car2go", 40.0005, 25.0010, 0.89, 0.89, 1.29, 0.99, 1.00, 1, "2020-05-03", 0.88);
		assert(gasStationService.saveGasStation(gasStationDto2)!=null);
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").isEmpty(),
					true);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
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
	public void TC1_getGasStationByCarSharing() throws PriceException, GPSDataException {
		gasStationService.setUpdateDependability(true);
		assert(gasStationService.saveGasStation(gasStationDto)!=null);
		assertEquals(gasStationService.getGasStationByCarSharing("Enjoy").size(),1);
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
