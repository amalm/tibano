package tibano.service;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import tibano.dto.UserInfo;
import tibano.entity.CarRepository;
import tibano.entity.User;
import tibano.entity.UserRepository;

@RunWith(SpringRunner.class)
public class UserServiceTest {

	private static final Long USER_ID = 1L;
	@Mock
    private UserRepository userRepository;
	@Mock
	private CarRepository carRepository;
    private UserService target;

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	target = new UserService(userRepository, carRepository);
    }

    @Test
    public void getUser() throws Exception {
        User user = new User("userName");
		when(userRepository.findOne(USER_ID)).thenReturn(user);
		when(carRepository.findLicensePlateByUserId(USER_ID)).thenReturn(new HashSet<String>(Arrays.asList("S1", "S2")));
        UserInfo userInfo = target.getUser(USER_ID);
        Assert.assertEquals(userInfo.getLicensePlates().size(), 2);
    }
}