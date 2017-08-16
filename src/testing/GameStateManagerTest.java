package testing;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import GameState.GameState;
import GameState.GameStateManager;

/** PTP 2017
 * Test for the handling of the managing of the GameStates.
 *
 * @author Dewin Bagci
 * @version 16.08.
 * @since 11.08.
 */
public class GameStateManagerTest
{
    private GameStateManager gsm;
    private GameState defaultState;

    public GameStateManagerTest()
    {
        super();
        init();
    }

    @Before
    public void init()
    {
        gsm = new GameStateManager();
        defaultState = gsm.getState(gsm.getCurrentStateNumber());
    }

    @Test
    // default state should be MenuState
    public void defaultStateIsMenuStateTest()
    {
        assertEquals(defaultState, gsm.getState(GameStateManager.MENUSTATE));
    }
    
     @Test
    // change state to Level1State in GameStateManager
    public void setLevel1StateTest()
    {
        gsm.setState(1);
        defaultState = gsm.getState(gsm.getCurrentStateNumber());
        assertEquals(defaultState, gsm.getState(GameStateManager.LEVEL1STATE));
    }
    
    @Test
    // change state to OptionState in GameStateManager
    public void setOptionStateTest()
    {
        gsm.setState(2);
        defaultState = gsm.getState(gsm.getCurrentStateNumber());
        assertEquals(defaultState, gsm.getState(GameStateManager.OPTIONSTATE));
    }
}
