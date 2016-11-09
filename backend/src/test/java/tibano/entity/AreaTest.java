package tibano.entity;

import org.junit.Assert;
import org.junit.Test;

public class AreaTest {

    @Test
    public void testIncrement()  {
        Area a = new Area("Name", 12l, 5l);
        a.incrementOccupied();
        Assert.assertEquals(6, a.getOccupied().intValue());
    }

    @Test
    public void testDecrement()  {
        Area a = new Area("Name", 12l, 5l);
        a.decrementOccupied();
        Assert.assertEquals(4, a.getOccupied().intValue());
    }

    @Test
    public void testNoDecrementBelowZero()  {
        Area a = new Area("Name", 12l, 0l);
        a.decrementOccupied();
        Assert.assertEquals(0, a.getOccupied().intValue());
    }

}