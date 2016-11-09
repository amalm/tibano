package tibano.entity;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CarRepositoryTest {
	private static final String LIC_PLATE = "STEST";
	private static final String USER_NAME = "trump";
	
	@Autowired
	private CarRepository target;
	@Autowired
	private UserRepository userRepository;
	@Test
	public void read()
	{
		User user = userRepository.save(new User(USER_NAME));
		Car car = new Car(LIC_PLATE, user);
		car = target.save(car);
		Assert.assertEquals(target.findOne(car.getId()).getLicensePlate(), LIC_PLATE);
	}
}
