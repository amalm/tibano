package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tibano.dto.CurrentAreaUtilization;
import tibano.entity.Area;
import tibano.entity.AreaRepository;

@RestController
public class Counter {
	private final static Logger LOGGER = LoggerFactory.getLogger(Counter.class);

	private AreaRepository areaRepository;

	@Autowired
	public Counter(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	@RequestMapping(path="/entry/{areaId}", method = RequestMethod.POST)
	CurrentAreaUtilization entry(@PathVariable Long areaId) {
		LOGGER.info("Entry to area {}", areaId);
		Area area = areaRepository.findOne(areaId);
        area.incrementOccupied();
		LOGGER.info("Area {} now at {} occupied lots.", areaId, area.getOccupied());
		return new CurrentAreaUtilization(area.getCapacity(), area.getOccupied());
	}

	@RequestMapping(path="/exit/{areaId}", method = RequestMethod.POST)
	CurrentAreaUtilization exit(@PathVariable String areaId) {
		LOGGER.info("Exit from area {}", areaId);
		return new CurrentAreaUtilization(23l, 12l);
	}
}
