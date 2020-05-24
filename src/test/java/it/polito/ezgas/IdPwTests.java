package it.polito.ezgas;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import it.polito.ezgas.dto.IdPw;

@RunWith(SpringRunner.class)

public class IdPwTests {
	
	private IdPw credentials=new IdPw("user","pw");
	
	@Test
	public void TC1_IdPwUser() {
		credentials.setUser("user@ezgas.com");
		assert(credentials.getUser().equals("user@ezgas.com"));
	}
	@Test
	public void TC1_IdPwPassword() {
		credentials.setPw("Th1s@ppISc00l");
		assert(credentials.getPw().equals("Th1s@ppISc00l"));
	}
}
