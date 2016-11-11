package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import tibano.dto.PaymentInfo;
import tibano.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
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
			BigDecimal bd = new BigDecimal(amount);
			bd = bd.setScale(2, RoundingMode.HALF_UP);
			Integer loyaltyPoints = 5 + Integer.valueOf(Double.valueOf(bd.doubleValue()).intValue());
			return new PaymentInfo(pt.getEnd(), amount, duration, loyaltyPoints);
		}
		return new PaymentInfo(null, Double.valueOf(0), Duration.ZERO, Integer.valueOf(0));
	}
	@RequestMapping(path = "/getPaymentHistory")
	List<PaymentInfo> getPaymentHistory(@RequestParam(name="userId") Long userId) {
		Iterable<ParkingTransaction> pts = ptRepository.findAll();
		List<PaymentInfo> paymentInfos = new ArrayList<>();
		for (ParkingTransaction pt : pts) {
//			BigDecimal bd = new BigDecimal(pt.getAmount() != null ? pt.getAmount() : 0);
//			bd = bd.setScale(2, RoundingMode.HALF_UP);
			PaymentInfo paymentInfo = new PaymentInfo(pt.getEnd(), pt.getAmount(), pt.getDuration(), pt.getLoyaltyPoints());
			paymentInfos.add(paymentInfo);
		}
		return paymentInfos;
	}

}
