package tibano.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tibano.dto.AreaInfo;
import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.ParkingTransactionRepository;

@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class MonitorService {
	private final AreaRepository areaRepository;
	private final ParkingTransactionRepository ptRepository;

	@Autowired
	public MonitorService(AreaRepository areaRepository, ParkingTransactionRepository ptRepository) {
		this.areaRepository = areaRepository;
		this.ptRepository = ptRepository;
	}

	@RequestMapping("/getAreas")
	public List<AreaInfo> getAreas() {
		Iterable<Area> areas = areaRepository.findAll();
		List<AreaInfo> areaInfos = new ArrayList<>();
		for (Area area : areas) {
			Long runningTxs = ptRepository.getOpenTransactionByAreaCount(area.getId());
			areaInfos.add(new AreaInfo(area.getName(), area.getCapacity(), area.getOccupied(), runningTxs));
		}
		return areaInfos;
	}
}
