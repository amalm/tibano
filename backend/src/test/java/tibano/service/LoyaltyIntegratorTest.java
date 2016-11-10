package tibano.service;

import org.junit.Assert;
import org.junit.Test;

public class LoyaltyIntegratorTest {

	@Test
	public void addPoints() {
		Assert.assertTrue(new LoyaltyIntegrator().addPoints(0));
	}
}
