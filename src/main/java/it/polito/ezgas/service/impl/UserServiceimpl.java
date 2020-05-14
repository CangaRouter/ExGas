package it.polito.ezgas.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.InvalidLoginDataException;
import exception.InvalidUserException;
import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.LoginDto;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.UserService;

/**
 * Created by softeng on 27/4/2020.
 */
@Service
public class UserServiceimpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserConverter userConverter;

	@Override
	public UserDto getUserById(Integer userId) throws InvalidUserException {
		// TODO Auto-generated method stub
		if (userId < 0)
			throw new InvalidUserException("Invalid user ID");
		if (userRepository.exists(userId)) {
			User user = userRepository.getOne(userId);
			return userConverter.toUserDto(user);
		}
		return null;
	}

	@Override
	public UserDto saveUser(UserDto userDto) {
		// TODO Auto-generated method stub
		User user = userConverter.toUser(userDto);
		userRepository.saveAndFlush(user);
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
		// TODO Auto-generated method stub
		List<UserDto> userDtolist = new ArrayList<>();
		userDtolist = userConverter.toUserDtoList(userRepository.findAll());
		return userDtolist;
	}

	@Override
	public Boolean deleteUser(Integer userId) throws InvalidUserException {
		// TODO Auto-generated method stub
		if (userId < 0)
			throw new InvalidUserException("Invalid user ID");
		if (userRepository.exists(userId)) {
			userRepository.delete(userId);
			return true;
		}
		return false;
	}

	@Override
	public LoginDto login(IdPw credentials) throws InvalidLoginDataException {
		// TODO Auto-generated method stub
		// token considered as null, will be used later
		if (credentials.getPw() == null || credentials.getUser() == null)
			throw new InvalidLoginDataException("Invalid user and/or password");
		User user = userRepository.findByUserName(credentials.getUser());
		if (user == null)
			throw new InvalidLoginDataException("Invalid username");
		if (user.getPassword() != credentials.getPw())
			throw new InvalidLoginDataException("Invalid password for user: " + user.getUserName());
		LoginDto loginDto = new LoginDto(user.getUserId(), user.getUserName(), null, user.getEmail(),
				user.getReputation());
		loginDto.setAdmin(user.getAdmin());
		return loginDto;
	}

	@Override
	public Integer increaseUserReputation(Integer userId) throws InvalidUserException {
		// TODO Auto-generated method stub
		if (userId < 0)
			throw new InvalidUserException("Invalid user ID");
		User user = userRepository.getOne(userId);
		if (user.getReputation() < 5) {
			user.setReputation(user.getReputation() + 1);
			userRepository.saveAndFlush(user);
		}
		return user.getReputation();
	}

	@Override
	public Integer decreaseUserReputation(Integer userId) throws InvalidUserException {
		// TODO Auto-generated method stub
		if (userId < 0)
			throw new InvalidUserException("Invalid user ID");
		User user = userRepository.getOne(userId);
		if (user.getReputation() > -5) {
			user.setReputation(user.getReputation() - 1);
			userRepository.saveAndFlush(user);
		}
		return user.getReputation();
	}

}
