package tibano.service;

import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import tibano.dto.AreaInfo;
import tibano.entity.Area;
import tibano.entity.AreaRepository;
import tibano.entity.ParkingTransactionRepository;

@RunWith(SpringRunner.class)
public class MonitorServiceTest {

	@Mock
    private AreaRepository areaRepository;
	@Mock
	private ParkingTransactionRepository ptRepository;
    private MonitorService target;

    @Before
    public void setup() {
    	MockitoAnnotations.initMocks(this);
    	target = new MonitorService(areaRepository, ptRepository);
    }

    @Test
    public void getAreas() throws Exception {
        Long areaId = 4711l;
        Long occupiedBefore = 5l;
        Area area = new Area("Bahnhof Nord", 12l, occupiedBefore);
        Set<Area> areas = new HashSet<>();
        areas.add(area);
        when(areaRepository.findAll()).thenReturn(areas);

        List<AreaInfo> areaInfos = target.getAreas();

        Assert.assertEquals(areaInfos.size(), 1);
    }
}