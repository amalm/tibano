package tibano.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tibano.dto.PaymentInfo;
import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.Car;
import tibano.entity.CarRepository;
import tibano.entity.ParkingTransaction;
import tibano.entity.ParkingTransactionRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class ParkService {
	private final static Logger LOGGER = LoggerFactory.getLogger(ParkService.class);
	private final static Double SEC_TARIF = new Double(0.01);
	private static final Double LOYALTY_POINTS_PER_SEC = new Double(0.01);
	private final AreaRepository areaRepository;
	private final ParkingTransactionRepository ptRepository;
	private final CarRepository carRepository;
	private final LoyaltyIntegrator loyaltyIntegrator;

	public ParkService(tibano.entity.AreaRepository areaRepository, ParkingTransactionRepository ptRepository,
			CarRepository carRepository, LoyaltyIntegrator loyaltyIntegrator) {
		super();
		this.areaRepository = areaRepository;
		this.ptRepository = ptRepository;
		this.carRepository = carRepository;
		this.loyaltyIntegrator = loyaltyIntegrator;
	}

	@RequestMapping(path = "/start/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void start(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Start parking in area {} with license plate {}", areaId, licensePlate);
		// Check if a TX is already running
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);
		if (pt != null) {
			LOGGER.warn("A parking payment transaction is already running for areaId/licensePlate", areaId,
					licensePlate);
			return;
		}
		Area area = areaRepository.findOne(areaId);
		if (area != null) {
			Car car = carRepository.findByLicensePlate(licensePlate);
			if (car != null) {
				ptRepository.save(new ParkingTransaction(area, car));
			} else {
				LOGGER.error("Could not find a car with license plate {}", licensePlate);
			}
		} else {
			LOGGER.error("Could not find an area with id {} to start a TX for license plate {}", areaId, licensePlate);
		}
	}

	@RequestMapping(path = "/stop/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void stop(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Stop parking in area {} with license plate {}", areaId, licensePlate);
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);
		if (pt != null) {
			PaymentInfo paymentInfo = getPaymentInfo(areaId, licensePlate);
			pt.end(paymentInfo);
			ptRepository.save(pt);
			loyaltyIntegrator.addPoints(paymentInfo.getLoyaltyPoints());

		} else {
			LOGGER.error("Could not find a open transaction in area with id {}  for license plate {}", areaId,
					licensePlate);
		}
	}

	@RequestMapping(path = "/getPaymentInfo")
	PaymentInfo getPaymentInfo(@RequestParam(name = "areaId") Long areaId,
			@RequestParam(name = "licensePlate") String licensePlate) {
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);
		if (pt != null) {
			Duration duration = Duration.between(pt.getStart(), LocalDateTime.now());
			Double amount = duration.getSeconds() * SEC_TARIF;
			Double loyaltyPoints = duration.getSeconds()*LOYALTY_POINTS_PER_SEC;
			return new PaymentInfo(amount, duration, loyaltyPoints);
		}
		return new PaymentInfo(Double.valueOf(0), Duration.ZERO, Double.valueOf(0));
	}

}
