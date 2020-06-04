package it.polito.ezgas.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
@EnableScheduling
@Service
public class GasStationServiceimpl implements GasStationService {
	private static final double LON_DIFF = 0.012733784;
	private static final double LAT_DIFF = 0.0089977776;
//	@Autowired
//	GasStationRepository gasStationRepository;
//	@Autowired
//	GasStationConverter gasStationConverter;
//	@Autowired
//	UserRepository userRepository;

	private boolean updateDependability = false;
	
	private GasStationRepository gasStationRepository;
	private GasStationConverter gasStationConverter;
	private UserRepository userRepository;
	
	public GasStationServiceimpl (GasStationRepository gasStationRepository, GasStationConverter gasStationConverter, UserRepository userRepository) {
		this.gasStationRepository = gasStationRepository;
		this.gasStationConverter = gasStationConverter;
		this.userRepository = userRepository;
	}
	
	@Override
	public GasStationDto getGasStationById(Integer gasStationId) throws InvalidGasStationException {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		this.checkId(gasStationId);
		GasStation gasStation = gasStationRepository.findOne(gasStationId);
		if (gasStation == null) {
			return null;
		}
		return gasStationConverter.toGasStationDto(gasStation);
	}

	@Override
	public GasStationDto saveGasStation(GasStationDto gasStationDto) throws PriceException, GPSDataException {
			this.checkCoordinates(gasStationDto.getLat(), gasStationDto.getLon());
			List<Double> prices = new ArrayList<Double>();
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
			this.checkPriceList(prices, false);
			if (gasStationDto.getGasStationId() == null) {
			return gasStationConverter.toGasStationDto(
					gasStationRepository.saveAndFlush(gasStationConverter.toGasStation(gasStationDto)));
		} 
			gasStationRepository.saveAndFlush(gasStationConverter.toGasStation(gasStationDto));
			return gasStationDto;
	}

	@Override
	public List<GasStationDto> getAllGasStations() {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}

		return gasStationConverter.toGasStationDtoList(gasStationRepository.findAll());
	}

	public boolean isUpdateDependability() {
		return updateDependability;
	}

	public void setUpdateDependability(boolean updateDependability) {
		this.updateDependability = updateDependability;
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
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+","");
		switch (gasolinetype) {
		case "diesel":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasDiesel(true));
		case "super":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuper(true));
		case "methane":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasMethane(true));
		case "gas":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasGas(true));
		case "superplus":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasSuperPlus(true));
		default:
			throw new InvalidGasTypeException("invalid fuel type " + gasolinetype);
		}
	}

	@Override
	public List<GasStationDto> getGasStationsByProximity(double lat, double lon) throws GPSDataException {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		this.checkCoordinates(lat, lon);
		return gasStationConverter.toGasStationDtoList(gasStationRepository
				.findBylatBetweenAndLonBetween(lat - LAT_DIFF, lat + LAT_DIFF, lon - LON_DIFF, lon + LON_DIFF));
	}

	@Override
	public List<GasStationDto> getGasStationsWithCoordinates(double lat, double lon, String gasolinetype,
			String carsharing) throws InvalidGasTypeException, GPSDataException {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		this.checkCoordinates(lat, lon);
		List<GasStation> gasStationList = gasStationRepository.findBylatBetweenAndLonBetween(lat - LAT_DIFF,
				lat + LAT_DIFF, lon - LON_DIFF, lon + LON_DIFF);
		List<GasStation> gasStationListNew= new ArrayList<>(gasStationList);
		if (!carsharing.equals("null")) {
			for (GasStation gs : gasStationList) {
				if (!gs.getCarSharing().equals(carsharing)) {
					gasStationListNew.remove(gs);
				}
			}
		}
		if (!gasolinetype.equals("null")) {
			gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+","");
			switch (gasolinetype) {
			case "diesel":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasDiesel()) {
						gasStationListNew.remove(gs);
						
					}
				}
				break;
			case "super":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasSuper()) {
						gasStationListNew.remove(gs);
					}
				}
				break;
			case "methane":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasMethane()) {
						gasStationListNew.remove(gs);
					}
				}
				break;
			case "gas":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasGas()) {
						gasStationListNew.remove(gs);
					}
				}
				break;
			case "superplus":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasSuperPlus()) {
						gasStationListNew.remove(gs);
					}
				}
				break;
			default:
				throw new InvalidGasTypeException("invalid gas type " + gasolinetype);
			}
		}
		return gasStationConverter.toGasStationDtoList(gasStationListNew);
	}

	@Override
	public List<GasStationDto> getGasStationsWithoutCoordinates(String gasolinetype, String carsharing)
			throws InvalidGasTypeException {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		if (carsharing.equals("null") && !gasolinetype.equals("null")) {
			return getGasStationsByGasolineType(gasolinetype);
		}
		if (!carsharing.equals("null") && gasolinetype.equals("null")) {
			return getGasStationByCarSharing(carsharing);
		}
		gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+","");
		switch (gasolinetype) {
		case "diesel":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasDieselAndCarSharing(true, carsharing));
		case "super":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasSuperAndCarSharing(true, carsharing));
		case "methane":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasMethaneAndCarSharing(true, carsharing));
		case "gas":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasGasAndCarSharing(true, carsharing));
		case "superplus":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasSuperPlusAndCarSharing(true, carsharing));
		default:
			throw new InvalidGasTypeException("invalid gas type " + gasolinetype);
		}
	}

	@Override
	public void setReport(Integer gasStationId, double dieselPrice, double superPrice, double superPlusPrice,
			double gasPrice, double methanePrice, Integer userId)
			throws InvalidGasStationException, PriceException, InvalidUserException {
		this.checkId(gasStationId);
		GasStation gasStation = gasStationRepository.findOne(gasStationId);
		if (gasStation != null) {

			List<Double> prices = new ArrayList<Double>();
			if (gasStation.getHasDiesel()) {
				prices.add(dieselPrice);
				gasStation.setDieselPrice(dieselPrice);
			}
			if (gasStation.getHasMethane()) {
				prices.add(methanePrice);
				gasStation.setMethanePrice(methanePrice);
			}
			if (gasStation.getHasGas()) {
				prices.add(gasPrice);
				gasStation.setGasPrice(gasPrice);
			}
			if (gasStation.getHasSuper()) {
				prices.add(superPrice);
				gasStation.setSuperPrice(superPrice);
			}
			if (gasStation.getHasSuperPlus()) {
				prices.add(superPlusPrice);
				gasStation.setSuperPlusPrice(superPlusPrice);
			}
			this.checkPriceList(prices, true);
			User user = userRepository.findOne(userId);
			
			if (user == null|| userId<0) {
				throw new InvalidUserException("User id non valid " + userId);
			}
			 DateFormat formatter = new SimpleDateFormat("MM-dd-YYYY");
			gasStation.setUser(user);
			gasStation.setReportTimestamp(formatter.format(new Date(System.currentTimeMillis())));
			gasStation.setReportDependability((50 * (user.getReputation() + 5) / 10) + 50);
			gasStation.setReportUser(user.getUserId());
			gasStationRepository.saveAndFlush(gasStation);
		}else {
			throw new InvalidGasStationException("Invalida ga station " + gasStationId);
		}
	}

	@Override
	public List<GasStationDto> getGasStationByCarSharing(String carSharing) {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		return gasStationConverter.toGasStationDtoList(gasStationRepository.findByCarSharing(carSharing));
	}

	private void checkId(Integer gasStationId) throws InvalidGasStationException {
		if (gasStationId <= 0) {
			throw new InvalidGasStationException("No gas station with this ID " + gasStationId);
		}
	}

	private void checkPriceList(List<Double> prices, boolean report) throws PriceException {
		for (double price : prices) {
			if (!report) {
				if (price < -1) {
					throw new PriceException("Negative price is not valid");
				}
			} else {
				if (price < 0) {

					throw new PriceException("Negative price is not valid");
				}
			}
		}
	}

	private void checkCoordinates(double lat, double lon) throws GPSDataException {
		if (!String.valueOf(lat).matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?)$")) {
			throw new GPSDataException("Invalid latitude");
		}
		if (!String.valueOf(lon).matches("^[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")) {
			throw new GPSDataException("Invalid longitude");
		}
	}

	@Scheduled(cron = "0 0 0 * * *")
	private void calculateDependability() {
		for (GasStation gs : gasStationRepository.findAll()) {
			if(gs.getReportUser()!=null && gs.getReportUser()>0 && gs.getUser()!=null) {
			LocalDate oldDate = LocalDate.parse(gs.getReportTimestamp());
			LocalDate newDate = LocalDate.now();
			if (oldDate.getYear() == newDate.getYear()) {
				if (newDate.getDayOfYear() - oldDate.getDayOfYear() > 7) {
					gs.setReportDependability(Math.round(50 * (gs.getUser().getReputation() + 5) / 10));
					gasStationRepository.saveAndFlush(gs);
				} else {
					gs.setReportDependability(Math.round(50 * (gs.getUser().getReputation() + 5) / 10
							+ 50 * (1 - ((double) (newDate.getDayOfYear() - oldDate.getDayOfYear()) / 7))));
					gasStationRepository.saveAndFlush(gs);
				}
			} else {
				if ((newDate.getDayOfYear() + 365 + (1 - (oldDate.getYear() % 4))) - oldDate.getDayOfYear() > 7) {
					gs.setReportDependability(Math.round(50 * (gs.getUser().getReputation() + 5) / 10));
					gasStationRepository.saveAndFlush(gs);

				} else {
					gs.setReportDependability(Math.round((50 * (gs.getUser().getReputation() + 5) / 10
							+ 50 * (1 - ((double) (newDate.getDayOfYear() + 365 + (1 - (oldDate.getYear() % 4)))
									- oldDate.getDayOfYear()) / 7))));
					gasStationRepository.saveAndFlush(gs);
				}
			}
			}
		}
	}
}
