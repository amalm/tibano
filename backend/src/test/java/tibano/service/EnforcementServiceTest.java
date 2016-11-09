package tibano.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;
import tibano.dto.CarPaymentStatus;
import tibano.entity.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EnforcementServiceTest {

	@Mock
	private ParkingTransactionRepository ptRepository;
    private EnforcementService target;

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	target = new EnforcementService(ptRepository);
    }

    @Test
    public void checkForPayingCar() throws Exception {
        Long areaId = 4711l;
        String licensePlate = "HH FT 4711";

        ParkingTransaction pt = new ParkingTransaction(new Area("Bahnhof", 42l), new Car(licensePlate, new User("Hubert F.")));
        when(ptRepository.findOpenTransactionByAreaAndLicensePlate(any(), any())).thenReturn(pt);

        CarPaymentStatus status = target.checkPayment(areaId, licensePlate);

        Assert.assertEquals(licensePlate, status.getLicensePlate());
        Assert.assertTrue(status.isPaying());
    }

    @Test
    public void checkForNonPayingCar() throws Exception {
        Long areaId = 4711l;
        String licensePlate = "HH FT 4711";

        when(ptRepository.findOpenTransactionByAreaAndLicensePlate(any(), any())).thenReturn(null);

        CarPaymentStatus status = target.checkPayment(areaId, licensePlate);

        Assert.assertEquals(licensePlate, status.getLicensePlate());
        Assert.assertFalse(status.isPaying());
    }
}