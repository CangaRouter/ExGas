package it.polito.ezgas;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EZGasApplicationTests {
	@Autowired
	GasStationRepository gasStationRepository;
	@Autowired
	UserRepository userRepository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRepository() {
		User user = new User("gino", "password", "email@finta.fake", 0);
		GasStation gasStation = new GasStation("pompa", "corso duca 24", true, true, false, false, false, "enjoy",
				345335, 43523, 1.5, 1.8, 0, 0, 0, 1, "01/06/2020", 10);
		user = userRepository.saveAndFlush(user);
		gasStation = gasStationRepository.saveAndFlush(gasStation);

		Assert.hasText(userRepository.findOne(user.getUserId()).getUserName() + "  #" + user.getUserId(),
				"User not found");
		Assert.hasText(gasStationRepository.findOne(gasStation.getGasStationId()).getGasStationName() + " #"
				+ gasStation.getGasStationId(), "Gas Station not found");

		gasStationRepository.deleteAllInBatch();
		GasStation gasStation2 = new GasStation("pompa", "corso duca 24", true, true, false, false, false, "enjoy", 1,
				3, 1.5, 1.8, 1, 3, 0, 2, "01/06/2020", 10);
		gasStation2 = gasStationRepository.saveAndFlush(gasStation2);

		Assert.hasText(gasStationRepository.findBygasStationName(gasStation2.getGasStationName()).getGasStationName()
				+ " #" + gasStation2.getGasStationId(), "Gas Station2 non found");

	}
}
