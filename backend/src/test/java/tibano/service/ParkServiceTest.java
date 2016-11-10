package tibano.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.Car;
import tibano.entity.CarRepository;
import tibano.entity.ParkingTransaction;
import tibano.entity.ParkingTransactionRepository;
import tibano.entity.User;

@RunWith(SpringRunner.class)
public class ParkServiceTest {
	private static final Long AREA_ID = 1L;
	private static final String AREA_NAME = "Area";
	private static final String LIC_PLATE = "S-TEST";
	@Mock
    private AreaRepository areaRepository;
	@Mock
	private ParkingTransactionRepository ptRepository;
	@Mock
	private CarRepository carRepository;
	@Mock
	private LoyaltyIntegrator loyaltyIntegrator;
	private ParkService target;
	
	@Before
    public void setup() {
		MockitoAnnotations.initMocks(this);
		target = new ParkService(areaRepository, ptRepository, carRepository, loyaltyIntegrator);
    }
	
	@Test
	public void start() {
		Area area = new Area(AREA_NAME, 30L);
		Mockito.when(areaRepository.findOne(AREA_ID)).thenReturn(area);
		Car car = new Car(LIC_PLATE, new User(""));
		Mockito.when(carRepository.findByLicensePlate(LIC_PLATE)).thenReturn(car);
		target.start(AREA_ID, LIC_PLATE);
		Mockito.verify(ptRepository).save(Mockito.any(ParkingTransaction.class));
	}
	
	@Test
	public void startNoArea() {
		Mockito.when(areaRepository.findOne(AREA_ID)).thenReturn(null);
		target.start(AREA_ID, LIC_PLATE);
		Mockito.verify(ptRepository, Mockito.times(0)).save(Mockito.any(ParkingTransaction.class));
	}
	@Test
	public void stop() {
		ParkingTransaction pt = new ParkingTransaction(new Area(AREA_NAME, 30L), new Car(LIC_PLATE, new User("")));
		Mockito.when(ptRepository.findOpenTransactionByAreaAndLicensePlate(AREA_ID, LIC_PLATE)).thenReturn(pt);
		target.stop(AREA_ID, LIC_PLATE);
		Mockito.verify(ptRepository).save(Mockito.any(ParkingTransaction.class));
	}
	@Test
	public void stopNoOpenTx() {
		Mockito.when(ptRepository.findOpenTransactionByAreaAndLicensePlate(AREA_ID, LIC_PLATE)).thenReturn(null);
		target.stop(AREA_ID, LIC_PLATE);
		Mockito.verify(ptRepository, Mockito.times(0)).save(Mockito.any(ParkingTransaction.class));
	}
}
