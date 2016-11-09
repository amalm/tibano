package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	private final AreaRepository areaRepository;
	private final ParkingTransactionRepository ptRepository;
	private final CarRepository carRepository;
	
	
	public ParkService(tibano.entity.AreaRepository areaRepository, ParkingTransactionRepository ptRepository, CarRepository carRepository) {
		super();
		this.areaRepository = areaRepository;
		this.ptRepository = ptRepository;
		this.carRepository = carRepository;
	}

	@RequestMapping(path="/start/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void start(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Start parking in area {} with license plate", areaId, licensePlate);
		// Check if a TX is already running
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);
		if (pt != null) {
			LOGGER.warn("A parking payment transaction is already running for areaId/licensePlate", areaId, licensePlate);
			return;
		}
		Area area = areaRepository.findOne(areaId);
		if (area != null) {
			Car car = carRepository.findByLicensePlate(licensePlate);
			if (car != null) {
				ptRepository.save(new ParkingTransaction(area, car));
			}
			else {
				LOGGER.error("Could not find a car with license plate {}", licensePlate);
			}
		}
		else {
			LOGGER.error("Could not find an area with id {} to start a TX for license plate {}", areaId, licensePlate);
		}
	}

	@RequestMapping(path="/stop/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void stop(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Stop parking in area {} with license plate", areaId, licensePlate);
		ParkingTransaction pt = ptRepository.findOpenTransactionByAreaAndLicensePlate(areaId, licensePlate);
		if (pt != null) {
			pt.end();
			ptRepository.save(pt);
		}
		else {
			LOGGER.error("Could not find a open transaction in area with id {}  for license plate {}", areaId, licensePlate);
		}

	}
}