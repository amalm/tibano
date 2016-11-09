package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.ParkingTransaction;
import tibano.entity.ParkingTransactionRepository;

@RestController
public class Park {
	private final static Logger LOGGER = LoggerFactory.getLogger(Park.class);
	private final AreaRepository areaRepository;
	private final ParkingTransactionRepository ptRepository;
	
	
	
	public Park(tibano.entity.AreaRepository areaRepository, ParkingTransactionRepository ptRepository) {
		super();
		this.areaRepository = areaRepository;
		this.ptRepository = ptRepository;
	}

	@RequestMapping(path="/start/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void start(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Start parking in area {} with license plate", areaId, licensePlate);
		Area area = areaRepository.findOne(areaId);
		if (area != null) {
			ptRepository.save(new ParkingTransaction(area));
		}
	}

	@RequestMapping(path="/stop/{areaId} /{licensePlate}", method = RequestMethod.POST)
	void stop(@PathVariable Long areaId, @PathVariable String licensePlate) {
		LOGGER.info("Stop parking in area {} with license plate", areaId, licensePlate);
	}
}
