package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tibano.dto.CarPaymentStatus;
import tibano.entity.*;

@RestController
@CrossOrigin
public class EnforcementService {
	private final static Logger LOGGER = LoggerFactory.getLogger(EnforcementService.class);
	private final ParkingTransactionRepository ptRepository;


	public EnforcementService(ParkingTransactionRepository ptRepository) {
		super();
		this.ptRepository = ptRepository;
	}

	@RequestMapping(path="/checkPayment/{areaId}/{licensePlate}", method = RequestMethod.GET)
	CarPaymentStatus checkPayment(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Checking parking status for license plate {} in area {}", licensePlate, areaId);
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);

		return new CarPaymentStatus(licensePlate, null != pt);
	}

}
