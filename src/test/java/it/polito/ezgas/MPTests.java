package it.polito.ezgas;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import exception.InvalidLoginDataException;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.LoginDto;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.GasStation;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.GasStationRepository;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.impl.GasStationServiceimpl;
import it.polito.ezgas.service.impl.UserServiceimpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MPTests {
	@Autowired
	GasStationRepository gasStationRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	GasStationServiceimpl gasStationServiceimpl;
	@Autowired
	UserServiceimpl userServiceimpl;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testRepository() {

	}
}
