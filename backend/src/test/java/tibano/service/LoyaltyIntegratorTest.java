package tibano.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

public class LoyaltyIntegratorTest {

	@Test
	public void addPoints() throws InterruptedException, ExecutionException {
		Future<Boolean> result = new LoyaltyIntegrator().addPoints(0);
		while(!result.isDone())
			Thread.sleep(10); //10-millisecond pause between each check
		Assert.assertTrue(result.get());
	}
}
