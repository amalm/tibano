package tibano.entity;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.Duration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import tibano.dto.PaymentInfo;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ParkingTransactionRepositoryTest {
	private static final String AREA = "Area";
	private static final String USER_NAME = "UserName";
	private static final String LIC_PLATE = "ParkingTransactionRepositoryTest";
	
	@Autowired
	private ParkingTransactionRepository target;
	@Autowired
	private AreaRepository areaRepository;
	@Autowired
	private CarRepository carRepository;
	@Autowired
	private UserRepository userRepository;
	private Area area;
	private User user;
	@Before
	public void before()
	{
		area = new Area(AREA, 30L);
		area = areaRepository.save(area);
		user = userRepository.save(new User(USER_NAME));
	}
	@Test
	public void read()
	{
		Car car = carRepository.save(new Car(LIC_PLATE, user));
		ParkingTransaction pt = new ParkingTransaction(area, car);
		pt = target.save(pt);
		assertEquals(target.findOne(pt.getId()).getArea().getName(), AREA);
	}
	
	@Test
	public void findOpenTransactionByAreaAndLicensePlate() {
		// when there is a closed TX
		Car car = carRepository.save(new Car(LIC_PLATE+"1", user));
		ParkingTransaction pt = new ParkingTransaction(area, car);
		pt.end(new PaymentInfo(null, Double.valueOf(0), Duration.ZERO, Integer.valueOf(0)));
		pt = target.save(pt);
		// and an open TX
		pt = new ParkingTransaction(area, car);
		pt = target.save(pt);
		// then I get only the open TX
		pt = target.findOpenTransactionByAreaAndLicensePlate(pt.getArea().getId(), car.getLicensePlate());
	}

	@Test
	public void findOpenTransactionByAreaAndLicensePlateNoTX() {
		assertNull(target.findOpenTransactionByAreaAndLicensePlate(area.getId(), LIC_PLATE+"3"));
		
	}
	@Test
	public void findOpenTransactionByArea() {
		Car car = carRepository.save(new Car("STEST4", user));
		ParkingTransaction pt = new ParkingTransaction(area, car);
		pt = target.save(pt);
		Assert.assertTrue(target.getOpenTransactionByAreaCount(pt.getArea().getId()) > 0);
		
	}
}
