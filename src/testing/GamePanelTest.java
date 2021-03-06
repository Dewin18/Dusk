package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Main.GamePanel;

/** PTP 2017
 * Test for the handling the initialisation of the GamePanel.
 *
 * @author Dewin Bagci
 * @version 16.08.
 * @since 15.06.
 */
public class GamePanelTest
{
    private GamePanel gamePanel;

    public GamePanelTest()
    {
        super();
        init();
    }

    @Before
    public void init()
    {
        gamePanel = new GamePanel();
    }

    @Test
    public void gamePanelSizeTest()
    {
        assertEquals(1300, GamePanel.WIDTH);
        assertEquals(600, GamePanel.HEIGHT);
    }
    
    @Test
    public void gameLoopIsRunning()
    {
        assertTrue(gamePanel.getGameLoopState());
    }
}
