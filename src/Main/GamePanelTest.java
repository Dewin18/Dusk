package Main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
        assertEquals(1200, gamePanel.WIDTH);
        assertEquals(600, gamePanel.HEIGHT);
    }
}
