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
        assertEquals("MenuState", gsm.getState(0).toString());
    }
    
 //   @Test
    // change states in GameStateManager
    public void changeStateTest()
    {
        assertTrue(gsm.getState(0) != null);
        assertTrue(gsm.getState(1) == null);
        assertTrue(gsm.getState(2) == null);
        
        gsm.setState(1);
        assertEquals("Level1State", gsm.getState(1).toString());
        
        gsm.setState(2);
        assertEquals("OptionState", gsm.getState(2).toString());
    }
}
