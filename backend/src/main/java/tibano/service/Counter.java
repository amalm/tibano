package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Counter {
	private final static Logger LOGGER = LoggerFactory.getLogger(Counter.class);

	@RequestMapping(path="/entry/{areaId}", method = RequestMethod.POST)
	void entry(@PathVariable String areaId) {
		LOGGER.info("Entry to area {}", areaId);
	}

	@RequestMapping(path="/exit/{areaId}", method = RequestMethod.POST)
	void exit(@PathVariable String areaId) {
		LOGGER.info("Exit from area {}", areaId);
	}
}
