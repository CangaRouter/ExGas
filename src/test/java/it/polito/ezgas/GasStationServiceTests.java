package it.polito.ezgas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import exception.InvalidGasStationException;
import exception.InvalidLoginDataException;
import org.mockito.Mock;

import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.service.*;
import it.polito.ezgas.service.impl.GasStationServiceimpl;
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
	
	@Before
	public void setUp() {
		gasStationRepositoryMock =mock(GasStationRepository.class);
		gasStationConverterMock=mock(GasStationConverter.class);
		gasStationDtoMock=mock(GasStationDto.class);
		gasStationMock=mock(GasStation.class);
	}
	
	@Test
	public void TC1_getGasStationById() {
		//Test: try to get a gas station with negative id
		GasStationService gasStationService=new GasStationServiceimpl();
		Boolean thrown=false;
		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(null);
//		GasStation  = gasStationRepository.findOne(gasStationId);
		try{gasStationService.getGasStationById(-1);}
		catch(InvalidGasStationException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
//	@Test
//	public void TC2_getGasStationById() {
//		//Test: try to retrieve a gas station with a non existing id
//		Boolean thrown=false;
//		when(gasStationRepositoryMock.exists(any(Integer.class))).thenReturn(false);
//		GasStationService gasStationService=new GasStationServiceimpl();
//		try{assert(gasStationService.getGasStationById(1)==null);}
//		catch(InvalidGasStationException e) {
//			thrown=true;
//		}
//		assert(thrown==false);
//	}
//	@Test
//	public void TC3_getGasStationById() {
//		//Test: try to retrieve an GasStation with an existing id
//		Boolean thrown=false;
//		when(gasStationRepositoryMock.exists(any(Integer.class))).thenReturn(true);
//		when(gasStationRepositoryMock.findOne(any(Integer.class))).thenReturn(gasStationMock);
//		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(gasStationDtoMock);
//		GasStationService gasStationService=new GasStationServiceimpl();
//		try{assert(gasStationService.getGasStationById(1)!=null);}
//		catch(InvalidGasStationException e) {
//			thrown=true;
//		}
//		assert(thrown==false);
//	}
//	
//	@Test
//	public void TC1_saveGasStationTest() {
//		//Test: saving without errors
//		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		GasStationService GasStationService=new GasStationServiceimpl();
//		GasStationDto u=new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88);
//		assert(GasStationService.saveGasStation(u)!=null);
//	}
//	
//	@Test
//	public void TC2_saveGasStationTest() {
//		//Test: try to save an already existing GasStation
//		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		when(gasStationConverterMock.toGasStation(any(GasStationDto.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		when(gasStationConverterMock.toGasStationDto(any(GasStation.class))).thenReturn(new GasStation("ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88));
//		GasStationService GasStationService=new GasStationServiceimpl(gasStationRepositoryMock,GasStationConverterMock);
//		GasStationDto u=new GasStationDto(null, "ENI", "corso Duca", true, true, true, true, true, "Enjoy", 40.0005, 25.0010, 0.99, 0.99, 0.99, 0.99, 0.99, 1, 1590345000, 0.88);
//		assert(GasStationService.saveGasStation(u)==null);
//	}
//	
//	@Test
//	public void TC3_saveGasStationTest() {
//		//Test: update a GasStation
//		when(gasStationDtoMock.getGasStationId()).thenReturn(2);
//		when(gasStationMock.getGasStationId()).thenReturn(2);
//		when(gasStationRepositoryMock.saveAndFlush(any(GasStation.class))).thenReturn(GasStationMock);
//		GasStationService GasStationService=new GasStationServiceimpl(gasStationRepositoryMock,GasStationConverterMock);
//		assert(GasStationService.saveGasStation(gasStationDtoMock)!=null);
//	}
//	@Test
//	public void TC4_saveGasStationTest() {
//		//Test: try to update a non existing or a different GasStation (different GasStationIds)
//		when(gasStationDtoMock.getGasStationId()).thenReturn(2);
//		when(gasStationMock.getGasStationId()).thenReturn(1);
//		GasStationService GasStationService=new GasStationServiceimpl(gasStationRepositoryMock,GasStationConverterMock);
//		assert(GasStationService.saveGasStation(gasStationDtoMock)!=null);
//	}
}
