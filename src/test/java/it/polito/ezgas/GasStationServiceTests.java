package it.polito.ezgas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;

import org.mockito.Mock;

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
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)

public class GasStationServiceTests {

	@Mock
	private GasStationRepository gasStationRepositoryMock;
	@Mock
	private GasStationConverter gasStationConverterMock;
	@Mock
	private IdPw credentialsMock;
	@Mock
	private UserRepository userRepositoryMock;

	private GasStationServiceimpl gasStationService;

	private GasStation gasStation = new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005,
			25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
	private GasStationDto gasStationDto = new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true,
			"Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
	private User user = new User("nome", "password", "email", 0);

	@Before
	public void setUp() {
		gasStationRepositoryMock = mock(GasStationRepository.class);
		userRepositoryMock = mock(UserRepository.class);
		gasStationConverterMock = mock(GasStationConverter.class);
		gasStationService = new GasStationServiceimpl(gasStationRepositoryMock, gasStationConverterMock,
				userRepositoryMock);
		gasStationService.setUpdateDependability(true);

	}

	@Test
	public void TC1_getGasStationById() {
		// try to get a gas station with invalid id
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStation);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDto);
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
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationById(1), null);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationById() {
		// try to retrieve a gas station with an existing id
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStation);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDto);
		Boolean thrown = false;
		try {
			assertNotNull(gasStationService.getGasStationById(1));
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC1_saveGasStation() {
		// try to save a gas station which has no fuels -> ERROR?
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", false, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gsDto);
		Boolean thrown = false;
		try {
			assertNotNull(gasStationService.saveGasStation(gsDto));
			// Nota: Posso salvare la gas station anche se ho false su tutti i fuel type, ma
			// ho i prezzi?
			// Nota: Posso salvare una gas station con ID nullo o negativo? La lista
			// dovrebbe essere vuota?
//			assertEquals(gasStationService.getAllGasStations().isEmpty());
		} catch (GPSDataException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC2_saveGasStation() {
		// saving without errors (all prices and no ID)
		GasStation gs = new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gsDto);
		Boolean thrown = false;
		try {
			assertNotNull(gasStationService.saveGasStation(gsDto));
//			assertEquals(!gasStationService.getAllGasStations().isEmpty());
		} catch (GPSDataException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC5_saveGasStation() {
		// try to update an already existing gas station
		GasStationDto myDto = new GasStationDto(1, gasStationDto.getGasStationName(),
				gasStationDto.getGasStationAddress(), gasStationDto.getHasDiesel(), gasStationDto.getHasSuper(),
				gasStationDto.getHasSuperPlus(), gasStationDto.getHasGas(), gasStationDto.getHasMethane(),
				gasStationDto.getCarSharing(), gasStationDto.getLat(), gasStationDto.getLon(),
				gasStationDto.getDieselPrice(), gasStationDto.getSuperPrice(), gasStationDto.getSuperPlusPrice(),
				gasStationDto.getGasPrice(), gasStationDto.getMethanePrice(), gasStationDto.getReportUser(),
				gasStationDto.getReportTimestamp(), gasStationDto.getReportDependability());
		Boolean thrown = false;
		try {
			assertNotNull(gasStationService.saveGasStation(myDto));
		} catch (GPSDataException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC1_getAllGasStations() {
		// try to retrieve an empty list
		when(gasStationRepositoryMock.findAll()).thenReturn(new ArrayList<GasStation>());
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(new ArrayList<GasStationDto>());
		assertEquals(gasStationService.getAllGasStations().isEmpty(), true);
	}

	@Test
	public void TC2_getAllGasStations() {
		// try to retrieve a list not empty
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findAll()).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		assertEquals(gasStationService.getAllGasStations().isEmpty(), false);
	}

	@Test
	public void TC1_deleteGasStation() {
		// try to delete a gas station with a negtive id (exception)
		Boolean thrown = false;
		try {
			gasStationService.deleteGasStation(-1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC2_deleteGasStation() {
		// try to delete an existing gas station
		Boolean thrown = false;
		when(gasStationRepositoryMock.exists(any(Integer.class))).thenReturn(true);
		try {
			assertEquals(gasStationService.deleteGasStation(1), true);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_deleteGasStation() {
		// try to delete a non existing gas station
		Boolean thrown = false;
		doThrow(EmptyResultDataAccessException.class).when(gasStationRepositoryMock).delete(1);
		try {
			assertEquals(gasStationService.deleteGasStation(1), null);
		} catch (InvalidGasStationException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC1_getGasStationsByGasolineType() {
		// try to get a gas station with invalid fuel type
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("water").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC2_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Diesel)
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("diesel").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Super)
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByhasSuper(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("super").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC4_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Methane)
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByhasMethane(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("methane").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC5_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (Gas)
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByhasGas(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("gas").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC6_getGasStationsByGasolineType() {
		// try to get a gas station with valid fuel type (SuperPlus)
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByhasSuperPlus(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByGasolineType("superplus").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC1_getGasStationsByProximity() {
		// valid coordinates
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByProximity(40.0005, 25.0010), listDto);
		} catch (GPSDataException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC2_getGasStationsByProximity() {
		// invalid coordinates
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsByProximity(999.9999, -999.9999).isEmpty(), false);
		} catch (GPSDataException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	// one test for each fuel type;
	@Test
	public void TC1_getGasStationsWithCoordinates() {
		// null fuel type and null car sharing -> all gas stations
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "null").isEmpty(),
					false);
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "null"), listDto);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC2_getGasStationsWithCoordinates() {
		// Select ANY fuel type and car sharing (Enjoy) -> one gas station matches (just
		// inserted)
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Enjoy").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationsWithCoordinates() {
		// ANY fuel type and car sharing (Car2GO) -> no gas station matches
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Car2GO").isEmpty(),
					true);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC4_getGasStationsWithCoordinates() {
		// INVALID fuel type and ANY car sharing
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gasStation));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
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
	public void TC5_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
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
	public void TC6_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
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
	public void TC7_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->super YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, true, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "super", "null").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC8_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->super NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
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
	public void TC9_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->methane YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, true, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "methane", "null").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC10_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->methane NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
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
	public void TC11_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->gas YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, true, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "gas", "null").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC12_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->diesel NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
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
	public void TC13_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->superplus YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, true, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(
					gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").isEmpty(),
					false);
		} catch (GPSDataException e) {
			thrown = true;
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC14_getGasStationsWithCoordinates() {
		// non-null fuel type (VALID->superplus NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class),
				any(Double.class), any(Double.class))).thenReturn(list);
		list.remove(list.indexOf(gs));
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		Boolean thrown = false;
		try {
			assertEquals(
					gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").isEmpty(),
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
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		Boolean thrown = false;
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("null", "null").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC2_getGasStationsWithoutCoordinates() {
		// null fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByCarSharing(any(String.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("null", "Enjoy").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC3_getGasStationsWithoutCoordinates() {
		// SET fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005,
				25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", false, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasDieselAndCarSharing(any(Boolean.class), any(String.class)))
				.thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("diesel", "Enjoy").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC4_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasSuperAndCarSharing(any(Boolean.class), any(String.class)))
				.thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("super", "Enjoy").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC5_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasMethaneAndCarSharing(any(Boolean.class), any(String.class)))
				.thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("methane", "Enjoy").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC6_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasDieselAndCarSharing(any(Boolean.class), any(String.class)))
				.thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("gas", "Enjoy").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC7_getGasStationsWithoutCoordinates() {
		// SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasSuperPlusAndCarSharing(any(Boolean.class), any(String.class)))
				.thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("   super plus", "Enjoy").isEmpty(), true);
		} catch (InvalidGasTypeException e) {
			thrown = true;
			System.out.println(e.getMessage());

		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC8_getGasStationsWithoutCoordinates() {
		// SET fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010,
				0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy",
				40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown = false;
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		try {
			assertEquals(gasStationService.getGasStationsWithoutCoordinates("diesel", "null").isEmpty(), false);
		} catch (InvalidGasTypeException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC1_getGasStationByCarSharing() {
		List<GasStation> list = new ArrayList<GasStation>();
		list.add(gasStation);
		List<GasStationDto> listDto = new ArrayList<GasStationDto>();
		listDto.add(gasStationDto);
		when(gasStationRepositoryMock.findByCarSharing(any(String.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(list)).thenReturn(listDto);
		assertEquals(gasStationService.getGasStationByCarSharing("Enjoy"), listDto);
	}

	@Test
	public void TC1_setReport() {
		// existing user sets all prices
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStation);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(user);
		Boolean thrown = false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		} catch (InvalidUserException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

	@Test
	public void TC2_setReport() {
		// invalid user
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStation);
		when(userRepositoryMock.findOne(-1)).thenReturn(null);
		Boolean thrown = false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, -1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		} catch (InvalidUserException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC4_setReport() {
		// non existing user
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStation);
		when(userRepositoryMock.findOne(1)).thenReturn(null);
		Boolean thrown = false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		} catch (InvalidUserException e) {
			thrown = true;
		}
		assertEquals(thrown, true);
	}

	@Test
	public void TC5_setReport() {
		// non-esisting gas station
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(user);
		Boolean thrown = false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown = true;
		} catch (PriceException e) {
			thrown = true;
		} catch (InvalidUserException e) {
			thrown = true;
		}
		assertEquals(thrown, false);
	}

}