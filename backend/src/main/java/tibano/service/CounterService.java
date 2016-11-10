package tibano.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tibano.dto.CurrentAreaUtilization;
import tibano.entity.Area;
import tibano.entity.AreaRepository;

@RestController
@CrossOrigin
public class CounterService {
	private final static Logger LOGGER = LoggerFactory.getLogger(CounterService.class);

	private AreaRepository areaRepository;

	@Autowired
	public CounterService(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}

	@RequestMapping(path="/entry/{areaId}", method = RequestMethod.POST)
	CurrentAreaUtilization entry(@PathVariable Long areaId) {
		LOGGER.info("Entry to area {}", areaId);
		Area area = areaRepository.findOne(areaId);
        area.incrementOccupied();
		areaRepository.save(area);
		LOGGER.info("Area {} now.", area, area.getOccupied());
		return new CurrentAreaUtilization(area.getCapacity(), area.getOccupied());
	}

	@RequestMapping(path="/exit/{areaId}", method = RequestMethod.POST)
	CurrentAreaUtilization exit(@PathVariable Long areaId) {
		LOGGER.info("Exit from area {}", areaId);
		Area area = areaRepository.findOne(areaId);
		area.decrementOccupied();
		areaRepository.save(area);
		LOGGER.info("Area now {}.", area, area.getOccupied());
		return new CurrentAreaUtilization(area.getCapacity(), area.getOccupied());
	}
}
