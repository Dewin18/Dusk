package testing;

import static org.junit.Assert.assertEquals;

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
    public void defaultStateIsMenuStateTest()
    {
        assertEquals("MenuState", gsm.getState(0).toString());
    }
}
