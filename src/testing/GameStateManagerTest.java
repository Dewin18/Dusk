package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import GameState.GameStateManager;

public class GameStateManagerTest
{
    GameStateManager gsm;

    public GameStateManagerTest()
    {
        super();
        init();
    }

    private void init()
    {
        gsm = new GameStateManager();
    }

    @Test
    // default state should be MenuState
    public void defaultStateIsMenuStateTest()
    {
        assertTrue(gsm.getState(0) != null);
        assertEquals("MenuState", gsm.getState(0).toString());
    }
    
//    @Test
    // change states in GameStateManager
    public void setMenuStateTest()
    {
     
    }
    
    @Test
    // change states in GameStateManager
    public void setLevel1StateTest()
    {
        assertTrue(gsm.getState(1) == null);
        gsm.setState(1);
        assertEquals("Level1State", gsm.getState(1).toString());
    }
    
    
    @Test
    public void setOptionStateTest()
    {
        assertTrue(gsm.getState(2) == null);
        gsm.setState(2);
        assertEquals("OptionState", gsm.getState(2).toString());
    }
}
