package tibano.entity;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ParkingTransactionRepositoryTest {
	@Autowired
	private ParkingTransactionRepository target;
	@Autowired
	private AreaRepository areaRepository;
	@Test
	public void read()
	{
		Area area = new Area("area", 30L);
		area = areaRepository.save(area);
		ParkingTransaction pt = new ParkingTransaction(area);
		pt = target.save(pt);
		Assert.assertEquals(target.findOne(pt.getId()).getArea().getName(), "area");
	}
}
