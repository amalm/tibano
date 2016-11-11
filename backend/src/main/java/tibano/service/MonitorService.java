package tibano.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tibano.dto.AreaInfo;
import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.ParkingTransactionRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
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
			areaInfos.add(new AreaInfo(area.getId(), area.getName(), area.getCapacity(), area.getOccupied(), runningTxs,
					area.getLongitude(), area.getLatitude()));
		}
		return areaInfos;
	}
}
