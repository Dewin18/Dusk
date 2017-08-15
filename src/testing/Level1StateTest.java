package testing;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import GameState.GameStateManager;
import GameState.Level1State;

public class Level1StateTest
{
    GameStateManager gsm;
    Level1State level1State;
    
    private final int DEFAULT_ENEMY_AMOUNT = 5;
    
    public Level1StateTest()
    {
        super();
        init();
    }

    @Before
    public void init()
    {
        gsm = new GameStateManager();
        gsm.setState(GameStateManager.LEVEL1STATE);
        
        level1State = (Level1State) gsm.getState(GameStateManager.LEVEL1STATE);
    }
    
    @Test
    public void enemyListTest()
    {
        assertTrue(level1State.getEnemyList().size() == DEFAULT_ENEMY_AMOUNT);
    }
}
