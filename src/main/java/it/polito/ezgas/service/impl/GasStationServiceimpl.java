package it.polito.ezgas.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import exception.GPSDataException;
import exception.InvalidGasStationException;
import exception.InvalidGasTypeException;
import exception.InvalidUserException;
import exception.PriceException;
import it.polito.ezgas.converter.GasStationConverter;
import it.polito.ezgas.dto.GasStationDto;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.GasStationService;

/**
 * Created by softeng on 27/4/2020.
 */
@Service
public class GasStationServiceimpl implements GasStationService {
	@Autowired
	GasStationRepository gasStationRepository;
	@Autowired
	GasStationConverter gasStationConverter;
	@Autowired
	UserRepository userRepository;

	@Override
	public GasStationDto getGasStationById(Integer gasStationId) throws InvalidGasStationException {
		checkId(gasStationId);
		return gasStationConverter.toGasStationDto(gasStationRepository.findOne(gasStationId));
	}

	@Override
	public GasStationDto saveGasStation(GasStationDto gasStationDto) throws PriceException, GPSDataException {
		List<Double> prices= new ArrayList<Double>();
		if (gasStationDto.getHasDiesel()) {
			prices.add(gasStationDto.getDieselPrice());
		}
		if (gasStationDto.getHasMethane()) {
			prices.add(gasStationDto.getMethanePrice());
		}
		if (gasStationDto.getHasGas()) {
			prices.add(gasStationDto.getGasPrice());
		}
		if (gasStationDto.getHasSuper()) {
			prices.add(gasStationDto.getSuperPrice());
		}
		if (gasStationDto.getHasSuperPlus()) {
			prices.add(gasStationDto.getSuperPlusPrice());
		}
		checkPriceList(prices);
		System.out.println(gasStationDto.getLat() + " " + gasStationDto.getLon());

		return gasStationConverter
				.toGasStationDto(gasStationRepository.saveAndFlush(gasStationConverter.toGasStation(gasStationDto)));
	}

	@Override
	public List<GasStationDto> getAllGasStations() {

		return gasStationConverter.toGasStationDtoList(gasStationRepository.findAll());
	}

	@Override
	public Boolean deleteGasStation(Integer gasStationId) throws InvalidGasStationException {
		this.checkId(gasStationId);
		try {
			gasStationRepository.delete(gasStationId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return true;
	}

	@Override
	public List<GasStationDto> getGasStationsByGasolineType(String gasolinetype) throws InvalidGasTypeException {
		gasolinetype = gasolinetype.toLowerCase().trim();
		switch (gasolinetype) {
		case "diesel":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasDiesel(true));
		case "gasoline":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuper(true));
		case "methane":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasMethane(true));
		case "lpg":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasGas(true));
		case "premiumgasoline":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuperPlus(true));
		default:
			throw new InvalidGasTypeException("invalid gas type");
		}
	}

	@Override
	public List<GasStationDto> getGasStationsByProximity(double lat, double lon) throws GPSDataException {
		// System.out.println("\n\n\n" + lat + " " + lon + "\n\n\n\n");
		// TODO
		return new ArrayList<GasStationDto>();
	}

	@Override
	public List<GasStationDto> getGasStationsWithCoordinates(double lat, double lon, String gasolinetype,
			String carsharing) throws InvalidGasTypeException, GPSDataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GasStationDto> getGasStationsWithoutCoordinates(String gasolinetype, String carsharing)
			throws InvalidGasTypeException {
		if(carsharing==null && gasolinetype!=null) {
			return getGasStationsByGasolineType(gasolinetype);
		}
		if(carsharing!=null && gasolinetype==null) {
			return getGasStationByCarSharing(carsharing);
		}
		gasolinetype = gasolinetype.toLowerCase().trim();
		switch (gasolinetype) {
		case "diesel":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasDieselAndCarSharing(true, carsharing));
		case "gasoline":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuperAndCarSharing(true, carsharing));
		case "methane":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasMethaneAndCarSharing(true, carsharing));
		case "lpg":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasGasAndCarSharing(true, carsharing));
		case "premiumgasoline":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuperPlusAndCarSharing(true, carsharing));
		default:
			throw new InvalidGasTypeException("invalid gas type");
		}
	}

	@Override
	public void setReport(Integer gasStationId, double dieselPrice, double superPrice, double superPlusPrice,
			double gasPrice, double methanePrice, Integer userId)
			throws InvalidGasStationException, PriceException, InvalidUserException {
		checkId(gasStationId);
		GasStation gasStation= gasStationRepository.findOne(gasStationId);
		List<Double> prices= new ArrayList<Double>();
		if (gasStation.getHasDiesel()) {
			prices.add(gasStation.getDieselPrice());
			gasStation.setDieselPrice(dieselPrice);
		}
		if (gasStation.getHasMethane()) {
			prices.add(gasStation.getMethanePrice());
			gasStation.setMethanePrice(methanePrice);
		}
		if (gasStation.getHasGas()) {
			prices.add(gasStation.getGasPrice());
			gasStation.setGasPrice(superPrice);
		}
		if (gasStation.getHasSuper()) {
			prices.add(gasStation.getSuperPrice());
			gasStation.setSuperPrice(superPrice);
		}
		if (gasStation.getHasSuperPlus()) {
			prices.add(gasStation.getSuperPlusPrice());
			gasStation.setSuperPlusPrice(superPlusPrice);
		}
		checkPriceList(prices);
		User user = userRepository.findOne(userId);
		if(user==null) {
			throw new InvalidUserException("User id non valid");
		}
		gasStation.setUser(user);
		gasStationRepository.saveAndFlush(gasStation);
	}

	@Override
	public List<GasStationDto> getGasStationByCarSharing(String carSharing) {
		
		return gasStationConverter.toGasStationDtoList(gasStationRepository.findByCarSharing(carSharing));
	}

	private void checkId(Integer gasStationId) throws InvalidGasStationException {
		if (gasStationId <= 0) {
			throw new InvalidGasStationException("No gas station with this ID");
		}
	}

	private void checkPriceList(List<Double> prices) throws PriceException {
		for (double price : prices) {
			if (price < 0) {
				throw new PriceException("Negative price is not supported");
			}
		}
	}

}
