package it.polito.ezgas.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
import exception.InvalidCarSharingException;
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
	private static final int DAYS_IN_YEAR = 365;
	private static final int OBSOLESCENCE_50 = 50;
	private static final double LON_DIFF  = 0.012733784;
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

	public GasStationServiceimpl(GasStationRepository gasStationRepository, GasStationConverter gasStationConverter,
			UserRepository userRepository) {
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
		if (gasStationDto.getCarSharing().equals("null")) {
			gasStationDto.setCarSharing(null);
		}
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
		if (gasStationDto.getHasPremiumDiesel()) {
			prices.add(gasStationDto.getPremiumDieselPrice());
		}
		this.checkPriceList(prices);
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
		gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+", "");
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
		case "premiumdiesel":
			return gasStationConverter.toGasStationDtoList(gasStationRepository.findByhasPremiumDiesel(true));
		default:
			throw new InvalidGasTypeException("invalid fuel type " + gasolinetype);
		}
	}

	@Override
	public List<GasStationDto> getGasStationsByProximity(double lat, double lon) throws GPSDataException {
			return this.getGasStationsByProximity(lat, lon,1);
	}
	
	@Override
	public 	List<GasStationDto> getGasStationsByProximity(double lat, double lon, int radius) throws GPSDataException{
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		this.checkCoordinates(lat, lon);
		if(radius <= 0){
			radius = 1;
		}
		return gasStationConverter.toGasStationDtoList(gasStationRepository
				.findBylatBetweenAndLonBetween(lat - (LAT_DIFF * radius) , lat + (LAT_DIFF * radius) , lon - (LON_DIFF * radius) , lon + (LON_DIFF * radius) ));
	}
	

	@Override
	public List<GasStationDto> getGasStationsWithCoordinates(double lat, double lon, int radius, String gasolinetype,
			String carsharing) throws InvalidGasTypeException, GPSDataException, InvalidCarSharingException {
	   if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		this.checkCoordinates(lat, lon);
		this.checkCarSharing(carsharing);
		if(radius <= 0){
			radius = 1;
		}
		List<GasStation> gasStationList = gasStationRepository.findBylatBetweenAndLonBetween(lat - (LAT_DIFF * radius),
				lat + (LAT_DIFF * radius), lon - (LON_DIFF * radius), lon + (LON_DIFF * radius));
		List<GasStation> gasStationListNew = new ArrayList<>(gasStationList);
		if (!carsharing.equals("null")) {
			for (GasStation gs : gasStationList) {
				if (gs.getCarSharing() == null || !gs.getCarSharing().equals(carsharing)) {
					gasStationListNew.remove(gs);
				}
			}
		}
		if (!gasolinetype.equals("null")) {
			gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+", "");
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
			case "premiumdiesel":
				for (GasStation gs : gasStationList) {
					if (!gs.getHasPremiumDiesel()) {
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
			throws InvalidGasTypeException, InvalidCarSharingException {
		if (!updateDependability) {
			this.calculateDependability();
			this.updateDependability = true;
		}
		checkCarSharing(carsharing);

		if (carsharing.equals("null") && !gasolinetype.equals("null")) {
			return getGasStationsByGasolineType(gasolinetype);
		}
		if (!carsharing.equals("null") && gasolinetype.equals("null")) {
			return getGasStationByCarSharing(carsharing);
		}
		gasolinetype = gasolinetype.toLowerCase().replaceAll("\\s+", "");
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
		case "premiumdiesel":
			return gasStationConverter
					.toGasStationDtoList(gasStationRepository.findByhasPremiumDieselAndCarSharing(true, carsharing));
		default:
			throw new InvalidGasTypeException("invalid gas type " + gasolinetype);
		}
	}

	
	@Override
	public void setReport(Integer gasStationId, Double dieselPrice, Double superPrice, Double superPlusPrice,
			Double gasPrice, Double methanePrice, Double premiumDieselPrice, Integer userId)
			throws InvalidGasStationException, PriceException, InvalidUserException {
		this.checkId(gasStationId);
		DateFormat formatter = new SimpleDateFormat("MM-dd-YYYY");
		DateFormat newFormatter = new SimpleDateFormat("YYYY-MM-dd");
		GasStation gasStation = gasStationRepository.findOne(gasStationId);
		if (gasStation != null) {
			User user1 = userRepository.findOne(userId);
			if (user1 == null || userId < 0) {
				throw new InvalidUserException("User id non valid " + userId);
			}
			if (gasStation != null) {
				LocalDate newDate = LocalDate.now(); // current date, the one of user1
				User user2 = gasStation.getUser();
				if (user2 != null) { // otherwise gasStation has never had a report
					LocalDate oldDate;
					try {
						oldDate = LocalDate.parse(newFormatter.format(formatter.parse(gasStation.getReportTimestamp())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					if (oldDate.getYear() == newDate.getYear()) {
						if (user2.getReputation() > user1.getReputation()
								&& newDate.getDayOfYear() - oldDate.getDayOfYear() < 4) { // time in milliseconds
							// in this case, the last report is still valid -> no update
							return;
						}
					} else {
						if (user2.getReputation() > user1.getReputation()
								&& (newDate.getDayOfYear() + DAYS_IN_YEAR + (1 - (oldDate.getYear() % 4)))
										- oldDate.getDayOfYear() < 4) {
							return;
						}
					}
				}
			}

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
			if (gasStation.getHasPremiumDiesel()) {
				prices.add(premiumDieselPrice);
				gasStation.setPremiumDieselPrice(premiumDieselPrice);
			}
			this.checkPriceList(prices);
			User user = userRepository.findOne(userId);
			gasStation.setUser(user);
			gasStation.setReportTimestamp(formatter.format(new Date(System.currentTimeMillis())));
			gasStation.setReportDependability((OBSOLESCENCE_50 * (user.getReputation() + 5) / 10) + OBSOLESCENCE_50);
			gasStation.setReportUser(user.getUserId());
			gasStationRepository.saveAndFlush(gasStation);
		} else {
			throw new InvalidGasStationException("Invalid gas station " + gasStationId);
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
	private void checkCarSharing(String carsharing) throws InvalidCarSharingException {
		carsharing=carsharing.toLowerCase().replaceAll("\\s+", "");
		if (!carsharing.equals("null") && !carsharing.equals("enjoy") && !carsharing.equals("car2go")) {
			throw new InvalidCarSharingException("invalid car sharing " + carsharing);
		}
	}


	private void checkPriceList(List<Double> prices) throws PriceException {
		for (Double price : prices) {
				if (price!=null && price < 0) {

					throw new PriceException("Negative price is not valid");
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
		DateFormat formatter = new SimpleDateFormat("MM-dd-YYYY");
		DateFormat newFormatter = new SimpleDateFormat("YYYY-MM-dd");
		for (GasStation gs : gasStationRepository.findAll()) {
			if (gs.getReportUser() != null && gs.getReportUser() > 0 && gs.getUser() != null) {
				LocalDate oldDate;
				try {
					oldDate = LocalDate.parse(newFormatter.format(formatter.parse(gs.getReportTimestamp())));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				LocalDate newDate = LocalDate.now();
				if (oldDate.getYear() == newDate.getYear()) {
					if (newDate.getDayOfYear() - oldDate.getDayOfYear() > 7) {
						gs.setReportDependability(
								Math.round(OBSOLESCENCE_50 * (gs.getUser().getReputation() + 5) / 10));
						gasStationRepository.saveAndFlush(gs);
					} else {
						gs.setReportDependability(
								Math.round(OBSOLESCENCE_50 * (gs.getUser().getReputation() + 5) / 10 + OBSOLESCENCE_50
										* (1 - ((double) (newDate.getDayOfYear() - oldDate.getDayOfYear()) / 7))));
						gasStationRepository.saveAndFlush(gs);
					}
				} else {
					if ((newDate.getDayOfYear() + DAYS_IN_YEAR + (1 - (oldDate.getYear() % 4)))
							- oldDate.getDayOfYear() > 7) {
						gs.setReportDependability(
								Math.round(OBSOLESCENCE_50 * (gs.getUser().getReputation() + 5) / 10));
						gasStationRepository.saveAndFlush(gs);

					} else {
						gs.setReportDependability(Math.round((OBSOLESCENCE_50 * (gs.getUser().getReputation() + 5) / 10
								+ OBSOLESCENCE_50 * (1 - ((double) (newDate.getDayOfYear() + DAYS_IN_YEAR
										+ (1 - (oldDate.getYear() % 4))) - oldDate.getDayOfYear()) / 7))));
						gasStationRepository.saveAndFlush(gs);
					}
				}
			}
		}
	}
}
