package tibano.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import tibano.dto.CurrentAreaUtilization;
import tibano.entity.Area;
import tibano.entity.AreaRepository;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CounterTest {

    private AreaRepository areaRepository;
    private Counter counter;

    @Before
    public void setup() {
        areaRepository = mock(AreaRepository.class);
        counter = new Counter(areaRepository);
    }

    @Test
    public void entry() throws Exception {
        Long areaId = 4711l;
        Long occupiedBefore = 5l;
        Area before = new Area("Bahnhof Nord", 12l, occupiedBefore);
        when(areaRepository.findOne(areaId)).thenReturn(before);

        CurrentAreaUtilization after = counter.entry(areaId);

        Assert.assertEquals(before.getCapacity(), after.getCapacity());
        Assert.assertEquals(occupiedBefore+1, after.getOccupied().intValue());
    }

    @Test
    public void exit() throws Exception {
        Long areaId = 4711l;
        Long occupiedBefore = 5l;
        Area before = new Area("Bahnhof Nord", 12l, occupiedBefore);
        when(areaRepository.findOne(areaId)).thenReturn(before);

        CurrentAreaUtilization after = counter.exit(areaId);

        Assert.assertEquals(before.getCapacity(), after.getCapacity());
        Assert.assertEquals(occupiedBefore-1, after.getOccupied().intValue());
    }

}