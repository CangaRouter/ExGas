package it.polito.ezgas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)

public class UserServiceTest {
	
	
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private UserConverter userConverterMock;
	@Mock
	private UserDto userDtoMock;
	@Mock
	private User userMock;
	@Mock
	private IdPw credentialsMock;
	
	@Before
	public void setUp() {
		userRepositoryMock=mock(UserRepository.class);
		userConverterMock=mock(UserConverter.class);
		userDtoMock=mock(UserDto.class);
		userMock=mock(User.class);
		
	}
	
	@Test
	public void TC1_saveUserTest() {
		//Test: saving without errors
		when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(new User("lukeb", "password", "prova@email.com", 5));
		when(userConverterMock.toUser(any(UserDto.class))).thenReturn(new User( "lukeb", "password", "prova@email.com", 5) );
		when(userConverterMock.toUserDto(any(User.class))).thenReturn(new UserDto(2, "lukeb", "password", "prova@email.com", 5) );
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(null);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
		assert(userService.saveUser(u)!=null);
	}
	
	@Test
	public void TC2_saveUserTest() {
		//Test: try to save an already existing user
		when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(new User("lukeb", "password", "prova@email.com", 5));
		when(userConverterMock.toUser(any(UserDto.class))).thenReturn(new User( "lukeb", "password", "prova@email.com", 5) );
		when(userConverterMock.toUserDto(any(User.class))).thenReturn(new UserDto(2, "lukeb", "password", "prova@email.com", 5) );
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(new User( "lukeb", "password", "prova@email.com", 5));
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		UserDto u=new UserDto(null, "lukeb", "password", "prova@email.com", 5);
		assert(userService.saveUser(u)==null);
	}
	
	@Test
	public void TC3_saveUserTest() {
		//Test: update a user
		when(userDtoMock.getUserId()).thenReturn(2);
		when(userMock.getUserId()).thenReturn(2);
		when(userRepositoryMock.saveAndFlush(any(User.class))).thenReturn(userMock);
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(userMock);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		assert(userService.saveUser(userDtoMock)!=null);
	}
	@Test
	public void TC4_saveUserTest() {
		//Test: try to update a non existing or a different user (different UserIds)
		when(userDtoMock.getUserId()).thenReturn(2);
		when(userMock.getUserId()).thenReturn(1);
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(userMock);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		assert(userService.saveUser(userDtoMock)!=null);
	}
	
	
	@Test
	public void TC1_getUserById() {
		//Test: try to get a user with negative id
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
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
		when(userRepositoryMock.exists(any(Integer.class))).thenReturn(false);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try{assert(userService.getUserById(1)==null);}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_getUserById() {
		//Test: try to retrieve an user with an existing id
		Boolean thrown=false;
		when(userRepositoryMock.exists(any(Integer.class))).thenReturn(true);
		when(userRepositoryMock.getOne(any(Integer.class))).thenReturn(userMock);
		when(userConverterMock.toUserDto(any(User.class))).thenReturn(userDtoMock);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try{assert(userService.getUserById(1)!=null);}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_getAllUsers() {
		//Test: try to retrieve an empty list
		when(userRepositoryMock.findAll()).thenReturn(new ArrayList<User>());
		when(userConverterMock.toUserDtoList(any(List.class))).thenReturn(new ArrayList<UserDto>());
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		assert(userService.getAllUsers().isEmpty());
	}
	@Test
	public void TC2_getAllUsers() {
		//Test: try to retrieve a list not empty
		List<User> list=new ArrayList<User>();
		list.add(userMock);
		List<UserDto> listDto=new ArrayList<UserDto>();
		listDto.add(userDtoMock);
		when(userRepositoryMock.findAll()).thenReturn(list);
		when(userConverterMock.toUserDtoList(any(List.class))).thenReturn(listDto);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		assert(!userService.getAllUsers().isEmpty());
	}
	
	@Test
	public void TC1_deleteUser() {
		//Test: try to delete a user with a negtive id
		Boolean thrown=false;
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
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
		Boolean thrown=false;
		when(userRepositoryMock.exists(any(Integer.class))).thenReturn(true);
		
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.deleteUser(1)==true);
		}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_deleteUser() {
		Boolean thrown=false;
		when(userRepositoryMock.exists(any(Integer.class))).thenReturn(false);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.deleteUser(1)==false);
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
		credentialsMock=mock(IdPw.class);
		when(credentialsMock.getPw()).thenReturn(null);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			userService.login(credentialsMock);
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
		credentialsMock=mock(IdPw.class);
		when(credentialsMock.getUser()).thenReturn(null);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			userService.login(credentialsMock);
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
		credentialsMock=mock(IdPw.class);
		when(credentialsMock.getUser()).thenReturn("user");
		when(credentialsMock.getPw()).thenReturn("psw");
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(null);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			userService.login(credentialsMock);
		}
		catch(InvalidLoginDataException e) {
			thrown=true;
		}
		assert(thrown==true);
	}
	@Test
	public void TC4_login() {
		//Test: user does not exists
		Boolean thrown=false;
		credentialsMock=mock(IdPw.class);
		when(credentialsMock.getUser()).thenReturn("user");
		when(credentialsMock.getPw()).thenReturn("psw");
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(userMock);
		when(userMock.getPassword()).thenReturn("psw2");
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			userService.login(credentialsMock);
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
		credentialsMock=mock(IdPw.class);
		when(credentialsMock.getUser()).thenReturn("user");
		when(credentialsMock.getPw()).thenReturn("psw");
		when(userRepositoryMock.findByEmail(any(String.class))).thenReturn(userMock);
		when(userMock.getPassword()).thenReturn("psw");
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.login(credentialsMock)!=null);
		}
		catch(InvalidLoginDataException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_increaseUserReputation() {
		Boolean thrown=false;
		
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
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
		Boolean thrown=false;
		when(userRepositoryMock.getOne(any(Integer.class))).thenReturn(userMock);
		when(userMock.getReputation()).thenReturn(5);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.increaseUserReputation(1)==5);
		}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_increaseUserReputation() {
		User u=new User();
		Boolean thrown=false;
		when(userRepositoryMock.getOne(any(Integer.class))).thenReturn(u);
		u.setReputation(3);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.increaseUserReputation(1)==4);
		}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	
	@Test
	public void TC1_decreaseUserReputation() {
		Boolean thrown=false;
		
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
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
		Boolean thrown=false;
		when(userRepositoryMock.getOne(any(Integer.class))).thenReturn(userMock);
		when(userMock.getReputation()).thenReturn(-5);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.decreaseUserReputation(1)==-5);
		}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
	@Test
	public void TC3_decreaseUserReputation() {
		User u=new User();
		Boolean thrown=false;
		when(userRepositoryMock.getOne(any(Integer.class))).thenReturn(u);
		u.setReputation(-3);
		UserService userService=new UserServiceimpl(userRepositoryMock,userConverterMock);
		try {
			assert(userService.decreaseUserReputation(1)==-4);
		}
		catch(InvalidUserException e) {
			thrown=true;
		}
		assert(thrown==false);
	}
}
