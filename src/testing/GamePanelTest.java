package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Main.GamePanel;

public class GamePanelTest
{
    GamePanel gamePanel;

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
