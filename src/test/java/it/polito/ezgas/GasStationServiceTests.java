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
import it.polito.ezgas.service.*;
import it.polito.ezgas.service.impl.GasStationServiceimpl;

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
	private GasStationDto gasStationDtoMock;
	@Mock
	private GasStation gasStationMock;
	@Mock
	private IdPw credentialsMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private User userMock;
	
	private GasStationService gasStationService;
	
	@Before
	public void setUp() {
		gasStationRepositoryMock=mock(GasStationRepository.class);
		userRepositoryMock=mock(UserRepository.class);
		gasStationConverterMock=mock(GasStationConverter.class);
		gasStationDtoMock=mock(GasStationDto.class);
		gasStationMock=mock(GasStation.class);
		
		gasStationService=new GasStationServiceimpl(gasStationRepositoryMock, gasStationConverterMock, userRepositoryMock);
	}
	
	@Test
	public void TC1_getGasStationById() {
		//try to get a gas station with invalid id
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDtoMock);
		Boolean thrown=false;
		try{
			gasStationService.getGasStationById(-1);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC2_getGasStationById() {
		//try to retrieve a gas station with a non existing id
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDtoMock);
		Boolean thrown=false;
		try{
			assert(gasStationService.getGasStationById(1)==null);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_getGasStationById() {
		//try to retrieve a gas station with an existing id
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDtoMock);
		Boolean thrown=false;
		try{
			assert(gasStationService.getGasStationById(1)!=null);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_saveGasStation() {
		//try to save a gas station which has no fuels -> ERROR?
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gsDto);
		Boolean thrown=false;
		try{
			assert(gasStationService.saveGasStation(gsDto)!=null);
			//Nota: Posso salvare la gas station anche se ho false su tutti i fuel type, ma ho i prezzi?
			//Nota: Posso salvare una gas station con ID nullo o negativo? La lista dovrebbe essere vuota?
//			assert(gasStationService.getAllGasStations().isEmpty());
		}
		catch(GPSDataException e) {
			thrown=true;
		}
		catch(PriceException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC2_saveGasStation() {
		//saving without errors (all prices and no ID)
		GasStation gs = new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(gs);
		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gsDto);
		Boolean thrown=false;
		try{
			assert(gasStationService.saveGasStation(gsDto)!=null);
//			assert(!gasStationService.getAllGasStations().isEmpty());
		}
		catch(GPSDataException e) {
			thrown=true;
		}
		catch(PriceException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC5_saveGasStation() {
		//try to update an already existing gas station
		when(gasStationDtoMock.getGasStationId()).thenReturn(1);
		when(gasStationMock.getGasStationId()).thenReturn(1);
		Boolean thrown=false;
		try{
			assert(gasStationService.saveGasStation(gasStationDtoMock)!=null);
		}
		catch(GPSDataException e) {
			thrown=true;
		}
		catch(PriceException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getAllGasStations() {
		//try to retrieve an empty list
		when(gasStationRepositoryMock.findAll()).thenReturn(new ArrayList<GasStation>());
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(new ArrayList<GasStationDto>());
		assert(gasStationService.getAllGasStations().isEmpty());
	}
	@Test
	public void TC2_getAllGasStations() {
		//try to retrieve a list not empty
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findAll()).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		assert(!gasStationService.getAllGasStations().isEmpty());
	}
	
	@Test
	public void TC1_deleteGasStation() {
		//try to delete a gas station with a negtive id (exception)
		Boolean thrown=false;
		try {
			gasStationService.deleteGasStation(-1);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC2_deleteGasStation() {
		//try to delete an existing gas station
		Boolean thrown=false;
		when(gasStationRepositoryMock.exists(any(Integer.class))).thenReturn(true);
		try {
			assert(gasStationService.deleteGasStation(1)==true);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_deleteGasStation() {
		//try to delete a non existing gas station
		Boolean thrown=false;
		when(gasStationRepositoryMock.exists(any(Integer.class))).thenReturn(false);
//		when(gasStationRepositoryMock.delete(any(Integer.class))).thenThrow(new EmptyResultDataAccessException(0));
		try {
			assert(gasStationService.deleteGasStation(1)==null);
		}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getGasStationsByGasolineType() {
		//try to get a gas station with invalid fuel type
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsByGasolineType("water").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC2_getGasStationsByGasolineType() {
		//try to get a gas station with valid fuel type (Diesel)
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsByGasolineType("diesel").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_getGasStationsByGasolineType() {
		//try to get a gas station with valid fuel type (Super)
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsByGasolineType("super").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC4_getGasStationsByGasolineType() {
		//try to get a gas station with valid fuel type (Methane)
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsByGasolineType("methane").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC5_getGasStationsByGasolineType() {
		//try to get a gas station with valid fuel type (Gas)
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsByGasolineType("gas").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC6_getGasStationsByGasolineType() {
		//try to get a gas station with valid fuel type (SuperPlus)
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByhasDiesel(any(Boolean.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsByGasolineType("superplus").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getGasStationsByProximity() {
		//valid coordinates
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsByProximity(40.0005, 25.0010)==listDto);
		} catch (GPSDataException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC2_getGasStationsByProximity() {
		//invalid coordinates
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsByProximity(999.9999, -999.9999).isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	
	//one test for each fuel type;
	@Test
	public void TC1_getGasStationsWithCoordinates() {
		//null fuel type and null car sharing -> all gas stations
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "null").isEmpty());
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "null")==listDto);
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC2_getGasStationsWithCoordinates() {
		//Select ANY fuel type and car sharing (Enjoy) -> one gas station matches (just inserted)
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Enjoy").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_getGasStationsWithCoordinates() {
		//ANY fuel type and car sharing (Car2GO) -> no gas station matches
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "null", "Car2GO").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC4_getGasStationsWithCoordinates() {
		//INVALID fuel type and ANY car sharing
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "d1esel!!!!", "null");
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC5_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->diesel YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "diesel", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC6_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->diesel NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "diesel", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC7_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->super YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, true, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "super", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC8_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->super NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "super", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC9_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->methane YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "methane", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC10_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->methane NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "methane", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC11_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->gas YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, true, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "gas", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC12_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->diesel NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "gas", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC13_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->superplus YES) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, true, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC14_getGasStationsWithCoordinates() {
		//non-null fuel type (VALID->superplus NO) and ANY car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findBylatBetweenAndLonBetween(any(Double.class), any(Double.class), any(Double.class), any(Double.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithCoordinates(40.0005, 25.0010, "superplus", "null").isEmpty());
		} catch (GPSDataException e) {
			thrown=true;
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getGasStationsWithoutCoordinates() {
		//null fuel type and null car sharing
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("null", "null").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC2_getGasStationsWithoutCoordinates() {
		//null fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithoutCoordinates("null", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_getGasStationsWithoutCoordinates() {
		//SET fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", false, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("diesel", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC4_getGasStationsWithoutCoordinates() {
		//SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("super", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC5_getGasStationsWithoutCoordinates() {
		//SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("methane", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC6_getGasStationsWithoutCoordinates() {
		//SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("gas", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC7_getGasStationsWithoutCoordinates() {
		//SET fuel type and null car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(gasStationService.getGasStationsWithoutCoordinates("superplus", "Enjoy").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC8_getGasStationsWithoutCoordinates() {
		//SET fuel type and SET car sharing
		GasStation gs = new GasStation("ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		GasStationDto gsDto = new GasStationDto(null, "ENI", "corso Duca", true, false, false, false, false, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, "1590345000", 0.88);
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gs);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gsDto);
		Boolean thrown=false;
		try {
			assert(!gasStationService.getGasStationsWithoutCoordinates("diesel", "null").isEmpty());
		} catch (InvalidGasTypeException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getGasStationByCarSharing() {
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findByCarSharing(any(String.class))).thenReturn(list);
		when(gasStationConverterMock.toGasStationDtoList(any(List.class))).thenReturn(listDto);
		assert(gasStationService.getGasStationByCarSharing("Enjoy")==listDto);
	}
	
	@Test
	public void TC1_setReport() {
		//existing user sets all prices
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationMock.getHasDiesel()).thenReturn(true);
		when(gasStationMock.getHasGas()).thenReturn(true);
		when(gasStationMock.getHasMethane()).thenReturn(true);
		when(gasStationMock.getHasSuper()).thenReturn(true);
		when(gasStationMock.getHasSuperPlus()).thenReturn(true);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(userMock);
		Boolean thrown=false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown=true;
		} catch (PriceException e) {
			thrown=true;
		} catch (InvalidUserException e) {
			thrown=true;
		}
		assert(thrown=false);
	}
	@Test
	public void TC2_setReport() {
		//invalid user
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationMock.getHasDiesel()).thenReturn(true);
		when(gasStationMock.getHasGas()).thenReturn(true);
		when(gasStationMock.getHasMethane()).thenReturn(true);
		when(gasStationMock.getHasSuper()).thenReturn(true);
		when(gasStationMock.getHasSuperPlus()).thenReturn(true);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(userMock);
		Boolean thrown=false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, -1);
		} catch (InvalidGasStationException e) {
			thrown=true;
		} catch (PriceException e) {
			thrown=true;
		} catch (InvalidUserException e) {
			thrown=true;
		}
		assert(thrown=true);
	}
	@Test
	public void TC4_setReport() {
		//non existing user
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationMock.getHasDiesel()).thenReturn(true);
		when(gasStationMock.getHasGas()).thenReturn(true);
		when(gasStationMock.getHasMethane()).thenReturn(true);
		when(gasStationMock.getHasSuper()).thenReturn(true);
		when(gasStationMock.getHasSuperPlus()).thenReturn(true);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
		Boolean thrown=false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown=true;
		} catch (PriceException e) {
			thrown=true;
		} catch (InvalidUserException e) {
			thrown=true;
		}
		assert(thrown=false);
	}
	@Test
	public void TC5_setReport() {
		//non-esisting gas station
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
		when(gasStationMock.getHasDiesel()).thenReturn(true);
		when(gasStationMock.getHasGas()).thenReturn(true);
		when(gasStationMock.getHasMethane()).thenReturn(true);
		when(gasStationMock.getHasSuper()).thenReturn(true);
		when(gasStationMock.getHasSuperPlus()).thenReturn(true);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(userMock);
		Boolean thrown=false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown=true;
		} catch (PriceException e) {
			thrown=true;
		} catch (InvalidUserException e) {
			thrown=true;
		}
		assert(thrown=false);
	}
	@Test
	public void TC6_setReport() {
		//gas station with no fuels
		List<GasStation> list=new ArrayList<GasStation>();
		list.add(gasStationMock);
		List<GasStationDto> listDto=new ArrayList<GasStationDto>();
		listDto.add(gasStationDtoMock);
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
		when(gasStationMock.getHasDiesel()).thenReturn(false);
		when(gasStationMock.getHasGas()).thenReturn(false);
		when(gasStationMock.getHasMethane()).thenReturn(false);
		when(gasStationMock.getHasSuper()).thenReturn(false);
		when(gasStationMock.getHasSuperPlus()).thenReturn(false);
		when(userRepositoryMock.findOne(any(Integer.class))).thenReturn(userMock);
		Boolean thrown=false;
		try {
			gasStationService.setReport(1, 0.99, 0.98, 0.97, 0.96, 0.95, 1);
		} catch (InvalidGasStationException e) {
			thrown=true;
		} catch (PriceException e) {
			thrown=true;
		} catch (InvalidUserException e) {
			thrown=true;
		}
		assert(thrown=false);
	}
	
}
