package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import Entity.EvilTwin;
import TileMap.TileMap;

/** PTP 2017
 * Test for the purpose of life of the evil twin.
 *
 * @author Dewin Bagci
 * @version 16.08.
 * @since 16.08.
 */
public class EvilTwinTest
{
    private EvilTwin evilTwin;
    private final int health = 30;
    private final int damage = 1;
    
    public EvilTwinTest()
    {
        super();
        initEvilTwin();
    }

    @Before
    public void initEvilTwin()
    {
        TileMap tm = new TileMap(128);
        evilTwin = new EvilTwin(tm);
    }
    
    @Test
    public void evilTwinDefaultInitialisationTest()
    {
        assertEquals(health, evilTwin.getHealth());
        assertEquals(damage, evilTwin.getDamage());
    }
}
