package it.polito.ezgas;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import exception.InvalidLoginDataException;
import exception.InvalidUserException;
import org.mockito.Mock;
import it.polito.ezgas.converter.UserConverter;
import it.polito.ezgas.dto.IdPw;
import it.polito.ezgas.dto.UserDto;
import it.polito.ezgas.entity.User;
import it.polito.ezgas.repository.UserRepository;
import it.polito.ezgas.service.*;
import it.polito.ezgas.service.impl.UserServiceimpl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceAPITests {
		@Autowired
		private TestEntityManager entityManager;
		@Autowired
		private UserRepository userRepository;
		
		private UserService userService;
		private UserConverter userConverter;
		
		
		@Before
		public void setUp() {
			userConverter= new UserConverter();
			userService=new UserServiceimpl(userRepository, userConverter);
		}
		@After
		public void destroy() {
			userRepository.deleteAll();
		}
		
		@Test
		public void TC1_saveUser() {
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			UserDto res;
			res=userService.saveUser(u);
			assert(res!=null);
			assert(res.getEmail().equals(u.getEmail()));
		}
		@Test
		public void TC2_saveUserTest() {
			//Test: try to save an already existing user
			
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			//first save of user u -> no error 
			assert(userService.saveUser(u)!=null);
			//second save of user u - >error: returns null
			assert(userService.saveUser(u)==null); 
		}
		@Test
		public void TC3_saveUserTest() {
			//Test: update a user
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			//first save of user u -> no error 
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			//I need to know the userId to update this user 
			u.setUserId(res.getUserId());
			u.setPassword("newpassword");
			//second save -> returns the updated user
			assert(userService.saveUser(u).getPassword().equals("newpassword"));
		}
		@Test
		public void TC4_saveUserTest() {
			//Test: try to update a non existing or a different user (different UserIds)
			
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			//first save of user u -> no error 
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			//we set the userId to a wrong value
			u.setUserId(res.getUserId()+1); 
			//second save -> no effects on dB (returns the user u without updating it)
			assert(userService.saveUser(u)!=null);
		}
		@Test
		public void TC1_getUserById() {
			//Test: try to get a user with negative id
			Boolean thrown=false;
			try{userService.getUserById(-1);}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC2_getUserById() {
			//Test: try to retrieve an user with a non existing id
			Boolean thrown=false;
			try{assert(userService.getUserById(98)==null);}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC3_getUserById() {
			//Test: try to retrieve an user with an existing id
			Boolean thrown=false;
			UserDto res=userService.saveUser(new UserDto(null, "lukeb", "password", "prova@email.com", 5));
			assert(res!=null);
			try{
				res=userService.getUserById(res.getUserId());
				assert(res!=null);
				assert(res.getEmail().equals("prova@email.com"));
				assert(res.getUserName().equals("lukeb"));
				assert(res.getPassword().equals("password"));
				}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		
		@Test
		public void TC1_getAllUsers() {
			//Test: try to retrieve an empty list
			userRepository.deleteAll();
			assert(userService.getAllUsers().isEmpty());
		}
		@Test
		public void TC2_getAllUsers() {
			//Test: try to retrieve a list not empty 
			assert(userService.saveUser(new UserDto(null, "lukeb", "password", "prova@email.com", 5))!=null);
			assert(userService.saveUser(new UserDto(null, "dartf", "force", "boss@galaxies.com", 5))!=null);
			List<UserDto> list=userService.getAllUsers();
			assert(!list.isEmpty());
			for(UserDto us:list) {
				System.out.println(us.getEmail());
				//(the admin user is also present)
				assert(us.getEmail().equals("prova@email.com")||us.getEmail().equals("boss@galaxies.com")||us.getEmail().equals("admin@ezgas.com"));
			}
		}
		@Test
		public void TC1_deleteUser() {
			//Test: try to delete a user with a negative id
			Boolean thrown=false;
			try {
				userService.deleteUser(-1);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC2_deleteUser() {
			//Test: try to correctly delete a user 
			Boolean thrown=false;
			try {
				UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
				UserDto res=userService.saveUser(u);
				assert(res!=null);
				assert(userService.deleteUser(res.getUserId())==true);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC3_deleteUser() {
			//Test: try to delete a user with a non existing id
			Boolean thrown=false;
			try {
				assert(userService.deleteUser(98)==false);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC1_login() {
			//Test: try to login with null psw
			Boolean thrown=false;
			IdPw credentials=new IdPw("prova@ezgas.com",null);
			try {
				userService.login(credentials);
			}
			catch(InvalidLoginDataException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC2_login() {
			//Test: try to login with null username
			Boolean thrown=false;
			IdPw credentials=new IdPw(null,"password");
			try {
				userService.login(credentials);
			}
			catch(InvalidLoginDataException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC3_login() {
			//Test: user does not exists
			Boolean thrown=false;
			IdPw credentials=new IdPw("email@gmail.com","password");
			try {
				userService.login(credentials);
			}
			catch(InvalidLoginDataException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC4_login() {
			//Test: passwords do not correspond
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			IdPw credentials=new IdPw(u.getEmail(),"psw");
			try {
				userService.login(credentials);
			}
			catch(InvalidLoginDataException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC5_login() {
			//Test: correct login
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			IdPw credentials=new IdPw(res.getEmail(),res.getPassword());
			try {
				assert(userService.login(credentials)!=null);
			}
			catch(InvalidLoginDataException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC1_increaseUserReputation() {
			//Test: try to update a user with negative id
			Boolean thrown=false;
			try {
				userService.increaseUserReputation(-1);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC2_increaseUserReputation() {
			//Test: try to increase reputation for a user that already has the max value (5)
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			try {
				assert(userService.increaseUserReputation(res.getUserId())==5);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC3_increaseUserReputation() {
			//Test: try to increase reputation for a user
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 3);
			UserDto res=userService.saveUser(u);
			assert(res!=null);
			try {
				assert(userService.increaseUserReputation(res.getUserId())==4);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		
		@Test
		public void TC1_decreaseUserReputation() {
			//Test: try to decrease reputation of a user with a negative id
			Boolean thrown=false;
			try {
				userService.decreaseUserReputation(-1);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==true);
		}
		@Test
		public void TC2_decreaseUserReputation() {
			//Test: try to decrease reputation of a user that already has the minimum value (-5)
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", -5);
			UserDto res=userService.saveUser(u);
			try {
				assert(userService.decreaseUserReputation(res.getUserId())==-5);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
		@Test
		public void TC3_decreaseUserReputation() {
			//Test: try to decrease reputation of a user
			Boolean thrown=false;
			UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", -3);
			UserDto res=userService.saveUser(u);
			try {
				assert(userService.decreaseUserReputation(res.getUserId())==-4);
			}
			catch(InvalidUserException e) {
				thrown=true;
			}
			assert(thrown==false);
		}
	}

		
