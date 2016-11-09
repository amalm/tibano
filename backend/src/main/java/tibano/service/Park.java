package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Park {
	private final static Logger LOGGER = LoggerFactory.getLogger(Park.class);

	@RequestMapping(path="/start/{area}/{licensePlate}", method = RequestMethod.POST)
	void entry(@PathVariable String areaId, @PathVariable String licensePate) {
		LOGGER.info("Start parking inarea {} with license plate", areaId, licensePate);
	}

	@RequestMapping(path="/stop/{areaId}/{licensePlate}", method = RequestMethod.POST)
	void exit(@PathVariable String areaId, @PathVariable String licensePate) {
		LOGGER.info("Stop parking inarea {} with license plate", areaId, licensePate);
	}
}
